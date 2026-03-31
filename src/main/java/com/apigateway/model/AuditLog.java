package com.apigateway.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_logs", indexes = {
        @Index(name = "idx_entity", columnList = "entity_type, entity_id")
})
public class AuditLog {
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "action", length = 100, nullable = false)
    private String action;

    @Column(name = "entity_type", length = 100)
    private String entityType;

    @Column(name = "entity_id", length = 255)
    private String entityId;

    @Lob
    @Column(name = "old_value", columnDefinition = "TEXT")
    private JsonNode oldValue;

    @Lob
    @Column(name = "new_value", columnDefinition = "TEXT")
    private JsonNode newValue;

    @Column(name = "changed_by", length = 255)
    private String changedBy;

    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;

    // No-args constructor required by JPA/Hibernate
    public AuditLog() {}

    // Manual getters
    public UUID getId() { return id; }
    public String getAction() { return action; }
    public String getEntityType() { return entityType; }
    public String getEntityId() { return entityId; }
    public JsonNode getOldValue() { return oldValue; }
    public JsonNode getNewValue() { return newValue; }
    public String getChangedBy() { return changedBy; }
    public LocalDateTime getChangedAt() { return changedAt; }

    // Manual setters
    public void setId(UUID id) { this.id = id; }
    public void setAction(String action) { this.action = action; }
    public void setEntityType(String entityType) { this.entityType = entityType; }
    public void setEntityId(String entityId) { this.entityId = entityId; }
    public void setOldValue(JsonNode oldValue) { this.oldValue = oldValue; }
    public void setNewValue(JsonNode newValue) { this.newValue = newValue; }
    public void setChangedBy(String changedBy) { this.changedBy = changedBy; }
    public void setChangedAt(LocalDateTime changedAt) { this.changedAt = changedAt; }
}
