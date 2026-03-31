package com.apigateway.filter;

import com.apigateway.model.Route;
import com.apigateway.model.RouteTarget;
import com.apigateway.repository.RouteRepository;
import com.apigateway.repository.RouteTargetRepository;
import com.apigateway.service.CircuitBreakerService;
import com.apigateway.service.RequestLogService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class RequestRoutingFilter extends OncePerRequestFilter {
    private final RestTemplate restTemplate;
    private final RouteRepository routeRepository;
    private final RouteTargetRepository routeTargetRepository;
    private final CircuitBreakerService circuitBreakerService;
    private final RequestLogService requestLogService;

    public RequestRoutingFilter(RestTemplate restTemplate, RouteRepository routeRepository,
                               RouteTargetRepository routeTargetRepository, CircuitBreakerService circuitBreakerService,
                               RequestLogService requestLogService) {
        this.restTemplate = restTemplate;
        this.routeRepository = routeRepository;
        this.routeTargetRepository = routeTargetRepository;
        this.circuitBreakerService = circuitBreakerService;
        this.requestLogService = requestLogService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Only apply routing to API routes (not authentication, actuator, etc.)
        String requestPath = request.getRequestURI();
        if (!requestPath.startsWith("/api") || requestPath.startsWith("/api/auth") || requestPath.startsWith("/api/logs") || requestPath.startsWith("/api/audit")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Find the route that matches the request path
        Optional<Route> matchingRoute = routeRepository.findAll().stream()
                .filter(route -> route.isActive() && requestPath.startsWith(route.getPath()))
                .findFirst();

        if (matchingRoute.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        Route route = matchingRoute.get();
        String serviceName = route.getServiceName();

        // Get active route targets
        List<RouteTarget> targets = routeTargetRepository.findAll().stream()
                .filter(target -> target.getRoute().getId().equals(route.getId()) && target.isActive())
                .toList();

        if (targets.isEmpty()) {
            response.setStatus(503);
            response.setContentType("application/json");
            response.getWriter().write("{\"status\": 503, \"message\": \"No active backend targets available\", \"error\": \"Service Unavailable\"}");
            return;
        }

        // Select the first active target (can implement load balancing here)
        RouteTarget selected = targets.get(0);
        String targetUrl = selected.getInstanceUrl();

        // Adjust target URL to include the original request path
        String forwardUrl = targetUrl + requestPath;

        try {
            // Build request headers
            HttpHeaders headers = buildHeaders(request);

            // Forward the request
            HttpMethod method = HttpMethod.valueOf(request.getMethod());
            ResponseEntity<String> backendResponse = restTemplate.exchange(
                    forwardUrl,
                    method,
                    new HttpEntity<>(headers),
                    String.class
            );

            // Set response status
            response.setStatus(backendResponse.getStatusCode().value());

            // Copy response headers
            backendResponse.getHeaders().forEach((name, values) -> {
                if (!isHopByHopHeader(name)) {
                    values.forEach(value -> response.addHeader(name, value));
                }
            });

            // Write response body
            response.setContentType(backendResponse.getHeaders().getContentType() != null
                    ? backendResponse.getHeaders().getContentType().toString()
                    : "application/json");
            response.getWriter().write(backendResponse.getBody() != null ? backendResponse.getBody() : "");

            // Record success in circuit breaker
            circuitBreakerService.recordSuccess(serviceName);

        } catch (Exception e) {
            // Record failure in circuit breaker
            circuitBreakerService.recordFailure(serviceName);

            // Return error response
            response.setStatus(503);
            response.setContentType("application/json");
            response.getWriter().write("{\"status\": 503, \"message\": \"Backend service error: " + e.getMessage() + "\", \"error\": \"Service Unavailable\"}");
        }
    }

    private HttpHeaders buildHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();

        // Copy relevant request headers
        java.util.Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (!isHopByHopHeader(headerName) && !headerName.equalsIgnoreCase("host")) {
                String headerValue = request.getHeader(headerName);
                headers.add(headerName, headerValue);
            }
        }

        // Add forwarded headers
        headers.add("X-Forwarded-For", request.getRemoteAddr());
        headers.add("X-Forwarded-Proto", request.getScheme());
        headers.add("X-Forwarded-Host", request.getServerName());

        return headers;
    }

    private boolean isHopByHopHeader(String header) {
        String lowerHeader = header.toLowerCase();
        return lowerHeader.equals("connection")
                || lowerHeader.equals("keep-alive")
                || lowerHeader.equals("transfer-encoding")
                || lowerHeader.equals("upgrade")
                || lowerHeader.equals("proxy-authenticate")
                || lowerHeader.equals("proxy-authorization")
                || lowerHeader.equals("te")
                || lowerHeader.equals("trailer");
    }
}
