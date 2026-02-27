package com.apigateway.filter;

import com.apigateway.model.RateLimitPolicy;
import com.apigateway.model.RateLimitState;
import com.apigateway.repository.RateLimitPolicyRepository;
import com.apigateway.repository.RateLimitStateRepository;
import com.apigateway.service.RateLimitPolicyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RateLimitingFilterTest {
    @Mock
    private RateLimitPolicyRepository policyRepository;
    @Mock
    private RateLimitStateRepository stateRepository;
    @Mock
    private RateLimitPolicyService rateLimitPolicyService;
    @InjectMocks
    private RateLimitingFilter filter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void allowsRequestWhenUnderLimit() throws Exception {
        String route = "/api/test";
        String key = "127.0.0.1";
        String stateKey = route + ":" + key;
        RateLimitPolicy policy = RateLimitPolicy.builder()
                .id(UUID.randomUUID())
                .perType("IP")
                .windowDurationSeconds(60)
                .limitCount(10)
                .build();
        RateLimitState state = RateLimitState.builder().key(stateKey).build();
        when(policyRepository.findAll()).thenReturn(java.util.List.of(policy));
        when(stateRepository.findById(stateKey)).thenReturn(Optional.of(state));
        when(rateLimitPolicyService.isRequestAllowed(stateKey, policy, state)).thenReturn(true);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI(route);
        request.setRemoteAddr(key);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilterInternal(request, response, chain);
        assertEquals(200, response.getStatus());
    }

    @Test
    void blocksRequestWhenOverLimit() throws Exception {
        String route = "/api/test";
        String key = "127.0.0.1";
        String stateKey = route + ":" + key;
        RateLimitPolicy policy = RateLimitPolicy.builder()
                .id(UUID.randomUUID())
                .perType("IP")
                .windowDurationSeconds(60)
                .limitCount(10)
                .build();
        RateLimitState state = RateLimitState.builder().key(stateKey).build();
        when(policyRepository.findAll()).thenReturn(java.util.List.of(policy));
        when(stateRepository.findById(stateKey)).thenReturn(Optional.of(state));
        when(rateLimitPolicyService.isRequestAllowed(stateKey, policy, state)).thenReturn(false);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI(route);
        request.setRemoteAddr(key);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilterInternal(request, response, chain);
        assertEquals(429, response.getStatus());
        assertTrue(response.getContentAsString().contains("Rate limit exceeded"));
    }
}
