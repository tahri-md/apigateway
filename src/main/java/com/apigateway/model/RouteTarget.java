
package com.apigateway.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "route_targets")
public class RouteTarget {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @Column(name = "instance_url", nullable = false)
    private String instanceUrl;

    @Column(name = "weight")
    private int weight;

    @Column(name = "version")
    private String version;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // No-args constructor required by JPA/Hibernate
    public RouteTarget() {}

    // All-args constructor
    public RouteTarget(UUID id, Route route, String instanceUrl, int weight, String version, 
                      boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.route = route;
        this.instanceUrl = instanceUrl;
        this.weight = weight;
        this.version = version;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public UUID getId() { return id; }
    public Route getRoute() { return route; }
    public String getInstanceUrl() { return instanceUrl; }
    public int getWeight() { return weight; }
    public String getVersion() { return version; }
    public boolean isActive() { return isActive; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(UUID id) { this.id = id; }
    public void setRoute(Route route) { this.route = route; }
    public void setInstanceUrl(String instanceUrl) { this.instanceUrl = instanceUrl; }
    public void setWeight(int weight) { this.weight = weight; }
    public void setVersion(String version) { this.version = version; }
    public void setIsActive(boolean isActive) { this.isActive = isActive; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Builder
    public static RouteTargetBuilder builder() { return new RouteTargetBuilder(); }

    public static class RouteTargetBuilder {
        private UUID id;
        private Route route;
        private String instanceUrl;
        private int weight;
        private String version;
        private boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public RouteTargetBuilder id(UUID id) { this.id = id; return this; }
        public RouteTargetBuilder route(Route route) { this.route = route; return this; }
        public RouteTargetBuilder instanceUrl(String instanceUrl) { this.instanceUrl = instanceUrl; return this; }
        public RouteTargetBuilder weight(int weight) { this.weight = weight; return this; }
        public RouteTargetBuilder version(String version) { this.version = version; return this; }
        public RouteTargetBuilder isActive(boolean isActive) { this.isActive = isActive; return this; }
        public RouteTargetBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public RouteTargetBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public RouteTarget build() {
            return new RouteTarget(id, route, instanceUrl, weight, version, isActive, createdAt, updatedAt);
        }
    }
}
