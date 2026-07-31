package com.wandersync.backend.controller;

import com.wandersync.backend.dto.ItineraryRequest;
import com.wandersync.backend.service.AIArchitectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AIArchitectController {

    private final AIArchitectService aiArchitectService;

    @PostMapping("/generate-itinerary")
    public ResponseEntity<Map<String, Object>> generateItinerary(@Valid @RequestBody ItineraryRequest request) {
        Map<String, Object> itinerary = aiArchitectService.generateItinerary(
                request.getDestination(),
                request.getDurationDays(),
                request.getBudget(),
                request.getPreferences()
        );
        return ResponseEntity.ok(itinerary);
    }
}
