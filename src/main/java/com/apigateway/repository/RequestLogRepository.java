package com.apigateway.repository;

import com.apigateway.model.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface RequestLogRepository extends JpaRepository<RequestLog, UUID> {
    Optional<RequestLog> findByRequestId(String requestId);
}
