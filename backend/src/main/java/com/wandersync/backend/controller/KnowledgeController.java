package com.wandersync.backend.controller;

import com.wandersync.backend.dto.KnowledgeIngestRequest;
import com.wandersync.backend.dto.KnowledgeIngestResponse;
import com.wandersync.backend.dto.KnowledgeSeedResponse;
import com.wandersync.backend.dto.KnowledgeStatsResponse;
import com.wandersync.backend.service.CuratedTravelKnowledge;
import com.wandersync.backend.service.KnowledgeIngestionService;
import com.wandersync.backend.repository.TravelKnowledgeDocumentRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/knowledge")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class KnowledgeController {

    private final KnowledgeIngestionService ingestionService;
    private final CuratedTravelKnowledge curatedTravelKnowledge;
    private final TravelKnowledgeDocumentRepository sourceRepository;
    private final MongoTemplate mongoTemplate;

    @Value("${spring.ai.vectorstore.mongodb.collection-name:travel_knowledge}")
    private String vectorCollectionName;

    @PostMapping
    public ResponseEntity<KnowledgeIngestResponse> ingest(
            @Valid @RequestBody KnowledgeIngestRequest request) {
        return ResponseEntity.ok(ingestionService.ingest(request));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<KnowledgeIngestResponse>> ingestBatch(
            @Valid @RequestBody List<@Valid KnowledgeIngestRequest> requests) {
        return ResponseEntity.ok(ingestionService.ingestBatch(requests));
    }

    @PostMapping("/seed")
    public ResponseEntity<KnowledgeSeedResponse> seed() {
        return ResponseEntity.ok(ingestionService.seed(curatedTravelKnowledge));
    }

    @GetMapping("/stats")
    public ResponseEntity<KnowledgeStatsResponse> stats() {
        return ResponseEntity.ok(KnowledgeStatsResponse.builder()
                .sourceDocuments(sourceRepository.count())
                .knowledgeChunks(mongoTemplate.count(new Query(), vectorCollectionName))
                .build());
    }
}
