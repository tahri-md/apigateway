package com.apigateway.repository;

import com.apigateway.model.ServiceInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServiceInstanceRepository extends JpaRepository<ServiceInstance, String> {
    List<ServiceInstance> findByServiceName(String serviceName);
}
