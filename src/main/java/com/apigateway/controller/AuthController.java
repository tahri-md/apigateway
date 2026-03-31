package com.apigateway.controller;

import com.apigateway.dto.AuthRequest;
import com.apigateway.dto.AuthResponse;
import com.apigateway.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Generate JWT token for a user
     * Default roles: ADMIN, GATEWAY
     */
    @PostMapping("/token")
    public ResponseEntity<AuthResponse> generateToken(@Valid @RequestBody AuthRequest request) {
        // In production, validate username/password against a database
        // For demo purposes, we'll accept any username and issue a token
        String token = jwtUtil.generateToken(request.getUsername(), "ADMIN", "GATEWAY");
        return ResponseEntity.ok(new AuthResponse(token, "Bearer", 86400));
    }

    /**
     * Generate token with specific role
     */
    @PostMapping("/token/{role}")
    public ResponseEntity<AuthResponse> generateTokenWithRole(
            @PathVariable String role,
            @Valid @RequestBody AuthRequest request) {
        String token = jwtUtil.generateToken(request.getUsername(), role);
        return ResponseEntity.ok(new AuthResponse(token, "Bearer", 86400));
    }

    /**
     * Validate token endpoint
     */
    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(false);
        }
        
        String token = authHeader.substring(7);
        boolean isValid = jwtUtil.validateToken(token) && !jwtUtil.isTokenExpired(token);
        return ResponseEntity.ok(isValid);
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Auth service is running");
    }
}
