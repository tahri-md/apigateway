package com.apigateway.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class RateLimitPolicyDto {
    private UUID id;
    private UUID routeId;
    private int limitCount;
    private int windowDurationSeconds;
    private String perType;
    private LocalDateTime createdAt;

    public RateLimitPolicyDto() {}

    public RateLimitPolicyDto(UUID id, UUID routeId, int limitCount, int windowDurationSeconds, 
                             String perType, LocalDateTime createdAt) {
        this.id = id;
        this.routeId = routeId;
        this.limitCount = limitCount;
        this.windowDurationSeconds = windowDurationSeconds;
        this.perType = perType;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public UUID getRouteId() { return routeId; }
    public int getLimitCount() { return limitCount; }
    public int getWindowDurationSeconds() { return windowDurationSeconds; }
    public String getPerType() { return perType; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(UUID id) { this.id = id; }
    public void setRouteId(UUID routeId) { this.routeId = routeId; }
    public void setLimitCount(int limitCount) { this.limitCount = limitCount; }
    public void setWindowDurationSeconds(int windowDurationSeconds) { this.windowDurationSeconds = windowDurationSeconds; }
    public void setPerType(String perType) { this.perType = perType; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static RateLimitPolicyDtoBuilder builder() { return new RateLimitPolicyDtoBuilder(); }

    public static class RateLimitPolicyDtoBuilder {
        private UUID id;
        private UUID routeId;
        private int limitCount;
        private int windowDurationSeconds;
        private String perType;
        private LocalDateTime createdAt;

        public RateLimitPolicyDtoBuilder id(UUID id) { this.id = id; return this; }
        public RateLimitPolicyDtoBuilder routeId(UUID routeId) { this.routeId = routeId; return this; }
        public RateLimitPolicyDtoBuilder limitCount(int limitCount) { this.limitCount = limitCount; return this; }
        public RateLimitPolicyDtoBuilder windowDurationSeconds(int windowDurationSeconds) { this.windowDurationSeconds = windowDurationSeconds; return this; }
        public RateLimitPolicyDtoBuilder perType(String perType) { this.perType = perType; return this; }
        public RateLimitPolicyDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public RateLimitPolicyDto build() {
            return new RateLimitPolicyDto(id, routeId, limitCount, windowDurationSeconds, perType, createdAt);
        }
    }
}
