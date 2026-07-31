package com.wandersync.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

// Only reports facts. For real dependency checks (MongoDB, etc.) use
// Spring Boot Actuator's /actuator/health instead of hardcoding statuses.
@RestController
@RequestMapping("/api/v1/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> healthCheck() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "WanderSync Backend",
                "timestamp", Instant.now().toString()
        ));
    }
}
