package com.apigateway.controller;

import com.apigateway.dto.AuditLogDto;
import com.apigateway.model.AuditLog;
import com.apigateway.service.AuditLogService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/logs/audit")
public class AuditLogController {
    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @PostMapping
    public ResponseEntity<AuditLog> createAuditLog(@Valid @RequestBody AuditLogDto dto) {
        AuditLog auditLog = new AuditLog();
        auditLog.setAction(dto.getAction());
        auditLog.setEntityType(dto.getEntityType());
        auditLog.setEntityId(dto.getEntityId());
        auditLog.setOldValue(dto.getOldValue());
        auditLog.setNewValue(dto.getNewValue());
        auditLog.setChangedBy(dto.getChangedBy());
        auditLog.setChangedAt(LocalDateTime.now());
        
        AuditLog created = auditLogService.createAuditLog(auditLog);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditLog> getAuditLogById(@PathVariable UUID id) {
        Optional<AuditLog> auditLog = auditLogService.getAuditLogById(id);
        return auditLog.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/entity/{entityType}/{entityId}")
    public ResponseEntity<List<AuditLog>> getAuditLogsByEntity(@PathVariable String entityType, @PathVariable String entityId) {
        return ResponseEntity.ok(auditLogService.getAuditLogsByEntity(entityType, entityId));
    }

    @GetMapping
    public ResponseEntity<List<AuditLog>> getAllAuditLogs() {
        return ResponseEntity.ok(auditLogService.getAllAuditLogs());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuditLog(@PathVariable UUID id) {
        auditLogService.deleteAuditLog(id);
        return ResponseEntity.noContent().build();
    }
}
