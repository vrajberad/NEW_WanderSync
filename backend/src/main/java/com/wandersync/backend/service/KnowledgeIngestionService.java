package com.wandersync.backend.service;

import com.wandersync.backend.dto.KnowledgeIngestRequest;
import com.wandersync.backend.dto.KnowledgeIngestResponse;
import com.wandersync.backend.dto.KnowledgeSeedResponse;
import com.wandersync.backend.model.TravelKnowledgeDocument;
import com.wandersync.backend.repository.TravelKnowledgeDocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class KnowledgeIngestionService {

    private final TravelKnowledgeDocumentRepository sourceRepository;
    private final VectorStore vectorStore;
    private final MongoTemplate mongoTemplate;

    @Value("${spring.ai.vectorstore.mongodb.collection-name:travel_knowledge}")
    private String vectorCollectionName;

    public KnowledgeIngestResponse ingest(KnowledgeIngestRequest request) {
        String sourceId = request.getSourceId() == null || request.getSourceId().isBlank()
                ? UUID.randomUUID().toString()
                : request.getSourceId();
        TravelKnowledgeDocument source = TravelKnowledgeDocument.builder()
                .id(sourceId)
                .title(request.getTitle())
                .destination(request.getDestination())
                .region(request.getRegion())
                .content(request.getContent())
                .budgetTier(request.getBudgetTier())
                .mobilityLevel(request.getMobilityLevel())
                .tags(request.getTags() == null ? List.of() : List.copyOf(request.getTags()))
                .themes(request.getThemes() == null ? List.of() : List.copyOf(request.getThemes()))
                .build();
        sourceRepository.save(source);

        removeExistingChunks(sourceId);
        List<Document> chunks = createChunks(source);
        if (!chunks.isEmpty()) {
            vectorStore.add(chunks);
        }
        log.info("Ingested travel knowledge source {} into {} chunks", sourceId, chunks.size());
        return KnowledgeIngestResponse.builder()
                .sourceId(sourceId)
                .chunksCreated(chunks.size())
                .build();
    }

    public List<KnowledgeIngestResponse> ingestBatch(List<KnowledgeIngestRequest> requests) {
        return requests.stream()
                .map(this::ingest)
                .toList();
    }

    public KnowledgeSeedResponse seed(CuratedTravelKnowledge curatedTravelKnowledge) {
        List<KnowledgeIngestResponse> results = ingestBatch(curatedTravelKnowledge.documents());
        return KnowledgeSeedResponse.builder()
                .documentsIngested(results.size())
                .chunksCreated(results.stream()
                        .mapToInt(KnowledgeIngestResponse::getChunksCreated)
                        .sum())
                .build();
    }

    private void removeExistingChunks(String sourceId) {
        mongoTemplate.remove(
                Query.query(Criteria.where("metadata.sourceId").is(sourceId)),
                vectorCollectionName);
    }

    private List<Document> createChunks(TravelKnowledgeDocument source) {
        TokenTextSplitter splitter = TokenTextSplitter.builder()
                .withChunkSize(800)
                .withMinChunkSizeChars(200)
                .withMinChunkLengthToEmbed(20)
                .withMaxNumChunks(100)
                .withKeepSeparator(true)
                .build();
        List<Document> splitDocuments = splitter.split(new Document(source.getContent()));
        List<Document> chunks = new ArrayList<>(splitDocuments.size());
        for (int index = 0; index < splitDocuments.size(); index++) {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("sourceId", source.getId());
            metadata.put("title", source.getTitle());
            metadata.put("destination", source.getDestination());
            metadata.put("region", source.getRegion());
            metadata.put("budgetTier", source.getBudgetTier());
            metadata.put("mobilityLevel", source.getMobilityLevel());
            metadata.put("tags", source.getTags());
            metadata.put("themes", source.getThemes());
            metadata.put("chunkIndex", index);
            chunks.add(Document.builder()
                    .id(source.getId() + ":" + index)
                    .text(splitDocuments.get(index).getText())
                    .metadata(metadata)
                    .build());
        }
        return chunks;
    }
}
