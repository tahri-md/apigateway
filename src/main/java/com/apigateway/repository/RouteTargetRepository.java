package com.apigateway.repository;

import com.apigateway.model.RouteTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface RouteTargetRepository extends JpaRepository<RouteTarget, UUID> {
    List<RouteTarget> findByRouteId(UUID routeId);
    List<RouteTarget> findByInstanceUrl(String instanceUrl);
    List<RouteTarget> findByRouteIdAndIsActive(UUID routeId, boolean isActive);
    List<RouteTarget> findByIsActive(boolean isActive);
    void deleteByRouteId(UUID routeId);
}
