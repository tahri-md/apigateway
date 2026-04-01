package com.apigateway.service;

import com.apigateway.dto.RouteDto;
import com.apigateway.model.Route;
import com.apigateway.repository.RouteRepository;
import com.apigateway.repository.RouteTargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteService {
    private final RouteRepository routeRepository;
    private final RouteTargetRepository routeTargetRepository;

    public RouteDto createRoute(RouteDto dto) {
        Route route = Route.builder()
                .id(UUID.randomUUID())
                .path(dto.getPath())
                .serviceName(dto.getServiceName())
                .timeoutMs(dto.getTimeoutMs())
                .retryCount(dto.getRetryCount())
                .authRequired(dto.isAuthRequired())
                .authProvider(dto.getAuthProvider())
                .isActive(dto.isActive())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Route saved = routeRepository.save(route);
        return toDto(saved);
    }

    public RouteDto getRoute(UUID id) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Route not found with id: " + id));
        return toDto(route);
    }

    public Page<RouteDto> getAllRoutes(Pageable pageable) {
        Page<Route> routes = routeRepository.findAll(pageable);
        List<RouteDto> dtos = routes.getContent().stream().map(this::toDto).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, routes.getTotalElements());
    }

    public List<RouteDto> getActiveRoutes() {
        return routeRepository.findByIsActive(true).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<RouteDto> findByPath(String path) {
        return routeRepository.findByPath(path).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<RouteDto> findByServiceName(String serviceName) {
        return routeRepository.findByServiceName(serviceName).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public RouteDto updateRoute(UUID id, RouteDto dto) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Route not found with id: " + id));
        
        route.setPath(dto.getPath());
        route.setServiceName(dto.getServiceName());
        route.setTimeoutMs(dto.getTimeoutMs());
        route.setRetryCount(dto.getRetryCount());
        route.setAuthRequired(dto.isAuthRequired());
        route.setAuthProvider(dto.getAuthProvider());
        route.setActive(dto.isActive());
        route.setUpdatedAt(LocalDateTime.now());
        
        Route updated = routeRepository.save(route);
        return toDto(updated);
    }

    public RouteDto activateRoute(UUID id) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Route not found with id: " + id));
        route.setActive(true);
        route.setUpdatedAt(LocalDateTime.now());
        Route updated = routeRepository.save(route);
        return toDto(updated);
    }

    public RouteDto deactivateRoute(UUID id) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Route not found with id: " + id));
        route.setActive(false);
        route.setUpdatedAt(LocalDateTime.now());
        Route updated = routeRepository.save(route);
        return toDto(updated);
    }

    public void deleteRoute(UUID id) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Route not found with id: " + id));
        
        // Delete all associated route targets first
        routeTargetRepository.deleteByRouteId(id);
        
        // Then delete the route
        routeRepository.deleteById(id);
    }

    private RouteDto toDto(Route route) {
        return RouteDto.builder()
                .id(route.getId())
                .path(route.getPath())
                .serviceName(route.getServiceName())
                .timeoutMs(route.getTimeoutMs())
                .retryCount(route.getRetryCount())
                .authRequired(route.isAuthRequired())
                .authProvider(route.getAuthProvider())
                .isActive(route.isActive())
                .createdAt(route.getCreatedAt())
                .updatedAt(route.getUpdatedAt())
                .build();
    }
}
