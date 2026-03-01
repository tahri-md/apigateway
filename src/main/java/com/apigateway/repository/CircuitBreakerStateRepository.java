package com.apigateway.repository;

import com.apigateway.model.CircuitBreakerState;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface CircuitBreakerStateRepository extends JpaRepository<CircuitBreakerState, UUID> {
    Optional<CircuitBreakerState> findByServiceName(String serviceName);
}
