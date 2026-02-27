package com.apigateway.repository;

import com.apigateway.model.RouteTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface RouteTargetRepository extends JpaRepository<RouteTarget, UUID> {
}
