package com.apigateway.service;

import com.apigateway.dto.RateLimitPolicyDto;
import com.apigateway.model.RateLimitPolicy;
import com.apigateway.model.RateLimitState;
import com.apigateway.ratelimit.RateLimiterAlgorithm;
import com.apigateway.repository.RateLimitPolicyRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RateLimitPolicyService {
    private final RateLimitPolicyRepository rateLimitPolicyRepository;
    private final List<RateLimiterAlgorithm> algorithms;

    public RateLimitPolicyDto createPolicy(RateLimitPolicyDto dto) {
        RateLimitPolicy policy = toEntity(dto);
        RateLimitPolicy saved = rateLimitPolicyRepository.save(policy);
        return toDto(saved);
    }

    public Optional<RateLimitPolicyDto> getPolicyById(UUID id) {
        return rateLimitPolicyRepository.findById(id).map(this::toDto);
    }

    public List<RateLimitPolicyDto> getPoliciesByRouteId(UUID routeId) {
        return rateLimitPolicyRepository.findAll().stream()
                .filter(p -> p.getRouteId() != null && p.getRouteId().equals(routeId))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<RateLimitPolicyDto> getAllPolicies() {
        return rateLimitPolicyRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public RateLimitPolicyDto updatePolicy(RateLimitPolicyDto dto) {
        RateLimitPolicy policy = toEntity(dto);
        RateLimitPolicy updated = rateLimitPolicyRepository.save(policy);
        return toDto(updated);
    }

    public void deletePolicy(UUID id) {
        rateLimitPolicyRepository.deleteById(id);
    }


    public boolean isRequestAllowed(String key, RateLimitPolicy policy, RateLimitState state) {
        RateLimiterAlgorithm algo = algorithms.stream()
            .filter(a -> a.getType().equals(policy.getAlgorithmType().name()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unknown algorithm type: " + policy.getAlgorithmType()));
        try {
            return algo.isRequestAllowed(key, policy, state);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return false;
    }


    private RateLimitPolicyDto toDto(RateLimitPolicy policy) {
        return RateLimitPolicyDto.builder()
                .id(policy.getId())
                .routeId(policy.getRouteId())
                .limitCount(policy.getLimitCount())
                .windowDurationSeconds(policy.getWindowDurationSeconds())
                .perType(policy.getPerType())
                .createdAt(policy.getCreatedAt())
                .build();
    }

    private RateLimitPolicy toEntity(RateLimitPolicyDto dto) {
        return RateLimitPolicy.builder()
                .id(dto.getId())
                .routeId(dto.getRouteId())
                .limitCount(dto.getLimitCount())
                .windowDurationSeconds(dto.getWindowDurationSeconds())
                .perType(dto.getPerType())
                .createdAt(dto.getCreatedAt())
                .build();
    }
}
