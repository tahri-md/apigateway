package com.apigateway.repository;

import com.apigateway.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface RouteRepository extends JpaRepository<Route, UUID> {
    List<Route> findByPath(String path);
    List<Route> findByServiceName(String serviceName);
    List<Route> findByIsActive(boolean isActive);
    List<Route> findByPathContaining(String pathPattern);
}
