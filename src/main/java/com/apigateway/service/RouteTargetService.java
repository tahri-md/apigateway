package com.apigateway.service;

import com.apigateway.dto.RouteTargetDto;
import com.apigateway.model.Route;
import com.apigateway.model.RouteTarget;
import com.apigateway.repository.RouteRepository;
import com.apigateway.repository.RouteTargetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RouteTargetService {
    @Autowired
    private RouteTargetRepository routeTargetRepository;
    @Autowired
    private RouteRepository routeRepository;

    public RouteTargetDto createRouteTarget(RouteTargetDto dto) {
        RouteTarget target = toEntity(dto);
        RouteTarget saved = routeTargetRepository.save(target);
        return toDto(saved);
    }

    public Optional<RouteTargetDto> getRouteTargetById(UUID id) {
        return routeTargetRepository.findById(id).map(this::toDto);
    }

    public List<RouteTargetDto> getAllRouteTargets() {
        return routeTargetRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<RouteTargetDto> getTargetsByRouteId(UUID routeId) {
        return routeTargetRepository.findAll().stream()
                .filter(rt -> rt.getRoute() != null && rt.getRoute().getId().equals(routeId))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public RouteTargetDto updateRouteTarget(RouteTargetDto dto) {
        RouteTarget target = toEntity(dto);
        RouteTarget updated = routeTargetRepository.save(target);
        return toDto(updated);
    }

    public void deleteRouteTarget(UUID id) {
        routeTargetRepository.deleteById(id);
    }

    private RouteTargetDto toDto(RouteTarget target) {
        RouteTargetDto dto = new RouteTargetDto();
        dto.setId(target.getId());
        dto.setRouteId(target.getRoute() != null ? target.getRoute().getId() : null);
        dto.setInstanceUrl(target.getInstanceUrl());
        dto.setWeight(target.getWeight());
        dto.setVersion(target.getVersion());
        dto.setActive(target.isActive());
        dto.setCreatedAt(target.getCreatedAt());
        dto.setUpdatedAt(target.getUpdatedAt());
        return dto;
    }

    private RouteTarget toEntity(RouteTargetDto dto) {
        Route route = null;
        if (dto.getRouteId() != null) {
            route = routeRepository.findById(dto.getRouteId()).orElse(null);
        }
        RouteTarget target = new RouteTarget();
        target.setId(dto.getId());
        target.setRoute(route);
        target.setInstanceUrl(dto.getInstanceUrl());
        target.setWeight(dto.getWeight());
        target.setVersion(dto.getVersion());
        target.setActive(dto.isActive());
        target.setCreatedAt(dto.getCreatedAt());
        target.setUpdatedAt(dto.getUpdatedAt());
        return target;
    }
}
