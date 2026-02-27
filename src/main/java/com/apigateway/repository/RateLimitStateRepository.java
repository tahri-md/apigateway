package com.apigateway.repository;

import com.apigateway.model.RateLimitState;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RateLimitStateRepository extends JpaRepository<RateLimitState, String> {
    Optional<RateLimitState> findByKey(String key);
}
