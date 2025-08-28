package com.example.Smart_Doc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Smart_Doc.model.dto.Result;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<Result<Map<String, Object>>> healthCheck() {
        Map<String, Object> health = Map.of(
            "status", "UP",
            "timestamp", LocalDateTime.now(),
            "service", "Smart Doc API",
            "version", "1.0.0"
        );
        
        return ResponseEntity.ok(new Result<>(200, "Service is healthy", health, true));
    }
}