package com.apigateway.config;

import com.apigateway.model.*;
import com.apigateway.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.LocalDateTime;
import java.util.UUID;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initializeData(
            RequestLogRepository requestLogRepository,
            AuditLogRepository auditLogRepository,
            ServiceInstanceRepository serviceInstanceRepository,
            RouteRepository routeRepository,
            RateLimitPolicyRepository rateLimitPolicyRepository) {
        return args -> {
            // Initialize with test data if needed
            if (requestLogRepository.count() == 0) {
                initializeRequestLogs(requestLogRepository);
            }
            if (auditLogRepository.count() == 0) {
                initializeAuditLogs(auditLogRepository);
            }
            if (serviceInstanceRepository.count() == 0) {
                initializeServiceInstances(serviceInstanceRepository);
            }
        };
    }

    private void initializeRequestLogs(RequestLogRepository repository) {
        ObjectMapper mapper = new ObjectMapper();
        
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

    private void initializeServiceInstances(ServiceInstanceRepository repository) {
        ServiceInstance instance1 = new ServiceInstance();
        instance1.setId("user-instance-1");
        instance1.setServiceName("UserService");
        instance1.setHost("user-service.local");
        instance1.setPort(8081);
        instance1.setHealthStatus("UP");
        instance1.setWeight(1);
        instance1.setLastHeartbeat(LocalDateTime.now());
        repository.save(instance1);

        ServiceInstance instance2 = new ServiceInstance();
        instance2.setId("product-instance-1");
        instance2.setServiceName("ProductService");
        instance2.setHost("product-service.local");
        instance2.setPort(8082);
        instance2.setHealthStatus("UP");
        instance2.setWeight(1);
        instance2.setLastHeartbeat(LocalDateTime.now());
        repository.save(instance2);
    }
}
