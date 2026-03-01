package com.apigateway.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "circuit_breaker_state", uniqueConstraints = {
    @UniqueConstraint(columnNames = "service_name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
