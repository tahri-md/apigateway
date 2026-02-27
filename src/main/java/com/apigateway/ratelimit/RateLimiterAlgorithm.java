package com.apigateway.ratelimit;

import com.apigateway.model.RateLimitPolicy;
import com.apigateway.model.RateLimitState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface RateLimiterAlgorithm {
    String getType();
    boolean isRequestAllowed(String key, RateLimitPolicy policy, RateLimitState state) throws JsonMappingException, JsonProcessingException;
}