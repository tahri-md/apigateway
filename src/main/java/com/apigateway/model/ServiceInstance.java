package com.apigateway.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "service_instances", indexes = {
    @Index(name = "idx_service_name", columnList = "service_name")
})
public class ServiceInstance {
    @Id
    @Column(name = "id", length = 255)
    private String id;

    @Column(name = "service_name", length = 100, nullable = false)
    private String serviceName;

    @Column(name = "host", length = 255, nullable = false)
    private String host;

    @Column(name = "port", nullable = false)
    private Integer port;

    @Column(name = "health_status", length = 50, nullable = false)
    private String healthStatus; // UP, DOWN

    @Column(name = "weight", nullable = false)
    private int weight = 1;

    @Column(name = "last_heartbeat")
    private LocalDateTime lastHeartbeat;

    @Column(name = "metadata", columnDefinition = "jsonb")
    @Convert(converter = MetadataConverter.class)
    private Map<String, Object> metadata;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated = LocalDateTime.now();

    // No-args constructor required by JPA/Hibernate
    public ServiceInstance() {}

    // All-args constructor
    public ServiceInstance(String id, String serviceName, String host, Integer port, String healthStatus,
                          int weight, LocalDateTime lastHeartbeat, Map<String, Object> metadata,
                          LocalDateTime createdAt, LocalDateTime lastUpdated) {
        this.id = id;
        this.serviceName = serviceName;
        this.host = host;
        this.port = port;
        this.healthStatus = healthStatus;
        this.weight = weight;
        this.lastHeartbeat = lastHeartbeat;
        this.metadata = metadata;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
    }

    // Getters
    public String getId() { return id; }
    public String getServiceName() { return serviceName; }
    public String getHost() { return host; }
    public Integer getPort() { return port; }
    public String getHealthStatus() { return healthStatus; }
    public int getWeight() { return weight; }
    public LocalDateTime getLastHeartbeat() { return lastHeartbeat; }
    public Map<String, Object> getMetadata() { return metadata; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public void setHost(String host) { this.host = host; }
    public void setPort(Integer port) { this.port = port; }
    public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }
    public void setWeight(int weight) { this.weight = weight; }
    public void setLastHeartbeat(LocalDateTime lastHeartbeat) { this.lastHeartbeat = lastHeartbeat; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }

    // Builder
    public static ServiceInstanceBuilder builder() { return new ServiceInstanceBuilder(); }

    public static class ServiceInstanceBuilder {
        private String id;
        private String serviceName;
        private String host;
        private Integer port;
        private String healthStatus;
        private int weight = 1;
        private LocalDateTime lastHeartbeat;
        private Map<String, Object> metadata;
        private LocalDateTime createdAt;
        private LocalDateTime lastUpdated;

        public ServiceInstanceBuilder id(String id) { this.id = id; return this; }
        public ServiceInstanceBuilder serviceName(String serviceName) { this.serviceName = serviceName; return this; }
        public ServiceInstanceBuilder host(String host) { this.host = host; return this; }
        public ServiceInstanceBuilder port(Integer port) { this.port = port; return this; }
        public ServiceInstanceBuilder healthStatus(String healthStatus) { this.healthStatus = healthStatus; return this; }
        public ServiceInstanceBuilder weight(int weight) { this.weight = weight; return this; }
        public ServiceInstanceBuilder lastHeartbeat(LocalDateTime lastHeartbeat) { this.lastHeartbeat = lastHeartbeat; return this; }
        public ServiceInstanceBuilder metadata(Map<String, Object> metadata) { this.metadata = metadata; return this; }
        public ServiceInstanceBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public ServiceInstanceBuilder lastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; return this; }

        public ServiceInstance build() {
            return new ServiceInstance(id, serviceName, host, port, healthStatus, weight, lastHeartbeat, metadata, createdAt, lastUpdated);
        }
    }
}
