
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
@Table(name = "rate_limit_policies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RateLimitPolicy {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "route_id", nullable = false)
    private UUID routeId;

    @Column(name = "limit_count", nullable = false)
    private int limitCount;

    @Column(name = "window_duration_seconds", nullable = false)
    private int windowDurationSeconds;

    @Column(name = "per_type", nullable = false, length = 50)
    private String perType;

        @Enumerated(EnumType.STRING)
        @Column(name = "algorithm_type", nullable = false, length = 30)
        private RateLimitAlgorithmType algorithmType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
