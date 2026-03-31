package com.apigateway.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class RouteTargetDto {
    private UUID id;
    private UUID routeId;
    private String instanceUrl;
    private int weight;
    private String version;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RouteTargetDto() {}

    public RouteTargetDto(UUID id, UUID routeId, String instanceUrl, int weight, String version,
                         boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.routeId = routeId;
        this.instanceUrl = instanceUrl;
        this.weight = weight;
        this.version = version;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public UUID getRouteId() { return routeId; }
    public String getInstanceUrl() { return instanceUrl; }
    public int getWeight() { return weight; }
    public String getVersion() { return version; }
    public boolean isActive() { return isActive; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setId(UUID id) { this.id = id; }
    public void setRouteId(UUID routeId) { this.routeId = routeId; }
    public void setInstanceUrl(String instanceUrl) { this.instanceUrl = instanceUrl; }
    public void setWeight(int weight) { this.weight = weight; }
    public void setVersion(String version) { this.version = version; }
    public void setIsActive(boolean isActive) { this.isActive = isActive; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static RouteTargetDtoBuilder builder() { return new RouteTargetDtoBuilder(); }

    public static class RouteTargetDtoBuilder {
        private UUID id;
        private UUID routeId;
        private String instanceUrl;
        private int weight;
        private String version;
        private boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public RouteTargetDtoBuilder id(UUID id) { this.id = id; return this; }
        public RouteTargetDtoBuilder routeId(UUID routeId) { this.routeId = routeId; return this; }
        public RouteTargetDtoBuilder instanceUrl(String instanceUrl) { this.instanceUrl = instanceUrl; return this; }
        public RouteTargetDtoBuilder weight(int weight) { this.weight = weight; return this; }
        public RouteTargetDtoBuilder version(String version) { this.version = version; return this; }
        public RouteTargetDtoBuilder isActive(boolean isActive) { this.isActive = isActive; return this; }
        public RouteTargetDtoBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public RouteTargetDtoBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public RouteTargetDto build() {
            return new RouteTargetDto(id, routeId, instanceUrl, weight, version, isActive, createdAt, updatedAt);
        }
    }
}
