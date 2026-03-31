package com.apigateway.service;

import com.apigateway.dto.RateLimitStateDto;
import com.apigateway.model.RateLimitState;
import com.apigateway.repository.RateLimitStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RateLimitStateService {
    private final RateLimitStateRepository rateLimitStateRepository;

    @Autowired
    public RateLimitStateService(RateLimitStateRepository rateLimitStateRepository) {
        this.rateLimitStateRepository = rateLimitStateRepository;
    }

    public RateLimitStateDto createOrUpdateState(RateLimitStateDto dto) {
        RateLimitState state = toEntity(dto);
        RateLimitState saved = rateLimitStateRepository.save(state);
        return toDto(saved);
    }

    public Optional<RateLimitStateDto> getStateByKey(String key) {
        return rateLimitStateRepository.findById(key).map(this::toDto);
    }

    public List<RateLimitStateDto> getAllStates() {
        return rateLimitStateRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public void deleteState(String key) {
        rateLimitStateRepository.deleteById(key);
    }

    public void resetState(String key) {
        rateLimitStateRepository.findById(key).ifPresent(state -> {
            state.setRemainingCount(state.getLimitCount());
            rateLimitStateRepository.save(state);
        });
    }

    private RateLimitStateDto toDto(RateLimitState state) {
        return RateLimitStateDto.builder()
                .key(state.getKey())
                .limitCount(state.getLimitCount())
                .remainingCount(state.getRemainingCount())
                .resetAt(state.getResetAt())
                .createdAt(state.getCreatedAt())
                .lastUpdated(state.getLastUpdated())
                .build();
    }

    private RateLimitState toEntity(RateLimitStateDto dto) {
        return RateLimitState.builder()
                .key(dto.getKey())
                .limitCount(dto.getLimitCount())
                .remainingCount(dto.getRemainingCount())
                .resetAt(dto.getResetAt())
                .createdAt(dto.getCreatedAt())
                .lastUpdated(dto.getLastUpdated())
                .build();
    }
}

    public RateLimitStateDto createOrUpdateState(RateLimitStateDto dto) {
        RateLimitState state = toEntity(dto);
        RateLimitState saved = rateLimitStateRepository.save(state);
        return toDto(saved);
    }

    public Optional<RateLimitStateDto> getStateByKey(String key) {
        return rateLimitStateRepository.findById(key).map(this::toDto);
    }

    public List<RateLimitStateDto> getAllStates() {
        return rateLimitStateRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public void deleteState(String key) {
        rateLimitStateRepository.deleteById(key);
    }

    public void resetState(String key) {
        rateLimitStateRepository.findById(key).ifPresent(state -> {
            state.setRemainingCount(state.getLimitCount());
            rateLimitStateRepository.save(state);
        });
    }

    private RateLimitStateDto toDto(RateLimitState state) {
        return RateLimitStateDto.builder()
                .key(state.getKey())
                .limitCount(state.getLimitCount())
                .remainingCount(state.getRemainingCount())
                .resetAt(state.getResetAt())
                .createdAt(state.getCreatedAt())
                .lastUpdated(state.getLastUpdated())
                .build();
    }

    private RateLimitState toEntity(RateLimitStateDto dto) {
        return RateLimitState.builder()
                .key(dto.getKey())
                .limitCount(dto.getLimitCount())
                .remainingCount(dto.getRemainingCount())
                .resetAt(dto.getResetAt())
                .createdAt(dto.getCreatedAt())
                .lastUpdated(dto.getLastUpdated())
                .build();
    }
}
