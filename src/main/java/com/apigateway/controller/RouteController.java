package com.apigateway.controller;

import com.apigateway.dto.RouteDto;
import com.apigateway.service.RouteService;
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
@RequestMapping("/api/routes")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    /**
     * Create a new route
     * 
     * Example:
     * POST /api/routes
     * {
     *   "path": "/api/orders",
     *   "serviceName": "order-service",
     *   "timeoutMs": 5000,
     *   "retryCount": 3,
     *   "authRequired": true,
     *   "authProvider": "JWT",
     *   "isActive": true
     * }
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RouteDto> createRoute(@Valid @RequestBody RouteDto routeDto) {
        RouteDto createdRoute = routeService.createRoute(routeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoute);
    }

    /**
     * Get a route by ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RouteDto> getRoute(@PathVariable UUID id) {
        RouteDto route = routeService.getRoute(id);
        return ResponseEntity.ok(route);
    }

    /**
     * Get all routes
     * 
     * Query parameters:
     * - page: Page number (default: 0)
     * - size: Page size (default: 10)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<RouteDto>> getAllRoutes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<RouteDto> routes = routeService.getAllRoutes(PageRequest.of(page, size));
        return ResponseEntity.ok(routes);
    }

    /**
     * Get all active routes
     */
    @GetMapping("/active/list")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RouteDto>> getActiveRoutes() {
        List<RouteDto> routes = routeService.getActiveRoutes();
        return ResponseEntity.ok(routes);
    }

    /**
     * Find routes by path pattern
     * 
     * Example: GET /api/routes/search?path=/api/orders
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RouteDto>> findByPath(@RequestParam String path) {
        List<RouteDto> routes = routeService.findByPath(path);
        return ResponseEntity.ok(routes);
    }

    /**
     * Find routes by service name
     * 
     * Example: GET /api/routes/by-service?serviceName=order-service
     */
    @GetMapping("/by-service")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RouteDto>> findByServiceName(@RequestParam String serviceName) {
        List<RouteDto> routes = routeService.findByServiceName(serviceName);
        return ResponseEntity.ok(routes);
    }

    /**
     * Update a route
     * 
     * Example:
     * PUT /api/routes/{id}
     * {
     *   "path": "/api/orders",
     *   "serviceName": "order-service",
     *   "timeoutMs": 5000,
     *   "retryCount": 3,
     *   "authRequired": true,
     *   "authProvider": "JWT",
     *   "isActive": true
     * }
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RouteDto> updateRoute(
            @PathVariable UUID id,
            @Valid @RequestBody RouteDto routeDto) {
        RouteDto updatedRoute = routeService.updateRoute(id, routeDto);
        return ResponseEntity.ok(updatedRoute);
    }

    /**
     * Activate a route
     * 
     * Example: PATCH /api/routes/{id}/activate
     */
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RouteDto> activateRoute(@PathVariable UUID id) {
        RouteDto activatedRoute = routeService.activateRoute(id);
        return ResponseEntity.ok(activatedRoute);
    }

    /**
     * Deactivate a route
     * 
     * Example: PATCH /api/routes/{id}/deactivate
     */
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RouteDto> deactivateRoute(@PathVariable UUID id) {
        RouteDto deactivatedRoute = routeService.deactivateRoute(id);
        return ResponseEntity.ok(deactivatedRoute);
    }

    /**
     * Delete a route
     * 
     * Note: This also deletes all associated route targets
     * 
     * Example: DELETE /api/routes/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRoute(@PathVariable UUID id) {
        routeService.deleteRoute(id);
        return ResponseEntity.noContent().build();
    }
}
