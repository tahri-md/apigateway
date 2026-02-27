package com.apigateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RateLimitPolicyDto {
    private UUID id;
    private UUID routeId;
    private int limitCount;
    private int windowDurationSeconds;
    private String perType;
    private LocalDateTime createdAt;
}
