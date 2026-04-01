package com.apigateway.service;

import com.apigateway.dto.RouteTargetDto;
import com.apigateway.model.Route;
import com.apigateway.model.RouteTarget;
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
public class RouteTargetService {
    private final RouteTargetRepository routeTargetRepository;
    private final RouteRepository routeRepository;

    public RouteTargetDto createRouteTarget(RouteTargetDto dto) {
        Route route = routeRepository.findById(dto.getRouteId())
                .orElseThrow(() -> new EntityNotFoundException("Route not found with id: " + dto.getRouteId()));
        
        RouteTarget target = RouteTarget.builder()
                .id(UUID.randomUUID())
                .route(route)
                .instanceUrl(dto.getInstanceUrl())
                .weight(dto.getWeight())
                .version(dto.getVersion())
                .isActive(dto.isActive())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        RouteTarget saved = routeTargetRepository.save(target);
        return toDto(saved);
    }

    public RouteTargetDto getRouteTarget(UUID id) {
        RouteTarget target = routeTargetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Route target not found with id: " + id));
        return toDto(target);
    }

    public Page<RouteTargetDto> getAllRouteTargets(Pageable pageable) {
        Page<RouteTarget> targets = routeTargetRepository.findAll(pageable);
        List<RouteTargetDto> dtos = targets.getContent().stream().map(this::toDto).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, targets.getTotalElements());
    }

    public List<RouteTargetDto> getTargetsByRoute(UUID routeId) {
        return routeTargetRepository.findByRouteId(routeId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<RouteTargetDto> getActiveTargetsByRoute(UUID routeId) {
        return routeTargetRepository.findByRouteIdAndIsActive(routeId, true).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<RouteTargetDto> getActiveTargets() {
        return routeTargetRepository.findByIsActive(true).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<RouteTargetDto> findByInstanceUrl(String url) {
        return routeTargetRepository.findByInstanceUrl(url).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public RouteTargetDto updateRouteTarget(UUID id, RouteTargetDto dto) {
        RouteTarget target = routeTargetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Route target not found with id: " + id));
        
        if (dto.getRouteId() != null && !dto.getRouteId().equals(target.getRoute().getId())) {
            Route route = routeRepository.findById(dto.getRouteId())
                    .orElseThrow(() -> new EntityNotFoundException("Route not found with id: " + dto.getRouteId()));
            target.setRoute(route);
        }
        
        target.setInstanceUrl(dto.getInstanceUrl());
        target.setWeight(dto.getWeight());
        target.setVersion(dto.getVersion());
        target.setActive(dto.isActive());
        target.setUpdatedAt(LocalDateTime.now());
        
        RouteTarget updated = routeTargetRepository.save(target);
        return toDto(updated);
    }

    public RouteTargetDto updateWeight(UUID id, int weight) {
        RouteTarget target = routeTargetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Route target not found with id: " + id));
        target.setWeight(weight);
        target.setUpdatedAt(LocalDateTime.now());
        RouteTarget updated = routeTargetRepository.save(target);
        return toDto(updated);
    }

    public RouteTargetDto activateTarget(UUID id) {
        RouteTarget target = routeTargetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Route target not found with id: " + id));
        target.setActive(true);
        target.setUpdatedAt(LocalDateTime.now());
        RouteTarget updated = routeTargetRepository.save(target);
        return toDto(updated);
    }

    public RouteTargetDto deactivateTarget(UUID id) {
        RouteTarget target = routeTargetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Route target not found with id: " + id));
        target.setActive(false);
        target.setUpdatedAt(LocalDateTime.now());
        RouteTarget updated = routeTargetRepository.save(target);
        return toDto(updated);
    }

    public void deleteRouteTarget(UUID id) {
        if (!routeTargetRepository.existsById(id)) {
            throw new EntityNotFoundException("Route target not found with id: " + id);
        }
        routeTargetRepository.deleteById(id);
    }

    public void deleteTargetsByRoute(UUID routeId) {
        routeTargetRepository.deleteByRouteId(routeId);
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
}
