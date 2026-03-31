package com.apigateway.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rate_limit_state")
public class RateLimitState {
    @Id
    @Column(name = "key", length = 255, nullable = false)
    private String key;

    @Column(name = "limit_count")
    private Integer limitCount;

    @Column(name = "remaining_count")
    private Integer remainingCount;

    @Column(name = "reset_at")
    private LocalDateTime resetAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    // Flexible JSON field for algorithm-specific state (timestamps, tokens, etc.)
    @Lob
    @Column(name = "algo_state", columnDefinition = "TEXT")
    private String algoState;

    // No-args constructor required by JPA/Hibernate
    public RateLimitState() {}

    // All-args constructor
    public RateLimitState(String key, Integer limitCount, Integer remainingCount, LocalDateTime resetAt,
                         LocalDateTime createdAt, LocalDateTime lastUpdated, String algoState) {
        this.key = key;
        this.limitCount = limitCount;
        this.remainingCount = remainingCount;
        this.resetAt = resetAt;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
        this.algoState = algoState;
    }

    // Getters
    public String getKey() { return key; }
    public Integer getLimitCount() { return limitCount; }
    public Integer getRemainingCount() { return remainingCount; }
    public LocalDateTime getResetAt() { return resetAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public String getAlgoState() { return algoState; }

    // Setters
    public void setKey(String key) { this.key = key; }
    public void setLimitCount(Integer limitCount) { this.limitCount = limitCount; }
    public void setRemainingCount(Integer remainingCount) { this.remainingCount = remainingCount; }
    public void setResetAt(LocalDateTime resetAt) { this.resetAt = resetAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
    public void setAlgoState(String algoState) { this.algoState = algoState; }

    // Builder
    public static RateLimitStateBuilder builder() { return new RateLimitStateBuilder(); }

    public static class RateLimitStateBuilder {
        private String key;
        private Integer limitCount;
        private Integer remainingCount;
        private LocalDateTime resetAt;
        private LocalDateTime createdAt;
        private LocalDateTime lastUpdated;
        private String algoState;

        public RateLimitStateBuilder key(String key) { this.key = key; return this; }
        public RateLimitStateBuilder limitCount(Integer limitCount) { this.limitCount = limitCount; return this; }
        public RateLimitStateBuilder remainingCount(Integer remainingCount) { this.remainingCount = remainingCount; return this; }
        public RateLimitStateBuilder resetAt(LocalDateTime resetAt) { this.resetAt = resetAt; return this; }
        public RateLimitStateBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public RateLimitStateBuilder lastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; return this; }
        public RateLimitStateBuilder algoState(String algoState) { this.algoState = algoState; return this; }

        public RateLimitState build() {
            return new RateLimitState(key, limitCount, remainingCount, resetAt, createdAt, lastUpdated, algoState);
        }
    }
}
