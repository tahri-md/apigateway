package com.apigateway.controller;

import com.apigateway.dto.RouteTargetDto;
import com.apigateway.service.RouteTargetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/route-targets")
@RequiredArgsConstructor
public class RouteTargetController {

    private final RouteTargetService routeTargetService;

    /**
     * Create a new route target
     * 
     * A route target points a route to a specific backend instance with a weight for load balancing.
     * 
     * Example:
     * POST /api/route-targets
     * {
     *   "routeId": "route-uuid-1",
     *   "instanceUrl": "http://order-service-1:8081",
     *   "weight": 50,
     *   "version": "1.0.0",
     *   "isActive": true
     * }
     * 
     * Weight Example (3 targets for same route):
     * - Target 1: weight=50 → Gets 50% of traffic
     * - Target 2: weight=30 → Gets 30% of traffic
     * - Target 3: weight=20 → Gets 20% of traffic
     * Total = 100%
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RouteTargetDto> createRouteTarget(@Valid @RequestBody RouteTargetDto routeTargetDto) {
        RouteTargetDto createdTarget = routeTargetService.createRouteTarget(routeTargetDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTarget);
    }

    /**
     * Get a route target by ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RouteTargetDto> getRouteTarget(@PathVariable UUID id) {
        RouteTargetDto target = routeTargetService.getRouteTarget(id);
        return ResponseEntity.ok(target);
    }

    /**
     * Get all route targets
     * 
     * Query parameters:
     * - page: Page number (default: 0)
     * - size: Page size (default: 10)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<RouteTargetDto>> getAllRouteTargets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<RouteTargetDto> targets = routeTargetService.getAllRouteTargets(PageRequest.of(page, size));
        return ResponseEntity.ok(targets);
    }

    /**
     * Get all targets for a specific route
     * 
     * Example: GET /api/route-targets/by-route/route-uuid-1
     */
    @GetMapping("/by-route/{routeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RouteTargetDto>> getTargetsByRoute(@PathVariable UUID routeId) {
        List<RouteTargetDto> targets = routeTargetService.getTargetsByRoute(routeId);
        return ResponseEntity.ok(targets);
    }

    /**
     * Get all active targets for a specific route
     * 
     * Example: GET /api/route-targets/active/by-route/route-uuid-1
     */
    @GetMapping("/active/by-route/{routeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RouteTargetDto>> getActiveTargetsByRoute(@PathVariable UUID routeId) {
        List<RouteTargetDto> targets = routeTargetService.getActiveTargetsByRoute(routeId);
        return ResponseEntity.ok(targets);
    }

    /**
     * Get all active targets
     */
    @GetMapping("/active/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RouteTargetDto>> getActiveTargets() {
        List<RouteTargetDto> targets = routeTargetService.getActiveTargets();
        return ResponseEntity.ok(targets);
    }

    /**
     * Find targets by instance URL
     * 
     * Example: GET /api/route-targets/search?url=http://order-service-1:8081
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RouteTargetDto>> findByInstanceUrl(@RequestParam String url) {
        List<RouteTargetDto> targets = routeTargetService.findByInstanceUrl(url);
        return ResponseEntity.ok(targets);
    }

    /**
     * Update a route target
     * 
     * Example:
     * PUT /api/route-targets/{id}
     * {
     *   "routeId": "route-uuid-1",
     *   "instanceUrl": "http://order-service-1:8081",
     *   "weight": 75,
     *   "version": "1.0.1",
     *   "isActive": true
     * }
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RouteTargetDto> updateRouteTarget(
            @PathVariable UUID id,
            @Valid @RequestBody RouteTargetDto routeTargetDto) {
        RouteTargetDto updatedTarget = routeTargetService.updateRouteTarget(id, routeTargetDto);
        return ResponseEntity.ok(updatedTarget);
    }

    /**
     * Update weight of a route target (for load balancing adjustment)
     * 
     * Example: PATCH /api/route-targets/{id}/weight?weight=75
     */
    @PatchMapping("/{id}/weight")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RouteTargetDto> updateWeight(
            @PathVariable UUID id,
            @RequestParam int weight) {
        RouteTargetDto updatedTarget = routeTargetService.updateWeight(id, weight);
        return ResponseEntity.ok(updatedTarget);
    }

    /**
     * Activate a route target
     * 
     * Example: PATCH /api/route-targets/{id}/activate
     */
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RouteTargetDto> activateTarget(@PathVariable UUID id) {
        RouteTargetDto activatedTarget = routeTargetService.activateTarget(id);
        return ResponseEntity.ok(activatedTarget);
    }

    /**
     * Deactivate a route target
     * 
     * Example: PATCH /api/route-targets/{id}/deactivate
     */
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RouteTargetDto> deactivateTarget(@PathVariable UUID id) {
        RouteTargetDto deactivatedTarget = routeTargetService.deactivateTarget(id);
        return ResponseEntity.ok(deactivatedTarget);
    }

    /**
     * Delete a route target
     * 
     * Example: DELETE /api/route-targets/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRouteTarget(@PathVariable UUID id) {
        routeTargetService.deleteRouteTarget(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete all targets for a specific route
     * 
     * Example: DELETE /api/route-targets/by-route/route-uuid-1
     */
    @DeleteMapping("/by-route/{routeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTargetsByRoute(@PathVariable UUID routeId) {
        routeTargetService.deleteTargetsByRoute(routeId);
        return ResponseEntity.noContent().build();
    }
}
