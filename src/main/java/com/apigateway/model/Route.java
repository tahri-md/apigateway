
package com.apigateway.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "routes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
