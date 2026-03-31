package com.apigateway.config;

import com.apigateway.model.*;
import com.apigateway.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class DataInitializer implements ApplicationRunner {
    
    @Autowired
    private RouteRepository routeRepository;
    
    @Autowired
    private RouteTargetRepository routeTargetRepository;
    
    @Autowired
    private RateLimitPolicyRepository rateLimitPolicyRepository;
    
    @Autowired
    private ServiceInstanceRepository serviceInstanceRepository;
    
    @Autowired
    private RateLimitStateRepository rateLimitStateRepository;
    
    @Autowired
    private AuditLogRepository auditLogRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (routeRepository.count() == 0) {
            initializeTestData();
        }
    }

    private void initializeTestData() {
        try {
            // Initialize test routes
            Route route1 = Route.builder()
                    .id(UUID.randomUUID())
                    .path("/api/users")
                    .serviceName("user-service")
                    .timeoutMs(5000)
                    .retryCount(3)
                    .authRequired(true)
                    .authProvider("JWT")
                    .isActive(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            routeRepository.save(route1);

            Route route2 = Route.builder()
                    .id(UUID.randomUUID())
                    .path("/api/orders")
                    .serviceName("order-service")
                    .timeoutMs(3000)
                    .retryCount(2)
                    .authRequired(true)
                    .authProvider("OAuth2")
                    .isActive(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            routeRepository.save(route2);

            // Initialize service instances
            ServiceInstance instance1 = ServiceInstance.builder()
                    .id("user-service-1")
                    .serviceName("user-service")
                    .host("localhost")
                    .port(8081)
                    .healthStatus("UP")
                    .weight(1)
                    .lastHeartbeat(LocalDateTime.now())
                    .createdAt(LocalDateTime.now())
                    .lastUpdated(LocalDateTime.now())
                    .build();
            serviceInstanceRepository.save(instance1);

            ServiceInstance instance2 = ServiceInstance.builder()
                    .id("order-service-1")
                    .serviceName("order-service")
                    .host("localhost")
                    .port(8082)
                    .healthStatus("UP")
                    .weight(2)
                    .lastHeartbeat(LocalDateTime.now())
                    .createdAt(LocalDateTime.now())
                    .lastUpdated(LocalDateTime.now())
                    .build();
            serviceInstanceRepository.save(instance2);

            // Initialize audit log  entry
            AuditLog auditLog = new AuditLog();
            auditLog.setId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
            auditLog.setAction("CREATE");
            auditLog.setEntityType("Route");
            auditLog.setEntityId(route1.getId().toString());
            auditLog.setChangedBy("system");
            auditLog.setChangedAt(LocalDateTime.now());
            auditLogRepository.save(auditLog);

            System.out.println("Test data initialized successfully");
        } catch (Exception e) {
            System.err.println("Error initializing test data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
