package com.apigateway.service;

import com.apigateway.model.AuditLog;
import com.apigateway.repository.AuditLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuditLogServiceTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @InjectMocks
    private AuditLogService auditLogService;

    private AuditLog testAuditLog;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testAuditLog = new AuditLog();
        testAuditLog.setAction("CREATE");
        testAuditLog.setEntityType("Service");
        testAuditLog.setEntityId("service-001");
        testAuditLog.setChangedBy("admin");
        testAuditLog.setChangedAt(LocalDateTime.now());
    }

    @Test
    void testCreateAuditLog() {
        when(auditLogRepository.save(any(AuditLog.class))).thenReturn(testAuditLog);
        
        AuditLog created = auditLogService.createAuditLog(testAuditLog);
        
        assertNotNull(created);
        assertEquals("CREATE", created.getAction());
        verify(auditLogRepository, times(1)).save(any(AuditLog.class));
    }

    @Test
    void testGetAuditLogById() {
        UUID id = UUID.randomUUID();
        testAuditLog.setId(id);
        when(auditLogRepository.findById(id)).thenReturn(Optional.of(testAuditLog));
        
        Optional<AuditLog> found = auditLogService.getAuditLogById(id);
        
        assertTrue(found.isPresent());
        assertEquals("CREATE", found.get().getAction());
        verify(auditLogRepository, times(1)).findById(id);
    }

    @Test
    void testGetAllAuditLogs() {
        List<AuditLog> logs = Arrays.asList(testAuditLog);
        when(auditLogRepository.findAll()).thenReturn(logs);
        
        List<AuditLog> found = auditLogService.getAllAuditLogs();
        
        assertEquals(1, found.size());
        assertEquals("CREATE", found.get(0).getAction());
        verify(auditLogRepository, times(1)).findAll();
    }

    @Test
    void testGetAuditLogsByEntity() {
        List<AuditLog> logs = Arrays.asList(testAuditLog);
        when(auditLogRepository.findByEntityTypeAndEntityId("Service", "service-001"))
                .thenReturn(logs);
        
        List<AuditLog> found = auditLogService.getAuditLogsByEntity("Service", "service-001");
        
        assertEquals(1, found.size());
        assertEquals("service-001", found.get(0).getEntityId());
        verify(auditLogRepository, times(1)).findByEntityTypeAndEntityId("Service", "service-001");
    }

    @Test
    void testDeleteAuditLog() {
        UUID id = UUID.randomUUID();
        
        auditLogService.deleteAuditLog(id);
        
        verify(auditLogRepository, times(1)).deleteById(id);
    }
}
