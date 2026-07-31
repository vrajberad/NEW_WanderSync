package com.wandersync.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * MOCK implementation. Returns a static sample itinerary and is clearly
 * labeled as such — no fabricated RAG match counts or confidence scores.
 * Replace with a real Spring AI + vector-store pipeline when ready.
 */
@Service
@RequiredArgsConstructor
public class AIArchitectService {

    public Map<String, Object> generateItinerary(String destination, int days, String budget, List<String> preferences) {
        List<Map<String, Object>> itineraryDays = List.of(
            Map.of(
                "day", 1,
                "title", "Arrival & Cultural Immersion in " + destination,
                "activities", List.of(
                    "Check-in at boutique eco-resort",
                    "Guided walking tour of historic Old Town & local artisan market",
                    "Welcome dinner featuring regional culinary specialties"
                ),
                "estimatedCost", "$120"
            ),
            Map.of(
                "day", 2,
                "title", "Scenic Exploration & Hidden Gems",
                "activities", List.of(
                    "Sunrise panoramic view & nature trail hike",
                    "Locally sourced organic lunch at hilltop bistro",
                    "Interactive artisan workshop with master craftsman"
                ),
                "estimatedCost", "$180"
            ),
            Map.of(
                "day", 3,
                "title", "Leisure & Farewell Sunset Experience",
                "activities", List.of(
                    "Morning spa & wellness session",
                    "Custom shopping tour & cafe hopping",
                    "Sunset catamaran cruise with live traditional music"
                ),
                "estimatedCost", "$210"
            )
        );

        return Map.of(
            "destination", destination,
            "durationDays", days,
            "budgetCategory", budget,
            "matchedPreferences", preferences != null ? preferences : List.of(),
            "generator", "mock",
            "itinerary", itineraryDays
        );
    }
}
