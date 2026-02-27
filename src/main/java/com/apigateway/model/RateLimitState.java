package com.apigateway.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rate_limit_state")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RateLimitState {
    @Id
    @Column(name = "key", length = 255, nullable = false)
    private String key;

    @Column(name = "limit_count")
    private Integer limitCount;

    @Column(name = "remaining_count")
    private Integer remainingCount;

    @Column(name = "reset_at")
    private LocalDateTime resetAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    // Flexible JSON field for algorithm-specific state (timestamps, tokens, etc.)
    @Lob
    @Column(name = "algo_state", columnDefinition = "TEXT")
    private String algoState;
}
