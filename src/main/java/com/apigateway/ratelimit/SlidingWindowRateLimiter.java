package com.apigateway.ratelimit;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class SlidingWindowRateLimiter implements RateLimiterAlgorithm {
    @Override
    public String getType() {
        return "SLIDING_WINDOW";
    }
    @Override
    public boolean isRequestAllowed(String key, com.apigateway.model.RateLimitPolicy policy, com.apigateway.model.RateLimitState state) {
        try {
            long currentTime = System.currentTimeMillis();
            long windowMillis = policy.getWindowDurationSeconds() * 1000L;
            int limit = policy.getLimitCount();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(state.getAlgoState());
            ArrayNode timestampsNode = (ArrayNode) jsonNode.get("timestamps");
            java.util.List<Long> timestamps = new java.util.ArrayList<>();
            if (timestampsNode != null) {
                for (JsonNode ts : timestampsNode) {
                    timestamps.add(ts.asLong());
                }
            }
            timestamps.removeIf(ts -> ts < currentTime - windowMillis);
            if (timestamps.size() < limit) {
                timestamps.add(currentTime);
                ArrayNode newTimestampsNode = mapper.createArrayNode();
                for (Long ts : timestamps) {
                    newTimestampsNode.add(ts);
                }
                ((ObjectNode) jsonNode).set("timestamps", newTimestampsNode);
                state.setAlgoState(jsonNode.toString());
                return true;
            }
            // Deny request
            ArrayNode newTimestampsNode = mapper.createArrayNode();
            for (Long ts : timestamps) {
                newTimestampsNode.add(ts);
            }
            ((ObjectNode) jsonNode).set("timestamps", newTimestampsNode);
            state.setAlgoState(jsonNode.toString());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}