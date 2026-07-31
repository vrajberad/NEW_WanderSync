package com.wandersync.backend.service;

import com.wandersync.backend.dto.KnowledgeIngestRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CuratedTravelKnowledge {

    public List<KnowledgeIngestRequest> documents() {
        return List.of(
                document(
                        "seed-goa",
                        "Goa beach and heritage guide",
                        "Goa",
                        "West India",
                        "Goa combines long beaches, Portuguese-influenced architecture, and a lively food scene. "
                                + "North Goa is generally busier, while South Goa is known for a quieter pace. "
                                + "Visitors should plan extra time for coastal traffic and respect local beach and wildlife regulations.",
                        "mid-range",
                        "easy",
                        List.of("beaches", "heritage", "food"),
                        List.of("leisure", "culture", "weekend")),
                document(
                        "seed-coorg",
                        "Coorg coffee country guide",
                        "Coorg",
                        "South India",
                        "Coorg, also called Kodagu, is a hill region known for coffee estates, forested landscapes, and local Kodava cuisine. "
                                + "A private vehicle or planned transfer is useful because many viewpoints and estates are spread out. "
                                + "The monsoon season brings lush scenery but can also mean slippery roads and slower travel.",
                        "mid-range",
                        "moderate",
                        List.of("coffee", "hills", "nature"),
                        List.of("slow-travel", "outdoors", "food")),
                document(
                        "seed-jaipur",
                        "Jaipur heritage guide",
                        "Jaipur",
                        "North India",
                        "Jaipur is known for Amber Fort, City Palace, Jantar Mantar, and traditional craft markets. "
                                + "Morning visits are often more comfortable for major forts, especially in warmer months. "
                                + "A local guide can add context to the city's Rajput and Mughal-influenced architecture, while visitors should allow time for market traffic.",
                        "mid-range",
                        "easy",
                        List.of("forts", "markets", "architecture"),
                        List.of("culture", "history", "family")),
                document(
                        "seed-rishikesh",
                        "Rishikesh outdoor travel guide",
                        "Rishikesh",
                        "North India",
                        "Rishikesh sits beside the Ganges and is widely associated with yoga, river rafting, and Himalayan foothill scenery. "
                                + "Rafting availability depends on season and river conditions, so travelers should use licensed operators. "
                                + "The town has several pedestrian and steeply sloped areas, making comfortable footwear useful.",
                        "budget-to-mid-range",
                        "moderate",
                        List.of("river", "yoga", "adventure"),
                        List.of("wellness", "outdoors", "adventure")),
                document(
                        "seed-munnar",
                        "Munnar tea and hills guide",
                        "Munnar",
                        "South India",
                        "Munnar is a highland destination with tea plantations, cool weather, and winding roads through the Western Ghats. "
                                + "Road journeys can take longer than expected because of curves and weather, so flexible itineraries work well. "
                                + "Travelers should follow park guidance when visiting protected areas and avoid disturbing wildlife.",
                        "mid-range",
                        "moderate",
                        List.of("tea", "hills", "wildlife"),
                        List.of("nature", "slow-travel", "photography")),
                document(
                        "seed-udaipur",
                        "Udaipur lakes and palaces guide",
                        "Udaipur",
                        "West India",
                        "Udaipur is centered around lakes, historic palaces, and the Aravalli landscape. "
                                + "The old city is best explored at an unhurried pace, with some narrow lanes and uneven surfaces. "
                                + "Sunset lake views are popular, so travelers should plan transport and restaurant reservations around busy evening periods.",
                        "mid-range",
                        "easy",
                        List.of("lakes", "palaces", "heritage"),
                        List.of("culture", "history", "romance"))
        );
    }

    private KnowledgeIngestRequest document(String sourceId,
                                            String title,
                                            String destination,
                                            String region,
                                            String content,
                                            String budgetTier,
                                            String mobilityLevel,
                                            List<String> tags,
                                            List<String> themes) {
        return KnowledgeIngestRequest.builder()
                .sourceId(sourceId)
                .title(title)
                .destination(destination)
                .region(region)
                .content(content)
                .budgetTier(budgetTier)
                .mobilityLevel(mobilityLevel)
                .tags(tags)
                .themes(themes)
                .build();
    }
}
