package com.apigateway.service;

import com.apigateway.model.RequestLog;
import com.apigateway.repository.RequestLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RequestLogService {
    private final RequestLogRepository requestLogRepository;

    public RequestLogService(RequestLogRepository requestLogRepository) {
        this.requestLogRepository = requestLogRepository;
    }

    public RequestLog createRequestLog(RequestLog requestLog) {
        if (requestLog.getCreatedAt() == null) {
            requestLog.setCreatedAt(LocalDateTime.now());
        }
        return requestLogRepository.save(requestLog);
    }

    public Optional<RequestLog> getRequestLogById(UUID id) {
        return requestLogRepository.findById(id);
    }

    public Optional<RequestLog> getRequestLogByRequestId(String requestId) {
        return requestLogRepository.findByRequestId(requestId);
    }

    public List<RequestLog> getAllRequestLogs() {
        return requestLogRepository.findAll();
    }

    public RequestLog updateRequestLog(UUID id, RequestLog requestLogDetails) {
        return requestLogRepository.findById(id).map(requestLog -> {
            requestLog.setUserId(requestLogDetails.getUserId());
            requestLog.setIpAddress(requestLogDetails.getIpAddress());
            requestLog.setMethod(requestLogDetails.getMethod());
            requestLog.setPath(requestLogDetails.getPath());
            requestLog.setQueryParams(requestLogDetails.getQueryParams());
            requestLog.setRoutedToService(requestLogDetails.getRoutedToService());
            requestLog.setRoutedToInstance(requestLogDetails.getRoutedToInstance());
            requestLog.setStatusCode(requestLogDetails.getStatusCode());
            requestLog.setResponseTimeMs(requestLogDetails.getResponseTimeMs());
            requestLog.setRateLimitRemaining(requestLogDetails.getRateLimitRemaining());
            requestLog.setCircuitBreakerState(requestLogDetails.getCircuitBreakerState());
            requestLog.setErrorMessage(requestLogDetails.getErrorMessage());
            requestLog.setRequestHeaders(requestLogDetails.getRequestHeaders());
            requestLog.setResponseHeaders(requestLogDetails.getResponseHeaders());
            return requestLogRepository.save(requestLog);
        }).orElse(null);
    }

    public void deleteRequestLog(UUID id) {
        requestLogRepository.deleteById(id);
    }
}
