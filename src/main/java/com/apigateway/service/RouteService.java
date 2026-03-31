package com.apigateway.service;

import com.apigateway.dto.RouteDto;
import com.apigateway.model.Route;
import com.apigateway.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RouteService {
    private final RouteRepository routeRepository;

    @Autowired
    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public RouteDto createRoute(RouteDto dto) {
        Route route = toEntity(dto);
        Route saved = routeRepository.save(route);
        return toDto(saved);
    }

    public Optional<RouteDto> getRouteById(UUID id) {
        return routeRepository.findById(id).map(this::toDto);
    }

    public List<RouteDto> getAllRoutes() {
        return routeRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public RouteDto updateRoute(RouteDto dto) {
        Route route = toEntity(dto);
        Route updated = routeRepository.save(route);
        return toDto(updated);
    }

    public void deleteRoute(UUID id) {
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

    private Route toEntity(RouteDto dto) {
        return Route.builder()
                .id(dto.getId())
                .path(dto.getPath())
                .serviceName(dto.getServiceName())
                .timeoutMs(dto.getTimeoutMs())
                .retryCount(dto.getRetryCount())
                .authRequired(dto.isAuthRequired())
                .authProvider(dto.getAuthProvider())
                .isActive(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}

    public RouteDto createRoute(RouteDto dto) {
        Route route = toEntity(dto);
        Route saved = routeRepository.save(route);
        return toDto(saved);
    }

    public Optional<RouteDto> getRouteById(UUID id) {
        return routeRepository.findById(id).map(this::toDto);
    }

    public List<RouteDto> getAllRoutes() {
        return routeRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public RouteDto updateRoute(RouteDto dto) {
        Route route = toEntity(dto);
        Route updated = routeRepository.save(route);
        return toDto(updated);
    }

    public void deleteRoute(UUID id) {
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

    private Route toEntity(RouteDto dto) {
        return Route.builder()
                .id(dto.getId())
                .path(dto.getPath())
                .serviceName(dto.getServiceName())
                .timeoutMs(dto.getTimeoutMs())
                .retryCount(dto.getRetryCount())
                .authRequired(dto.isAuthRequired())
                .authProvider(dto.getAuthProvider())
                .isActive(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}
