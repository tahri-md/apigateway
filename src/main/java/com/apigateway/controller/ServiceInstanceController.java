package com.apigateway.controller;

import com.apigateway.model.ServiceInstance;
import com.apigateway.service.ServiceInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service-instances")
public class ServiceInstanceController {
    private final ServiceInstanceService service;

    @Autowired
    public ServiceInstanceController(ServiceInstanceService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ServiceInstance>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/service/{serviceName}")
    public ResponseEntity<List<ServiceInstance>> getByServiceName(@PathVariable String serviceName) {
        return ResponseEntity.ok(service.getByServiceName(serviceName));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceInstance> getById(@PathVariable String id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ServiceInstance> save(@RequestBody ServiceInstance instance) {
        return ResponseEntity.ok(service.save(instance));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    // --- Connection tracking ---

    @PostMapping("/{id}/connections/increment")
    public ResponseEntity<Void> incrementConnections(@PathVariable String id) {
        service.incrementConnections(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/connections/decrement")
    public ResponseEntity<Void> decrementConnections(@PathVariable String id) {
        service.decrementConnections(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/connections")
    public ResponseEntity<Integer> getActiveConnections(@PathVariable String id) {
        return ResponseEntity.ok(service.getActiveConnections(id));
    }

    // --- Sticky sessions ---

    @DeleteMapping("/sticky-session/{clientKey}")
    public ResponseEntity<Void> clearStickySession(@PathVariable String clientKey) {
        service.clearStickySession(clientKey);
        return ResponseEntity.ok().build();
    }
}

    @GetMapping
    public ResponseEntity<List<ServiceInstance>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/service/{serviceName}")
    public ResponseEntity<List<ServiceInstance>> getByServiceName(@PathVariable String serviceName) {
        return ResponseEntity.ok(service.getByServiceName(serviceName));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceInstance> getById(@PathVariable String id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ServiceInstance> save(@RequestBody ServiceInstance instance) {
        return ResponseEntity.ok(service.save(instance));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    // --- Connection tracking ---

    @PostMapping("/{id}/connections/increment")
    public ResponseEntity<Void> incrementConnections(@PathVariable String id) {
        service.incrementConnections(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/connections/decrement")
    public ResponseEntity<Void> decrementConnections(@PathVariable String id) {
        service.decrementConnections(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/connections")
    public ResponseEntity<Integer> getActiveConnections(@PathVariable String id) {
        return ResponseEntity.ok(service.getActiveConnections(id));
    }

    // --- Sticky sessions ---

    @DeleteMapping("/sticky-session/{clientKey}")
    public ResponseEntity<Void> clearStickySession(@PathVariable String clientKey) {
        service.clearStickySession(clientKey);
        return ResponseEntity.ok().build();
    }
}
