package com.apigateway.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    
    @NotNull(message = "Route ID is required")
    private UUID routeId;
    
    @Min(value = 1, message = "Limit count must be at least 1")
    private int limitCount;
    
    @Min(value = 1, message = "Window duration must be at least 1 second")
    private int windowDurationSeconds;
    
    @NotBlank(message = "Per type is required")
    private String perType;
    
    @NotBlank(message = "Algorithm type is required")
    private String algorithmType;
    
    private LocalDateTime createdAt;
}
