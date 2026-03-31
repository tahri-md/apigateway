
package com.apigateway.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "rate_limit_policies")
public class RateLimitPolicy {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "route_id", nullable = false)
    private UUID routeId;

    @Column(name = "limit_count", nullable = false)
    private int limitCount;

    @Column(name = "window_duration_seconds", nullable = false)
    private int windowDurationSeconds;

    @Column(name = "per_type", nullable = false, length = 50)
    private String perType;

    @Enumerated(EnumType.STRING)
    @Column(name = "algorithm_type", nullable = false, length = 30)
    private RateLimitAlgorithmType algorithmType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // No-args constructor required by JPA/Hibernate
    public RateLimitPolicy() {}

    // All-args constructor
    public RateLimitPolicy(UUID id, UUID routeId, int limitCount, int windowDurationSeconds, 
                          String perType, RateLimitAlgorithmType algorithmType, LocalDateTime createdAt) {
        this.id = id;
        this.routeId = routeId;
        this.limitCount = limitCount;
        this.windowDurationSeconds = windowDurationSeconds;
        this.perType = perType;
        this.algorithmType = algorithmType;
        this.createdAt = createdAt;
    }

    // Getters
    public UUID getId() { return id; }
    public UUID getRouteId() { return routeId; }
    public int getLimitCount() { return limitCount; }
    public int getWindowDurationSeconds() { return windowDurationSeconds; }
    public String getPerType() { return perType; }
    public RateLimitAlgorithmType getAlgorithmType() { return algorithmType; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setId(UUID id) { this.id = id; }
    public void setRouteId(UUID routeId) { this.routeId = routeId; }
    public void setLimitCount(int limitCount) { this.limitCount = limitCount; }
    public void setWindowDurationSeconds(int windowDurationSeconds) { this.windowDurationSeconds = windowDurationSeconds; }
    public void setPerType(String perType) { this.perType = perType; }
    public void setAlgorithmType(RateLimitAlgorithmType algorithmType) { this.algorithmType = algorithmType; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Builder
    public static RateLimitPolicyBuilder builder() { return new RateLimitPolicyBuilder(); }

    public static class RateLimitPolicyBuilder {
        private UUID id;
        private UUID routeId;
        private int limitCount;
        private int windowDurationSeconds;
        private String perType;
        private RateLimitAlgorithmType algorithmType;
        private LocalDateTime createdAt;

        public RateLimitPolicyBuilder id(UUID id) { this.id = id; return this; }
        public RateLimitPolicyBuilder routeId(UUID routeId) { this.routeId = routeId; return this; }
        public RateLimitPolicyBuilder limitCount(int limitCount) { this.limitCount = limitCount; return this; }
        public RateLimitPolicyBuilder windowDurationSeconds(int windowDurationSeconds) { this.windowDurationSeconds = windowDurationSeconds; return this; }
        public RateLimitPolicyBuilder perType(String perType) { this.perType = perType; return this; }
        public RateLimitPolicyBuilder algorithmType(RateLimitAlgorithmType algorithmType) { this.algorithmType = algorithmType; return this; }
        public RateLimitPolicyBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public RateLimitPolicy build() {
            return new RateLimitPolicy(id, routeId, limitCount, windowDurationSeconds, perType, algorithmType, createdAt);
        }
    }
}
