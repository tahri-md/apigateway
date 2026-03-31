
package com.apigateway.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "routes")
public class Route {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @Column(name = "timeout_ms")
    private int timeoutMs;

    @Column(name = "retry_count")
    private int retryCount;

    @Column(name = "auth_required")
    private boolean authRequired;

    @Column(name = "auth_provider")
    private String authProvider;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // No-args constructor required by JPA/Hibernate
    public Route() {}

    // All-args constructor
    public Route(UUID id, String path, String serviceName, int timeoutMs, int retryCount, 
                 boolean authRequired, String authProvider, boolean isActive, 
                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.path = path;
        this.serviceName = serviceName;
        this.timeoutMs = timeoutMs;
        this.retryCount = retryCount;
        this.authRequired = authRequired;
        this.authProvider = authProvider;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public UUID getId() { return id; }
    public String getPath() { return path; }
    public String getServiceName() { return serviceName; }
    public int getTimeoutMs() { return timeoutMs; }
    public int getRetryCount() { return retryCount; }
    public boolean isAuthRequired() { return authRequired; }
    public String getAuthProvider() { return authProvider; }
    public boolean isActive() { return isActive; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(UUID id) { this.id = id; }
    public void setPath(String path) { this.path = path; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public void setTimeoutMs(int timeoutMs) { this.timeoutMs = timeoutMs; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }
    public void setAuthRequired(boolean authRequired) { this.authRequired = authRequired; }
    public void setAuthProvider(String authProvider) { this.authProvider = authProvider; }
    public void setIsActive(boolean isActive) { this.isActive = isActive; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Builder
    public static RouteBuilder builder() { return new RouteBuilder(); }

    public static class RouteBuilder {
        private UUID id;
        private String path;
        private String serviceName;
        private int timeoutMs;
        private int retryCount;
        private boolean authRequired;
        private String authProvider;
        private boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public RouteBuilder id(UUID id) { this.id = id; return this; }
        public RouteBuilder path(String path) { this.path = path; return this; }
        public RouteBuilder serviceName(String serviceName) { this.serviceName = serviceName; return this; }
        public RouteBuilder timeoutMs(int timeoutMs) { this.timeoutMs = timeoutMs; return this; }
        public RouteBuilder retryCount(int retryCount) { this.retryCount = retryCount; return this; }
        public RouteBuilder authRequired(boolean authRequired) { this.authRequired = authRequired; return this; }
        public RouteBuilder authProvider(String authProvider) { this.authProvider = authProvider; return this; }
        public RouteBuilder isActive(boolean isActive) { this.isActive = isActive; return this; }
        public RouteBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public RouteBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Route build() {
            return new Route(id, path, serviceName, timeoutMs, retryCount, authRequired, authProvider, isActive, createdAt, updatedAt);
        }
    }
}
