package com.apigateway.filter;

import com.apigateway.model.Route;
import com.apigateway.repository.RouteRepository;
import com.apigateway.service.CircuitBreakerService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class CircuitBreakerFilter extends OncePerRequestFilter {
    private final CircuitBreakerService circuitBreakerService;
    private final RouteRepository routeRepository;

    public CircuitBreakerFilter(CircuitBreakerService circuitBreakerService, RouteRepository routeRepository) {
        this.circuitBreakerService = circuitBreakerService;
        this.routeRepository = routeRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Only apply circuit breaker check to API routes
        if (!request.getRequestURI().startsWith("/api")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Find the route that matches the request path
        String requestPath = request.getRequestURI();
        Optional<Route> matchingRoute = routeRepository.findAll().stream()
                .filter(route -> route.isActive() && requestPath.startsWith(route.getPath()))
                .findFirst();

        if (matchingRoute.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        Route route = matchingRoute.get();
        String serviceName = route.getServiceName();

        // Check if the circuit breaker allows the request
        if (!circuitBreakerService.allowRequest(serviceName)) {
            response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"status\": 503, \"message\": \"Service temporarily unavailable. Circuit breaker is OPEN.\", \"error\": \"Circuit Breaker Open\", \"path\": \"" + requestPath + "\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
