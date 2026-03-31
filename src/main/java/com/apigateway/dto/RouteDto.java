package com.apigateway.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
public class RouteDto {
    private UUID id;
    
    @NotBlank(message = "Path is required")
    private String path;
    
    @NotBlank(message = "Service name is required")
    private String serviceName;
    
    @Min(value = 100, message = "Timeout must be at least 100ms")
    private int timeoutMs;
    
    @Min(value = 0, message = "Retry count must be non-negative")
    private int retryCount;
    
    private boolean authRequired;
    private String authProvider;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
