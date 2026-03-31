package com.apigateway.controller;

import com.apigateway.model.CircuitBreakerState;
import com.apigateway.service.CircuitBreakerService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/circuit-breaker")
public class CircuitBreakerController {
    private final CircuitBreakerService circuitBreakerService;

    @Autowired
    public CircuitBreakerController(CircuitBreakerService circuitBreakerService) {
        this.circuitBreakerService = circuitBreakerService;
    }

    @GetMapping("/state/{serviceName}")
    public ResponseEntity<CircuitBreakerState> getState(@PathVariable String serviceName) {
        return ResponseEntity.ok(circuitBreakerService.getState(serviceName));
    }

    @PostMapping("/failure/{serviceName}")
    public ResponseEntity<Void> recordFailure(@PathVariable String serviceName) {
        circuitBreakerService.recordFailure(serviceName);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/success/{serviceName}")
    public ResponseEntity<Void> recordSuccess(@PathVariable String serviceName) {
        circuitBreakerService.recordSuccess(serviceName);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/allow/{serviceName}")
    public ResponseEntity<Boolean> allowRequest(@PathVariable String serviceName) {
        return ResponseEntity.ok(circuitBreakerService.allowRequest(serviceName));
    }

    @GetMapping("/states")
    public ResponseEntity<List<CircuitBreakerState>> getAllStates() {
        return ResponseEntity.ok(circuitBreakerService.getAllStates());
    }

    @PostMapping("/reset/{serviceName}")
    public ResponseEntity<Void> resetState(@PathVariable String serviceName) {
        circuitBreakerService.resetState(serviceName);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{serviceName}")
    public ResponseEntity<Void> deleteState(@PathVariable String serviceName) {
        circuitBreakerService.deleteState(serviceName);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/thresholds")
    public ResponseEntity<Void> updateThresholds(@RequestParam int failureThreshold,
                                                 @RequestParam double failureRateThreshold,
                                                 @RequestParam long openTimeoutSeconds) {
        circuitBreakerService.updateThresholds(failureThreshold, failureRateThreshold, openTimeoutSeconds);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/metrics")
    public ResponseEntity<CircuitBreakerService.CircuitBreakerMetrics> getMetrics() {
        return ResponseEntity.ok(circuitBreakerService.getMetrics());
    }

   
}

    @GetMapping("/state/{serviceName}")
    public ResponseEntity<CircuitBreakerState> getState(@PathVariable String serviceName) {
        return ResponseEntity.ok(circuitBreakerService.getState(serviceName));
    }

    @PostMapping("/failure/{serviceName}")
    public ResponseEntity<Void> recordFailure(@PathVariable String serviceName) {
        circuitBreakerService.recordFailure(serviceName);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/success/{serviceName}")
    public ResponseEntity<Void> recordSuccess(@PathVariable String serviceName) {
        circuitBreakerService.recordSuccess(serviceName);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/allow/{serviceName}")
    public ResponseEntity<Boolean> allowRequest(@PathVariable String serviceName) {
        return ResponseEntity.ok(circuitBreakerService.allowRequest(serviceName));
    }

    @GetMapping("/states")
    public ResponseEntity<List<CircuitBreakerState>> getAllStates() {
        return ResponseEntity.ok(circuitBreakerService.getAllStates());
    }

    @PostMapping("/reset/{serviceName}")
    public ResponseEntity<Void> resetState(@PathVariable String serviceName) {
        circuitBreakerService.resetState(serviceName);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{serviceName}")
    public ResponseEntity<Void> deleteState(@PathVariable String serviceName) {
        circuitBreakerService.deleteState(serviceName);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/thresholds")
    public ResponseEntity<Void> updateThresholds(@RequestParam int failureThreshold,
                                                 @RequestParam double failureRateThreshold,
                                                 @RequestParam long openTimeoutSeconds) {
        circuitBreakerService.updateThresholds(failureThreshold, failureRateThreshold, openTimeoutSeconds);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/metrics")
    public ResponseEntity<CircuitBreakerService.CircuitBreakerMetrics> getMetrics() {
        return ResponseEntity.ok(circuitBreakerService.getMetrics());
    }

   
}
