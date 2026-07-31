package com.wandersync.backend.config;

import com.wandersync.backend.service.CuratedTravelKnowledge;
import com.wandersync.backend.service.KnowledgeIngestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class RagStartupSeeder {

    private final KnowledgeIngestionService ingestionService;
    private final CuratedTravelKnowledge curatedTravelKnowledge;

    @Value("${wandersync.rag.seed-on-startup:false}")
    private boolean seedOnStartup;

    @Bean
    public CommandLineRunner seedCuratedTravelKnowledge() {
        return args -> {
            if (!seedOnStartup) {
                return;
            }
            log.info("Seeding curated travel knowledge on startup");
            ingestionService.seed(curatedTravelKnowledge);
        };
    }
}
