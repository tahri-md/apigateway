package com.apigateway.ratelimit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RedisSlidingWindowService {
    private final StringRedisTemplate redisTemplate;

    public RedisSlidingWindowService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isRequestAllowed(String key, int limit, int windowSeconds) {
        String timestampsKey = key + ":timestamps";
        long now = System.currentTimeMillis() / 1000;
        long windowStart = now - windowSeconds;
        // Add current timestamp
        redisTemplate.opsForList().rightPush(timestampsKey, String.valueOf(now));
        // Remove timestamps outside window
        List<String> allTimestamps = redisTemplate.opsForList().range(timestampsKey, 0, -1);
        List<String> validTimestamps = allTimestamps == null ? List.of() : allTimestamps.stream()
                .filter(ts -> Long.parseLong(ts) >= windowStart)
                .collect(Collectors.toList());
        redisTemplate.delete(timestampsKey);
        validTimestamps.forEach(ts -> redisTemplate.opsForList().rightPush(timestampsKey, ts));
        // Check limit
        return validTimestamps.size() <= limit;
    }
}
