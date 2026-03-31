package com.apigateway.dto;

import java.time.LocalDateTime;

public class RateLimitStateDto {
    private String key;
    private int limitCount;
    private int remainingCount;
    private LocalDateTime resetAt;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdated;

    public RateLimitStateDto() {}

    public RateLimitStateDto(String key, int limitCount, int remainingCount, LocalDateTime resetAt,
                            LocalDateTime createdAt, LocalDateTime lastUpdated) {
        this.key = key;
        this.limitCount = limitCount;
        this.remainingCount = remainingCount;
        this.resetAt = resetAt;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
    }

    public String getKey() { return key; }
    public int getLimitCount() { return limitCount; }
    public int getRemainingCount() { return remainingCount; }
    public LocalDateTime getResetAt() { return resetAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }

    public void setKey(String key) { this.key = key; }
    public void setLimitCount(int limitCount) { this.limitCount = limitCount; }
    public void setRemainingCount(int remainingCount) { this.remainingCount = remainingCount; }
    public void setResetAt(LocalDateTime resetAt) { this.resetAt = resetAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }

    public static RateLimitStateDtoBuilder builder() { return new RateLimitStateDtoBuilder(); }

    public static class RateLimitStateDtoBuilder {
        private String key;
        private int limitCount;
        private int remainingCount;
        private LocalDateTime resetAt;
        private LocalDateTime createdAt;
        private LocalDateTime lastUpdated;

        public RateLimitStateDtoBuilder key(String key) { this.key = key; return this; }
        public RateLimitStateDtoBuilder limitCount(int limitCount) { this.limitCount = limitCount; return this; }
        public RateLimitStateDtoBuilder remainingCount(int remainingCount) { this.remainingCount = remainingCount; return this; }
        public RateLimitStateDtoBuilder resetAt(LocalDateTime resetAt) { this.resetAt = resetAt; return this; }
        public RateLimitStateDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public RateLimitStateDtoBuilder lastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; return this; }

        public RateLimitStateDto build() {
            return new RateLimitStateDto(key, limitCount, remainingCount, resetAt, createdAt, lastUpdated);
        }
    }
}
