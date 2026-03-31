package com.apigateway.filter;

import com.apigateway.model.RateLimitPolicy;
import com.apigateway.model.RateLimitState;
import com.apigateway.repository.RateLimitPolicyRepository;
import com.apigateway.repository.RateLimitStateRepository;
import com.apigateway.service.RateLimitPolicyService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RateLimitingFilter extends OncePerRequestFilter {
    private final RateLimitPolicyRepository policyRepository;
    private final RateLimitStateRepository stateRepository;
    private final RateLimitPolicyService rateLimitPolicyService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String route = request.getRequestURI();
        Optional<RateLimitPolicy> policyOpt = policyRepository.findAll().stream()
                .filter(p -> route.startsWith("/api") && route.contains(p.getRouteId().toString())) 
                .findFirst();
        if (policyOpt.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }
        RateLimitPolicy policy = policyOpt.get();

        String key;
        switch (policy.getPerType().toUpperCase()) {
            case "USER":
                key = request.getHeader("X-User-Id");
                if (key == null || key.isEmpty()) {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.getWriter().write("User ID required for rate limiting.");
                    return;
                }
                break;
            case "IP":
                key = request.getRemoteAddr();
                break;
            case "GLOBAL":
                key = "GLOBAL";
                break;
            default:
                key = request.getRemoteAddr(); // fallback to IP
        }
        String stateKey = route + ":" + key;
        RateLimitState state = stateRepository.findById(stateKey).orElse(null);
        if (state == null) {
            state = RateLimitState.builder().key(stateKey).build();
            stateRepository.save(state);
        }
        boolean allowed = rateLimitPolicyService.isRequestAllowed(stateKey, policy, state);
        if (!allowed) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Rate limit exceeded. Try again later.");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
