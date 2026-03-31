package com.apigateway.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "circuit_breaker_state", uniqueConstraints = {
    @UniqueConstraint(columnNames = "service_name")
})
public class CircuitBreakerState {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "service_name", length = 100, nullable = false, unique = true)
    private String serviceName;

    @Column(name = "state", length = 50, nullable = false)
    private String state; // CLOSED, OPEN, HALF_OPEN

    @Column(name = "failure_count")
    private Integer failureCount = 0;

    @Column(name = "failure_rate", precision = 5, scale = 2)
    private Double failureRate = 0.0;

    @Column(name = "last_failure_time")
    private LocalDateTime lastFailureTime;

    @Column(name = "open_at")
    private LocalDateTime openAt;

    @Column(name = "half_open_at")
    private LocalDateTime halfOpenAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated = LocalDateTime.now();

    // No-args constructor required by JPA/Hibernate
    public CircuitBreakerState() {}

    // All-args constructor
    public CircuitBreakerState(UUID id, String serviceName, String state, Integer failureCount, Double failureRate,
                              LocalDateTime lastFailureTime, LocalDateTime openAt, LocalDateTime halfOpenAt,
                              LocalDateTime createdAt, LocalDateTime lastUpdated) {
        this.id = id;
        this.serviceName = serviceName;
        this.state = state;
        this.failureCount = failureCount;
        this.failureRate = failureRate;
        this.lastFailureTime = lastFailureTime;
        this.openAt = openAt;
        this.halfOpenAt = halfOpenAt;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
    }

    // Getters
    public UUID getId() { return id; }
    public String getServiceName() { return serviceName; }
    public String getState() { return state; }
    public Integer getFailureCount() { return failureCount; }
    public Double getFailureRate() { return failureRate; }
    public LocalDateTime getLastFailureTime() { return lastFailureTime; }
    public LocalDateTime getOpenAt() { return openAt; }
    public LocalDateTime getHalfOpenAt() { return halfOpenAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }

    // Setters
    public void setId(UUID id) { this.id = id; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public void setState(String state) { this.state = state; }
    public void setFailureCount(Integer failureCount) { this.failureCount = failureCount; }
    public void setFailureRate(Double failureRate) { this.failureRate = failureRate; }
    public void setLastFailureTime(LocalDateTime lastFailureTime) { this.lastFailureTime = lastFailureTime; }
    public void setOpenAt(LocalDateTime openAt) { this.openAt = openAt; }
    public void setHalfOpenAt(LocalDateTime halfOpenAt) { this.halfOpenAt = halfOpenAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }

    // Builder
    public static CircuitBreakerStateBuilder builder() { return new CircuitBreakerStateBuilder(); }

    public static class CircuitBreakerStateBuilder {
        private UUID id;
        private String serviceName;
        private String state;
        private Integer failureCount = 0;
        private Double failureRate = 0.0;
        private LocalDateTime lastFailureTime;
        private LocalDateTime openAt;
        private LocalDateTime halfOpenAt;
        private LocalDateTime createdAt;
        private LocalDateTime lastUpdated;

        public CircuitBreakerStateBuilder id(UUID id) { this.id = id; return this; }
        public CircuitBreakerStateBuilder serviceName(String serviceName) { this.serviceName = serviceName; return this; }
        public CircuitBreakerStateBuilder state(String state) { this.state = state; return this; }
        public CircuitBreakerStateBuilder failureCount(Integer failureCount) { this.failureCount = failureCount; return this; }
        public CircuitBreakerStateBuilder failureRate(Double failureRate) { this.failureRate = failureRate; return this; }
        public CircuitBreakerStateBuilder lastFailureTime(LocalDateTime lastFailureTime) { this.lastFailureTime = lastFailureTime; return this; }
        public CircuitBreakerStateBuilder openAt(LocalDateTime openAt) { this.openAt = openAt; return this; }
        public CircuitBreakerStateBuilder halfOpenAt(LocalDateTime halfOpenAt) { this.halfOpenAt = halfOpenAt; return this; }
        public CircuitBreakerStateBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public CircuitBreakerStateBuilder lastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; return this; }

        public CircuitBreakerState build() {
            return new CircuitBreakerState(id, serviceName, state, failureCount, failureRate, lastFailureTime, openAt, halfOpenAt, createdAt, lastUpdated);
        }
    }
}
