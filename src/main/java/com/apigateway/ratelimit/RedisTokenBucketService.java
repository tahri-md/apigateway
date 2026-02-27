package com.apigateway.ratelimit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import java.util.Arrays;

@Service
public class RedisTokenBucketService {
    private final StringRedisTemplate redisTemplate;
    private final RedisScript<Long> tokenBucketScript;

    public RedisTokenBucketService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.tokenBucketScript = RedisScript.of(
            "local tokens = tonumber(redis.call('get', KEYS[1]) or ARGV[1]) " +
            "local lastRefill = tonumber(redis.call('get', KEYS[2]) or ARGV[2]) " +
            "local now = tonumber(ARGV[3]) " +
            "local refillRate = tonumber(ARGV[4]) " +
            "local capacity = tonumber(ARGV[1]) " +
            "local elapsed = now - lastRefill " +
            "local add = math.floor(elapsed * refillRate) " +
            "tokens = math.min(capacity, tokens + add) " +
            "if add > 0 then lastRefill = now end " +
            "if tokens > 0 then " +
            "  tokens = tokens - 1 " +
            "  redis.call('set', KEYS[1], tokens) " +
            "  redis.call('set', KEYS[2], lastRefill) " +
            "  return 1 " +
            "else " +
            "  redis.call('set', KEYS[1], tokens) " +
            "  redis.call('set', KEYS[2], lastRefill) " +
            "  return 0 " +
            "end",
            Long.class
        );
    }

    public boolean isRequestAllowed(String key, int capacity, double refillRate) {
        long now = System.currentTimeMillis() / 1000; // seconds
        Long allowed = redisTemplate.execute(
            tokenBucketScript,
            Arrays.asList(key + ":tokens", key + ":lastRefill"),
            String.valueOf(capacity), "0", String.valueOf(now), String.valueOf(refillRate)
        );
        return allowed != null && allowed == 1;
    }
}
