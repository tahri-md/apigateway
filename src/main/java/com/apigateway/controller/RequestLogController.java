package com.apigateway.controller;

import com.apigateway.model.RequestLog;
import com.apigateway.service.RequestLogService;
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
    public ResponseEntity<RequestLog> createRequestLog(@RequestBody RequestLog requestLog) {
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
    public ResponseEntity<RequestLog> updateRequestLog(@PathVariable UUID id, @RequestBody RequestLog requestLogDetails) {
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
