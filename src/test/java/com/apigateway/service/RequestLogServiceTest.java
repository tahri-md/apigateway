package com.apigateway.service;

import com.apigateway.model.RequestLog;
import com.apigateway.repository.RequestLogRepository;
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

class RequestLogServiceTest {

    @Mock
    private RequestLogRepository requestLogRepository;

    @InjectMocks
    private RequestLogService requestLogService;

    private RequestLog testRequestLog;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testRequestLog = new RequestLog();
        testRequestLog.setRequestId("test-req-001");
        testRequestLog.setUserId("testuser");
        testRequestLog.setIpAddress("192.168.1.1");
        testRequestLog.setMethod("GET");
        testRequestLog.setPath("/api/test");
        testRequestLog.setStatusCode(200);
        testRequestLog.setResponseTimeMs(100);
        testRequestLog.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testCreateRequestLog() {
        when(requestLogRepository.save(any(RequestLog.class))).thenReturn(testRequestLog);
        
        RequestLog created = requestLogService.createRequestLog(testRequestLog);
        
        assertNotNull(created);
        assertEquals("test-req-001", created.getRequestId());
        verify(requestLogRepository, times(1)).save(any(RequestLog.class));
    }

    @Test
    void testGetRequestLogById() {
        UUID id = UUID.randomUUID();
        testRequestLog.setId(id);
        when(requestLogRepository.findById(id)).thenReturn(Optional.of(testRequestLog));
        
        Optional<RequestLog> found = requestLogService.getRequestLogById(id);
        
        assertTrue(found.isPresent());
        assertEquals("test-req-001", found.get().getRequestId());
        verify(requestLogRepository, times(1)).findById(id);
    }

    @Test
    void testGetRequestLogByRequestId() {
        when(requestLogRepository.findByRequestId("test-req-001")).thenReturn(Optional.of(testRequestLog));
        
        Optional<RequestLog> found = requestLogService.getRequestLogByRequestId("test-req-001");
        
        assertTrue(found.isPresent());
        assertEquals("test-req-001", found.get().getRequestId());
        verify(requestLogRepository, times(1)).findByRequestId("test-req-001");
    }

    @Test
    void testGetAllRequestLogs() {
        List<RequestLog> logs = Arrays.asList(testRequestLog);
        when(requestLogRepository.findAll()).thenReturn(logs);
        
        List<RequestLog> found = requestLogService.getAllRequestLogs();
        
        assertEquals(1, found.size());
        assertEquals("test-req-001", found.get(0).getRequestId());
        verify(requestLogRepository, times(1)).findAll();
    }

    @Test
    void testUpdateRequestLog() {
        UUID id = UUID.randomUUID();
        testRequestLog.setId(id);
        RequestLog updates = new RequestLog();
        updates.setStatusCode(201);
        updates.setResponseTimeMs(150);
        
        when(requestLogRepository.findById(id)).thenReturn(Optional.of(testRequestLog));
        when(requestLogRepository.save(any(RequestLog.class))).thenReturn(testRequestLog);
        
        RequestLog updated = requestLogService.updateRequestLog(id, updates);
        
        assertNotNull(updated);
        verify(requestLogRepository, times(1)).findById(id);
        verify(requestLogRepository, times(1)).save(any(RequestLog.class));
    }

    @Test
    void testDeleteRequestLog() {
        UUID id = UUID.randomUUID();
        
        requestLogService.deleteRequestLog(id);
        
        verify(requestLogRepository, times(1)).deleteById(id);
    }
}
