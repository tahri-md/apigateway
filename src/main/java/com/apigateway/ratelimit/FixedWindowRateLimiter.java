package com.apigateway.ratelimit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apigateway.model.RateLimitPolicy;
import com.apigateway.model.RateLimitState;
import com.apigateway.repository.RateLimitStateRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class FixedWindowRateLimiter implements RateLimiterAlgorithm {
    @Autowired
    private RateLimitStateRepository stateRepository;
    @Override
    public String getType() {
        return "FIXED_WINDOW";
    }
    @Override
    public boolean isRequestAllowed(String key, RateLimitPolicy policy, RateLimitState state) {
        try {
            long currentTime = System.currentTimeMillis();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(state.getAlgoState());
            long windowStart = jsonNode.get("windowStart").asLong();
            int count = jsonNode.get("count").asInt();
            long windowDurationMillis = policy.getWindowDurationSeconds() * 1000L;
            if (currentTime < windowStart + windowDurationMillis) {
                if (count < policy.getLimitCount()) {
                    ((ObjectNode) jsonNode).put("count", count + 1);
                    state.setAlgoState(jsonNode.toString());
                    stateRepository.save(state);
                    return true;
                }
                return false;
            } else {
                ((ObjectNode) jsonNode).put("windowStart", currentTime);
                ((ObjectNode) jsonNode).put("count", 1);
                state.setAlgoState(jsonNode.toString());
                stateRepository.save(state);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}