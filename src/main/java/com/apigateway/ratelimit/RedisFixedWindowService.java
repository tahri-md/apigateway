package com.apigateway.ratelimit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisFixedWindowService {
    private final StringRedisTemplate redisTemplate;

    public RedisFixedWindowService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isRequestAllowed(String key, int limit, int windowSeconds) {
        String countKey = key + ":count";
        String windowKey = key + ":window";
        long currentWindow = System.currentTimeMillis() / 1000 / windowSeconds;
        String windowValue = String.valueOf(currentWindow);
        String storedWindow = redisTemplate.opsForValue().get(windowKey);
        if (storedWindow == null || !storedWindow.equals(windowValue)) {
            // New window, reset count
            redisTemplate.opsForValue().set(windowKey, windowValue);
            redisTemplate.opsForValue().set(countKey, "1");
            return true;
        }
        Long count = redisTemplate.opsForValue().increment(countKey);
        if (count != null && count <= limit) {
            return true;
        }
        return false;
    }
}
