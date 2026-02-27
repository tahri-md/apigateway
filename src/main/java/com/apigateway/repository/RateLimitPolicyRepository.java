package com.apigateway.repository;

import com.apigateway.model.RateLimitPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface RateLimitPolicyRepository extends JpaRepository<RateLimitPolicy, UUID> {
}
