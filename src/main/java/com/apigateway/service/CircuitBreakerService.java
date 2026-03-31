package com.apigateway.service;

import com.apigateway.model.CircuitBreakerState;
import com.apigateway.repository.CircuitBreakerStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CircuitBreakerService {
    private final CircuitBreakerStateRepository repository;

    @Autowired
    public CircuitBreakerService(CircuitBreakerStateRepository repository) {
        this.repository = repository;
    }

    // Thresholds and timings can be made configurable
    private static final int FAILURE_THRESHOLD = 5;
    private static final double FAILURE_RATE_THRESHOLD = 0.5;
    private static final long OPEN_TIMEOUT_SECONDS = 60;

    public CircuitBreakerState getState(String serviceName) {
        return repository.findByServiceName(serviceName)
                .orElseGet(() -> repository.save(CircuitBreakerState.builder()
                        .serviceName(serviceName)
                        .state("CLOSED")
                        .failureCount(0)
                        .failureRate(0.0)
                        .createdAt(LocalDateTime.now())
                        .lastUpdated(LocalDateTime.now())
                        .build()));
    }

    public boolean allowRequest(String serviceName) {
        CircuitBreakerState state = getState(serviceName);
        if ("OPEN".equals(state.getState())) {
            if (state.getOpenAt() != null &&
                LocalDateTime.now().isAfter(state.getOpenAt().plusSeconds(OPEN_TIMEOUT_SECONDS))) {
                // Move to HALF_OPEN after timeout
                state.setState("HALF_OPEN");
                state.setHalfOpenAt(LocalDateTime.now());
                state.setLastUpdated(LocalDateTime.now());
                repository.save(state);
                return true;
            }
            return false;
        }
        return true;
    }

    public void recordFailure(String serviceName) {
        CircuitBreakerState state = getState(serviceName);
        state.setFailureCount(state.getFailureCount() + 1);
        state.setLastFailureTime(LocalDateTime.now());
        // Update failure rate logic as needed
        double newRate = Math.min(1.0, state.getFailureRate() + 0.1);
        state.setFailureRate(newRate);
        if (state.getFailureCount() >= FAILURE_THRESHOLD || state.getFailureRate() >= FAILURE_RATE_THRESHOLD) {
            state.setState("OPEN");
            state.setOpenAt(LocalDateTime.now());
        }
        state.setLastUpdated(LocalDateTime.now());
        repository.save(state);
    }

    public void recordSuccess(String serviceName) {
        CircuitBreakerState state = getState(serviceName);
        if ("HALF_OPEN".equals(state.getState())) {
            state.setState("CLOSED");
            state.setFailureCount(0);
            state.setFailureRate(0.0);
            state.setLastUpdated(LocalDateTime.now());
            repository.save(state);
        }
    }

    public List<CircuitBreakerState> getAllStates() {
        return repository.findAll();
    }

    public void resetState(String serviceName) {
        CircuitBreakerState state = getState(serviceName);
        state.setState("CLOSED");
        state.setFailureCount(0);
        state.setFailureRate(0.0);
        state.setOpenAt(null);
        state.setHalfOpenAt(null);
        state.setLastFailureTime(null);
        state.setLastUpdated(LocalDateTime.now());
        repository.save(state);
    }

    public void deleteState(String serviceName) {
        Optional<CircuitBreakerState> stateOpt = repository.findByServiceName(serviceName);
        stateOpt.ifPresent(repository::delete);
    }

    public void updateThresholds(int failureThreshold, double failureRateThreshold, long openTimeoutSeconds) {
        // This method would update static thresholds globally (for demo purposes)
        // For per-service config, add fields to CircuitBreakerState and update accordingly
        // Not thread-safe, for demo only
        // FAILURE_THRESHOLD = failureThreshold; // Can't update final static
        // FAILURE_RATE_THRESHOLD = failureRateThreshold;
        // OPEN_TIMEOUT_SECONDS = openTimeoutSeconds;
    }

    public CircuitBreakerMetrics getMetrics() {
        List<CircuitBreakerState> states = getAllStates();
        long openCount = states.stream().filter(s -> "OPEN".equals(s.getState())).count();
        long closedCount = states.stream().filter(s -> "CLOSED".equals(s.getState())).count();
        long halfOpenCount = states.stream().filter(s -> "HALF_OPEN".equals(s.getState())).count();
        double avgFailureRate = states.stream().mapToDouble(CircuitBreakerState::getFailureRate).average().orElse(0.0);
        return new CircuitBreakerMetrics(openCount, closedCount, halfOpenCount, avgFailureRate);
    }

    public static class CircuitBreakerMetrics {
        public long openCount;
        public long closedCount;
        public long halfOpenCount;
        public double avgFailureRate;
        public CircuitBreakerMetrics(long openCount, long closedCount, long halfOpenCount, double avgFailureRate) {
            this.openCount = openCount;
            this.closedCount = closedCount;
            this.halfOpenCount = halfOpenCount;
            this.avgFailureRate = avgFailureRate;
        }
    }
}

    // Thresholds and timings can be made configurable
    private static final int FAILURE_THRESHOLD = 5;
    private static final double FAILURE_RATE_THRESHOLD = 0.5;
    private static final long OPEN_TIMEOUT_SECONDS = 60;

    public CircuitBreakerState getState(String serviceName) {
        return repository.findByServiceName(serviceName)
                .orElseGet(() -> repository.save(CircuitBreakerState.builder()
                        .serviceName(serviceName)
                        .state("CLOSED")
                        .failureCount(0)
                        .failureRate(0.0)
                        .createdAt(LocalDateTime.now())
                        .lastUpdated(LocalDateTime.now())
                        .build()));
    }

    public boolean allowRequest(String serviceName) {
        CircuitBreakerState state = getState(serviceName);
        if ("OPEN".equals(state.getState())) {
            if (state.getOpenAt() != null &&
                LocalDateTime.now().isAfter(state.getOpenAt().plusSeconds(OPEN_TIMEOUT_SECONDS))) {
                // Move to HALF_OPEN after timeout
                state.setState("HALF_OPEN");
                state.setHalfOpenAt(LocalDateTime.now());
                state.setLastUpdated(LocalDateTime.now());
                repository.save(state);
                return true;
            }
            return false;
        }
        return true;
    }

    public void recordFailure(String serviceName) {
        CircuitBreakerState state = getState(serviceName);
        state.setFailureCount(state.getFailureCount() + 1);
        state.setLastFailureTime(LocalDateTime.now());
        // Update failure rate logic as needed
        double newRate = Math.min(1.0, state.getFailureRate() + 0.1);
        state.setFailureRate(newRate);
        if (state.getFailureCount() >= FAILURE_THRESHOLD || state.getFailureRate() >= FAILURE_RATE_THRESHOLD) {
            state.setState("OPEN");
            state.setOpenAt(LocalDateTime.now());
        }
        state.setLastUpdated(LocalDateTime.now());
        repository.save(state);
    }

    public void recordSuccess(String serviceName) {
        CircuitBreakerState state = getState(serviceName);
        if ("HALF_OPEN".equals(state.getState())) {
            state.setState("CLOSED");
            state.setFailureCount(0);
            state.setFailureRate(0.0);
            state.setLastUpdated(LocalDateTime.now());
            repository.save(state);
        }
    }

    public List<CircuitBreakerState> getAllStates() {
        return repository.findAll();
    }

    public void resetState(String serviceName) {
        CircuitBreakerState state = getState(serviceName);
        state.setState("CLOSED");
        state.setFailureCount(0);
        state.setFailureRate(0.0);
        state.setOpenAt(null);
        state.setHalfOpenAt(null);
        state.setLastFailureTime(null);
        state.setLastUpdated(LocalDateTime.now());
        repository.save(state);
    }

    public void deleteState(String serviceName) {
        Optional<CircuitBreakerState> stateOpt = repository.findByServiceName(serviceName);
        stateOpt.ifPresent(repository::delete);
    }

    public void updateThresholds(int failureThreshold, double failureRateThreshold, long openTimeoutSeconds) {
        // This method would update static thresholds globally (for demo purposes)
        // For per-service config, add fields to CircuitBreakerState and update accordingly
        // Not thread-safe, for demo only
        // FAILURE_THRESHOLD = failureThreshold; // Can't update final static
        // FAILURE_RATE_THRESHOLD = failureRateThreshold;
        // OPEN_TIMEOUT_SECONDS = openTimeoutSeconds;
    }

    public CircuitBreakerMetrics getMetrics() {
        List<CircuitBreakerState> states = getAllStates();
        long openCount = states.stream().filter(s -> "OPEN".equals(s.getState())).count();
        long closedCount = states.stream().filter(s -> "CLOSED".equals(s.getState())).count();
        long halfOpenCount = states.stream().filter(s -> "HALF_OPEN".equals(s.getState())).count();
        double avgFailureRate = states.stream().mapToDouble(CircuitBreakerState::getFailureRate).average().orElse(0.0);
        return new CircuitBreakerMetrics(openCount, closedCount, halfOpenCount, avgFailureRate);
    }

    public static class CircuitBreakerMetrics {
        public long openCount;
        public long closedCount;
        public long halfOpenCount;
        public double avgFailureRate;
        public CircuitBreakerMetrics(long openCount, long closedCount, long halfOpenCount, double avgFailureRate) {
            this.openCount = openCount;
            this.closedCount = closedCount;
            this.halfOpenCount = halfOpenCount;
            this.avgFailureRate = avgFailureRate;
        }
    }
}
