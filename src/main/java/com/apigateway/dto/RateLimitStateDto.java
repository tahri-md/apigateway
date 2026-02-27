package com.apigateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RateLimitStateDto {
    private String key;
    private int limitCount;
    private int remainingCount;
    private LocalDateTime resetAt;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdated;
}
