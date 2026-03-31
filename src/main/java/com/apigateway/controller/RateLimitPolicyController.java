package com.apigateway.controller;

import com.apigateway.dto.RateLimitPolicyDto;
import com.apigateway.model.RateLimitPolicy;
import com.apigateway.service.RateLimitPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/rate-limit-policies")
public class RateLimitPolicyController {
    private final RateLimitPolicyService rateLimitPolicyService;

    @Autowired
    public RateLimitPolicyController(RateLimitPolicyService rateLimitPolicyService) {
        this.rateLimitPolicyService = rateLimitPolicyService;
    }

    @PostMapping
    public ResponseEntity<RateLimitPolicyDto> createPolicy(@RequestBody RateLimitPolicyDto dto) {
        return ResponseEntity.ok(rateLimitPolicyService.createPolicy(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RateLimitPolicyDto> getPolicy(@PathVariable UUID id) {
        Optional<RateLimitPolicyDto> policy = rateLimitPolicyService.getPolicyById(id);
        return policy.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<RateLimitPolicyDto>> getAllPolicies() {
        return ResponseEntity.ok(rateLimitPolicyService.getAllPolicies());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RateLimitPolicyDto> updatePolicy(@PathVariable UUID id, @RequestBody RateLimitPolicyDto dto) {
        dto.setId(id);
        return ResponseEntity.ok(rateLimitPolicyService.updatePolicy(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePolicy(@PathVariable UUID id) {
        rateLimitPolicyService.deletePolicy(id);
        return ResponseEntity.noContent().build();
    }
}

    @PostMapping
    public ResponseEntity<RateLimitPolicyDto> createPolicy(@RequestBody RateLimitPolicyDto dto) {
        return ResponseEntity.ok(rateLimitPolicyService.createPolicy(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RateLimitPolicyDto> getPolicy(@PathVariable UUID id) {
        Optional<RateLimitPolicyDto> policy = rateLimitPolicyService.getPolicyById(id);
        return policy.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<RateLimitPolicyDto>> getAllPolicies() {
        return ResponseEntity.ok(rateLimitPolicyService.getAllPolicies());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RateLimitPolicyDto> updatePolicy(@PathVariable UUID id, @RequestBody RateLimitPolicyDto dto) {
        dto.setId(id);
        return ResponseEntity.ok(rateLimitPolicyService.updatePolicy(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePolicy(@PathVariable UUID id) {
        rateLimitPolicyService.deletePolicy(id);
        return ResponseEntity.noContent().build();
    }
}
