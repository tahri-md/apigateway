package com.apigateway.dto;

import jakarta.validation.constraints.NotBlank;
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
public class RequestLogDto {
    private UUID id;

    @NotBlank(message = "Request ID is required")
    private String requestId;

    private String userId;
    private String ipAddress;

    @NotBlank(message = "HTTP method is required")
    private String method;

    @NotBlank(message = "Path is required")
    private String path;

    private String queryParams;
    private String routedToService;
    private String routedToInstance;

    @Min(value = 100, message = "Status code must be at least 100")
    private Integer statusCode;

    @Min(value = 0, message = "Response time must be non-negative")
    private Integer responseTimeMs;

    private Integer rateLimitRemaining;
    private String circuitBreakerState;
    private String errorMessage;
    private String requestHeaders;
    private String responseHeaders;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
