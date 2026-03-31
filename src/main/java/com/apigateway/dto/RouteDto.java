package com.apigateway.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class RouteDto {
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

    public RouteDto() {}

    public RouteDto(UUID id, String path, String serviceName, int timeoutMs, int retryCount,
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

    public static RouteDtoBuilder builder() { return new RouteDtoBuilder(); }

    public static class RouteDtoBuilder {
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

        public RouteDtoBuilder id(UUID id) { this.id = id; return this; }
        public RouteDtoBuilder path(String path) { this.path = path; return this; }
        public RouteDtoBuilder serviceName(String serviceName) { this.serviceName = serviceName; return this; }
        public RouteDtoBuilder timeoutMs(int timeoutMs) { this.timeoutMs = timeoutMs; return this; }
        public RouteDtoBuilder retryCount(int retryCount) { this.retryCount = retryCount; return this; }
        public RouteDtoBuilder authRequired(boolean authRequired) { this.authRequired = authRequired; return this; }
        public RouteDtoBuilder authProvider(String authProvider) { this.authProvider = authProvider; return this; }
        public RouteDtoBuilder isActive(boolean isActive) { this.isActive = isActive; return this; }
        public RouteDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public RouteDtoBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public RouteDto build() {
            return new RouteDto(id, path, serviceName, timeoutMs, retryCount, authRequired, authProvider, isActive, createdAt, updatedAt);
        }
    }
}
