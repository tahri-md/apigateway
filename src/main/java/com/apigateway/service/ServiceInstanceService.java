package com.apigateway.service;

import com.apigateway.model.ServiceInstance;
import com.apigateway.repository.ServiceInstanceRepository;
import com.apigateway.model.CircuitBreakerState;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class ServiceInstanceService {
    private final ServiceInstanceRepository repository;
    private final RestTemplate restTemplate;
    private final CircuitBreakerService circuitBreakerService;

    private final ConcurrentHashMap<String, Integer> rrIndexes = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicInteger> activeConnections = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> stickySessionMap = new ConcurrentHashMap<>();

    public enum LoadBalancingStrategy {
        RANDOM, ROUND_ROBIN, LEAST_CONNECTIONS, STICKY_SESSION
    }

    public List<ServiceInstance> getAll() {
        return repository.findAll();
    }

    public List<ServiceInstance> getByServiceName(String serviceName) {
        return repository.findByServiceName(serviceName);
    }

    public Optional<ServiceInstance> getById(String id) {
        return repository.findById(id);
    }

    public ServiceInstance save(ServiceInstance instance) {
        return repository.save(instance);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }


    public Optional<ServiceInstance> selectHealthyInstance(
            String serviceName,
            LoadBalancingStrategy strategy,
            boolean useWeight,
            String clientKey) {

        // Filter: instance must be UP and its circuit breaker must not be OPEN
        List<ServiceInstance> healthy = getByServiceName(serviceName).stream()
            .filter(i -> "UP".equalsIgnoreCase(i.getHealthStatus()))
            .filter(i -> {
                CircuitBreakerState cbState = circuitBreakerService.getState(i.getId());
                return !"OPEN".equalsIgnoreCase(cbState.getState());
            })
            .toList();
        if (healthy.isEmpty()) return Optional.empty();

        return switch (strategy) {
            case RANDOM -> {
                List<ServiceInstance> pool = useWeight ? buildWeightedPool(healthy) : healthy;
                yield Optional.of(pool.get((int) (Math.random() * pool.size())));
            }
            case ROUND_ROBIN -> {
                List<ServiceInstance> pool = useWeight ? buildWeightedPool(healthy) : healthy;
                int idx = rrIndexes.compute(serviceName, (k, v) -> v == null ? 0 : (v + 1) % pool.size());
                yield Optional.of(pool.get(idx));
            }
            case LEAST_CONNECTIONS -> {
                yield healthy.stream()
                    .min((a, b) -> {
                        double loadA = (double) getActiveConnections(a.getId()) / Math.max(1, a.getWeight());
                        double loadB = (double) getActiveConnections(b.getId()) / Math.max(1, b.getWeight());
                        return Double.compare(loadA, loadB);
                    });
            }
            case STICKY_SESSION -> {
                if (clientKey == null) throw new IllegalArgumentException("clientKey is required for STICKY_SESSION");
                String stickyId = stickySessionMap.get(clientKey);
                if (stickyId != null) {
                    Optional<ServiceInstance> sticky = healthy.stream()
                        .filter(i -> i.getId().equals(stickyId))
                        .findFirst();
                    if (sticky.isPresent()) yield sticky;
                }
                ServiceInstance assigned = healthy.get((int) (Math.random() * healthy.size()));
                stickySessionMap.put(clientKey, assigned.getId());
                yield Optional.of(assigned);
            }
        };
    }
    @Scheduled(fixedDelay = 5000)
    public void updateHealthStatus(String id, String healthStatus) {
        repository.findAll().stream().forEach(instance-> {
                 String url = "http://" + instance.getHost() + ":" 
                             + instance.getPort() + "/actuator/health";

                ResponseEntity<String> response =
                        restTemplate.getForEntity(url, String.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    instance.setHealthStatus("UP");
                } else {
                    instance.setHealthStatus("DOWN");  
                }
                instance.setLastHeartbeat(java.time.LocalDateTime.now());
                repository.save(instance);
        });
    }

    // Track connection lifecycle
    public void incrementConnections(String instanceId) {
        activeConnections.computeIfAbsent(instanceId, k -> new AtomicInteger(0)).incrementAndGet();
    }

    public void decrementConnections(String instanceId) {
        activeConnections.computeIfAbsent(instanceId, k -> new AtomicInteger(0)).decrementAndGet();
    }

    public int getActiveConnections(String instanceId) {
        return activeConnections.getOrDefault(instanceId, new AtomicInteger(0)).get();
    }

    public void clearStickySession(String clientKey) {
        stickySessionMap.remove(clientKey);
    }

    // Build a weighted pool: each instance appears weight times
    private List<ServiceInstance> buildWeightedPool(List<ServiceInstance> instances) {
        List<ServiceInstance> pool = new ArrayList<>();
        for (ServiceInstance i : instances) {
            pool.addAll(Collections.nCopies(Math.max(1, i.getWeight()), i));
        }
        return pool;
    }
}
