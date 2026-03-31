package com.apigateway.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceInstanceDto {
    private String id;

    @NotBlank(message = "Service name is required")
    private String serviceName;

    @NotBlank(message = "Host is required")
    private String host;

    @NotNull(message = "Port is required")
    @Min(value = 1, message = "Port must be between 1 and 65535")
    private Integer port;

    @NotBlank(message = "Health status is required")
    private String healthStatus;

    @Min(value = 1, message = "Weight must be at least 1")
    private int weight;

    private LocalDateTime lastHeartbeat;
    private Map<String, Object> metadata;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdated;
}
