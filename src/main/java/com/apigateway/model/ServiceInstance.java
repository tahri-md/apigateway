package com.apigateway.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "service_instances", indexes = {
    @Index(name = "idx_service_name", columnList = "service_name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @Builder.Default
    private int weight = 1;

    @Column(name = "last_heartbeat")
    private LocalDateTime lastHeartbeat;

    @Column(name = "metadata", columnDefinition = "TEXT")
    @Convert(converter = MetadataConverter.class)
    private Map<String, Object> metadata;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated = LocalDateTime.now();
}
