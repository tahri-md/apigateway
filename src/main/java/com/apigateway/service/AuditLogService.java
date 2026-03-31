package com.apigateway.service;

import com.apigateway.model.AuditLog;
import com.apigateway.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public AuditLog createAuditLog(AuditLog auditLog) {
        if (auditLog.getChangedAt() == null) {
            auditLog.setChangedAt(LocalDateTime.now());
        }
        return auditLogRepository.save(auditLog);
    }

    public Optional<AuditLog> getAuditLogById(UUID id) {
        return auditLogRepository.findById(id);
    }

    public List<AuditLog> getAuditLogsByEntity(String entityType, String entityId) {
        return auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId);
    }

    public List<AuditLog> getAllAuditLogs() {
        return auditLogRepository.findAll();
    }

    public void deleteAuditLog(UUID id) {
        auditLogRepository.deleteById(id);
    }

    public void logAction(String action, String entityType, String entityId, String changedBy) {
        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setChangedBy(changedBy);
        log.setChangedAt(LocalDateTime.now());
        auditLogRepository.save(log);
    }
}
