package com.apigateway.config;

import com.apigateway.model.RequestLog;
import com.apigateway.model.AuditLog;
import com.apigateway.repository.RequestLogRepository;
import com.apigateway.repository.AuditLogRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initializeData(
            RequestLogRepository requestLogRepository,
            AuditLogRepository auditLogRepository) {
        return args -> {
            // Initialize with test data if needed
            if (requestLogRepository.count() == 0) {
                initializeRequestLogs(requestLogRepository);
            }
            if (auditLogRepository.count() == 0) {
                initializeAuditLogs(auditLogRepository);
            }
        };
    }

    private void initializeRequestLogs(RequestLogRepository repository) {
        RequestLog log1 = new RequestLog();
        log1.setRequestId("req-001");
        log1.setUserId("user1");
        log1.setIpAddress("192.168.1.100");
        log1.setMethod("GET");
        log1.setPath("/api/users");
        log1.setRoutedToService("UserService");
        log1.setRoutedToInstance("user-instance-1");
        log1.setStatusCode(200);
        log1.setResponseTimeMs(150);
        log1.setRateLimitRemaining(99);
        log1.setCircuitBreakerState("CLOSED");
        log1.setCreatedAt(LocalDateTime.now());
        repository.save(log1);

        RequestLog log2 = new RequestLog();
        log2.setRequestId("req-002");
        log2.setUserId("user2");
        log2.setIpAddress("192.168.1.101");
        log2.setMethod("POST");
        log2.setPath("/api/users");
        log2.setRoutedToService("UserService");
        log2.setRoutedToInstance("user-instance-1");
        log2.setStatusCode(201);
        log2.setResponseTimeMs(250);
        log2.setRateLimitRemaining(98);
        log2.setCircuitBreakerState("CLOSED");
        log2.setCreatedAt(LocalDateTime.now().minusMinutes(5));
        repository.save(log2);
    }

    private void initializeAuditLogs(AuditLogRepository repository) {
        AuditLog audit1 = new AuditLog();
        audit1.setAction("CREATE");
        audit1.setEntityType("Route");
        audit1.setEntityId("route-1");
        audit1.setChangedBy("admin");
        audit1.setChangedAt(LocalDateTime.now());
        repository.save(audit1);

        AuditLog audit2 = new AuditLog();
        audit2.setAction("UPDATE");
        audit2.setEntityType("RateLimitPolicy");
        audit2.setEntityId("policy-1");
        audit2.setChangedBy("admin");
        audit2.setChangedAt(LocalDateTime.now().minusHours(1));
        repository.save(audit2);
    }
}
