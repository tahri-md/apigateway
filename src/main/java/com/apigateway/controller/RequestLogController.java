package com.apigateway.controller;

import com.apigateway.dto.RequestLogDto;
import com.apigateway.model.RequestLog;
import com.apigateway.service.RequestLogService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/logs/requests")
public class RequestLogController {
    private final RequestLogService requestLogService;

    public RequestLogController(RequestLogService requestLogService) {
        this.requestLogService = requestLogService;
    }

    @PostMapping
    public ResponseEntity<RequestLog> createRequestLog(@Valid @RequestBody RequestLogDto dto) {
        RequestLog requestLog = new RequestLog();
        requestLog.setRequestId(dto.getRequestId());
        requestLog.setUserId(dto.getUserId());
        requestLog.setIpAddress(dto.getIpAddress());
        requestLog.setMethod(dto.getMethod());
        requestLog.setPath(dto.getPath());
        requestLog.setQueryParams(dto.getQueryParams());
        requestLog.setRoutedToService(dto.getRoutedToService());
        requestLog.setRoutedToInstance(dto.getRoutedToInstance());
        requestLog.setStatusCode(dto.getStatusCode());
        requestLog.setResponseTimeMs(dto.getResponseTimeMs());
        requestLog.setRateLimitRemaining(dto.getRateLimitRemaining());
        requestLog.setCircuitBreakerState(dto.getCircuitBreakerState());
        requestLog.setErrorMessage(dto.getErrorMessage());
        requestLog.setRequestHeaders(dto.getRequestHeaders());
        requestLog.setResponseHeaders(dto.getResponseHeaders());
        
        RequestLog created = requestLogService.createRequestLog(requestLog);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestLog> getRequestLogById(@PathVariable UUID id) {
        Optional<RequestLog> requestLog = requestLogService.getRequestLogById(id);
        return requestLog.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/request/{requestId}")
    public ResponseEntity<RequestLog> getRequestLogByRequestId(@PathVariable String requestId) {
        Optional<RequestLog> requestLog = requestLogService.getRequestLogByRequestId(requestId);
        return requestLog.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<RequestLog>> getAllRequestLogs() {
        return ResponseEntity.ok(requestLogService.getAllRequestLogs());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RequestLog> updateRequestLog(@PathVariable UUID id, @Valid @RequestBody RequestLogDto dto) {
        RequestLog requestLogDetails = new RequestLog();
        requestLogDetails.setRequestId(dto.getRequestId());
        requestLogDetails.setUserId(dto.getUserId());
        requestLogDetails.setIpAddress(dto.getIpAddress());
        requestLogDetails.setMethod(dto.getMethod());
        requestLogDetails.setPath(dto.getPath());
        requestLogDetails.setQueryParams(dto.getQueryParams());
        requestLogDetails.setRoutedToService(dto.getRoutedToService());
        requestLogDetails.setRoutedToInstance(dto.getRoutedToInstance());
        requestLogDetails.setStatusCode(dto.getStatusCode());
        requestLogDetails.setResponseTimeMs(dto.getResponseTimeMs());
        requestLogDetails.setRateLimitRemaining(dto.getRateLimitRemaining());
        requestLogDetails.setCircuitBreakerState(dto.getCircuitBreakerState());
        requestLogDetails.setErrorMessage(dto.getErrorMessage());
        requestLogDetails.setRequestHeaders(dto.getRequestHeaders());
        requestLogDetails.setResponseHeaders(dto.getResponseHeaders());
        
        RequestLog updated = requestLogService.updateRequestLog(id, requestLogDetails);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequestLog(@PathVariable UUID id) {
        requestLogService.deleteRequestLog(id);
        return ResponseEntity.noContent().build();
    }
}
