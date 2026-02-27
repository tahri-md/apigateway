package com.apigateway.service;

import com.apigateway.dto.RouteTargetDto;
import com.apigateway.model.Route;
import com.apigateway.model.RouteTarget;
import com.apigateway.repository.RouteRepository;
import com.apigateway.repository.RouteTargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteTargetService {
    private final RouteTargetRepository routeTargetRepository;
    private final RouteRepository routeRepository;

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
        return RouteTargetDto.builder()
                .id(target.getId())
                .routeId(target.getRoute() != null ? target.getRoute().getId() : null)
                .instanceUrl(target.getInstanceUrl())
                .weight(target.getWeight())
                .version(target.getVersion())
                .isActive(target.isActive())
                .createdAt(target.getCreatedAt())
                .updatedAt(target.getUpdatedAt())
                .build();
    }

    private RouteTarget toEntity(RouteTargetDto dto) {
        Route route = null;
        if (dto.getRouteId() != null) {
            route = routeRepository.findById(dto.getRouteId()).orElse(null);
        }
        return RouteTarget.builder()
                .id(dto.getId())
                .route(route)
                .instanceUrl(dto.getInstanceUrl())
                .weight(dto.getWeight())
                .version(dto.getVersion())
                .isActive(dto.isActive())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}
