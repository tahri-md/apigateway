package com.apigateway.ratelimit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.apigateway.model.RateLimitPolicy;
import com.apigateway.model.RateLimitState;
import com.apigateway.repository.RateLimitStateRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class TokenBucketRateLimiter implements RateLimiterAlgorithm {
    @Autowired
    private RateLimitStateRepository stateRepository;
    @Override
    public String getType() {
        return "TOKEN_BUCKET";
    }

    @Override
    public boolean isRequestAllowed(String key, RateLimitPolicy policy, RateLimitState state) {
        RateLimitState stateDto = stateRepository.findByKey(key).orElseThrow();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = mapper.readTree(stateDto.getAlgoState());
            int tokens = jsonNode.get("tokens").asInt();
            long lastRefill = jsonNode.get("lastRefill").asLong();
            int capacity = policy.getLimitCount();
            double refillRate = capacity / (double) policy.getWindowDurationSeconds();
            long currentTime = System.currentTimeMillis();
            long elapsedMillis = currentTime - lastRefill;
            double elapsedSeconds = elapsedMillis / 1000.0;
            int tokensToAdd = (int) (elapsedSeconds * refillRate);
            if (tokensToAdd > 0) {
                tokens = Math.min(capacity, tokens + tokensToAdd);
                lastRefill = currentTime;
            }
            if (tokens > 0) {
                tokens--;
                ((com.fasterxml.jackson.databind.node.ObjectNode) jsonNode).put("tokens", tokens);
                ((com.fasterxml.jackson.databind.node.ObjectNode) jsonNode).put("lastRefill", lastRefill);
                stateDto.setAlgoState(jsonNode.toString());
                stateRepository.save(stateDto);
                return true;
            }
            ((com.fasterxml.jackson.databind.node.ObjectNode) jsonNode).put("lastRefill", lastRefill);
            stateDto.setAlgoState(jsonNode.toString());
            stateRepository.save(stateDto);
            return false;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }
}