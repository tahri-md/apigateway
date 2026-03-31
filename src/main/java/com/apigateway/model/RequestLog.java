package com.apigateway.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "request_logs", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_created_at", columnList = "created_at")
})
public class RequestLog {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "request_id", length = 255, nullable = false, unique = true)
    private String requestId;

    @Column(name = "user_id", length = 255)
    private String userId;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "method", length = 10)
    private String method;

    @Column(name = "path", length = 500)
    private String path;

    @Lob
    @Column(name = "query_params", columnDefinition = "TEXT")
    private String queryParams;

    @Column(name = "routed_to_service", length = 100)
    private String routedToService;

    @Column(name = "routed_to_instance", length = 255)
    private String routedToInstance;

    @Column(name = "status_code")
    private Integer statusCode;

    @Column(name = "response_time_ms")
    private Integer responseTimeMs;

    @Column(name = "rate_limit_remaining")
    private Integer rateLimitRemaining;

    @Column(name = "circuit_breaker_state", length = 50)
    private String circuitBreakerState;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Lob
    @Column(name = "request_headers", columnDefinition = "TEXT")
    private String requestHeaders;

    @Lob
    @Column(name = "response_headers", columnDefinition = "TEXT")
    private String responseHeaders;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // No-args constructor required by JPA/Hibernate
    public RequestLog() {}

    // Manual getters
    public UUID getId() { return id; }
    public String getRequestId() { return requestId; }
    public String getUserId() { return userId; }
    public String getIpAddress() { return ipAddress; }
    public String getMethod() { return method; }
    public String getPath() { return path; }
    public String getQueryParams() { return queryParams; }
    public String getRoutedToService() { return routedToService; }
    public String getRoutedToInstance() { return routedToInstance; }
    public Integer getStatusCode() { return statusCode; }
    public Integer getResponseTimeMs() { return responseTimeMs; }
    public Integer getRateLimitRemaining() { return rateLimitRemaining; }
    public String getCircuitBreakerState() { return circuitBreakerState; }
    public String getErrorMessage() { return errorMessage; }
    public String getRequestHeaders() { return requestHeaders; }
    public String getResponseHeaders() { return responseHeaders; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Manual setters
    public void setId(UUID id) { this.id = id; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public void setMethod(String method) { this.method = method; }
    public void setPath(String path) { this.path = path; }
    public void setQueryParams(String queryParams) { this.queryParams = queryParams; }
    public void setRoutedToService(String routedToService) { this.routedToService = routedToService; }
    public void setRoutedToInstance(String routedToInstance) { this.routedToInstance = routedToInstance; }
    public void setStatusCode(Integer statusCode) { this.statusCode = statusCode; }
    public void setResponseTimeMs(Integer responseTimeMs) { this.responseTimeMs = responseTimeMs; }
    public void setRateLimitRemaining(Integer rateLimitRemaining) { this.rateLimitRemaining = rateLimitRemaining; }
    public void setCircuitBreakerState(String circuitBreakerState) { this.circuitBreakerState = circuitBreakerState; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public void setRequestHeaders(String requestHeaders) { this.requestHeaders = requestHeaders; }
    public void setResponseHeaders(String responseHeaders) { this.responseHeaders = responseHeaders; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
