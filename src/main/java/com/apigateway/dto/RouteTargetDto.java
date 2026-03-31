package com.apigateway.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
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
public class RouteTargetDto {
    private UUID id;
    
    @NotNull(message = "Route ID is required")
    private UUID routeId;
    
    @NotBlank(message = "Instance URL is required")
    private String instanceUrl;
    
    @Min(value = 1, message = "Weight must be at least 1")
    private int weight;
    
    private String version;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
