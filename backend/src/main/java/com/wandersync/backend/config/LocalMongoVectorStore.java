package com.wandersync.backend.config;

import org.bson.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Local development vector store backed by MongoDB and in-memory cosine search.
 * Atlas Vector Search can replace this bean in a later profile.
 */
@Component
// @ConditionalOnMissingBean(VectorStore.class)
public class LocalMongoVectorStore implements VectorStore {

    private final EmbeddingModel embeddingModel;
    private final MongoTemplate mongoTemplate;
    private final String collectionName;

    public LocalMongoVectorStore(
            EmbeddingModel embeddingModel,
            MongoTemplate mongoTemplate,
            @org.springframework.beans.factory.annotation.Value(
                    "${spring.ai.vectorstore.mongodb.collection-name:travel_knowledge}")
            String collectionName) {
        this.embeddingModel = embeddingModel;
        this.mongoTemplate = mongoTemplate;
        this.collectionName = collectionName;
    }

    @Override
    public void add(List<org.springframework.ai.document.Document> documents) {
        for (org.springframework.ai.document.Document document : documents) {
            float[] embedding = embeddingModel.embed(document.getText());
            Document stored = new Document("_id", document.getId())
                    .append("content", document.getText())
                    .append("metadata", new HashMap<>(document.getMetadata()))
                    .append("embedding", toDoubles(embedding));
            mongoTemplate.save(stored, collectionName);
        }
    }

    @Override
    public void delete(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        mongoTemplate.remove(
                Query.query(Criteria.where("_id").in(ids)),
                collectionName);
    }

    @Override
    public void delete(Filter.Expression filterExpression) {
        throw new UnsupportedOperationException("Filter deletion is not supported by the local vector store");
    }

    @Override
    public List<org.springframework.ai.document.Document> similaritySearch(SearchRequest request) {
        float[] queryEmbedding = embeddingModel.embed(request.getQuery());
        double threshold = request.getSimilarityThreshold();
        List<ScoredDocument> scoredDocuments = new ArrayList<>();

        for (Document stored : mongoTemplate.find(new Query(), Document.class, collectionName)) {
            List<?> values = stored.getList("embedding", Object.class);
            if (values == null) {
                continue;
            }
            float[] embedding = new float[values.size()];
            for (int index = 0; index < values.size(); index++) {
                embedding[index] = ((Number) values.get(index)).floatValue();
            }
            double score = cosineSimilarity(queryEmbedding, embedding);
            if (score >= threshold) {
                Map<String, Object> metadata = stored.get("metadata", Map.class);
                scoredDocuments.add(new ScoredDocument(
                        org.springframework.ai.document.Document.builder()
                                .id(stored.getString("_id"))
                                .text(stored.getString("content"))
                                .metadata(metadata == null ? Map.of() : metadata)
                                .score(score)
                                .build(),
                        score));
            }
        }

        return scoredDocuments.stream()
                .sorted(Comparator.comparingDouble(ScoredDocument::score).reversed())
                .limit(request.getTopK())
                .map(ScoredDocument::document)
                .toList();
    }

    private List<Double> toDoubles(float[] embedding) {
        List<Double> values = new ArrayList<>(embedding.length);
        for (float value : embedding) {
            values.add((double) value);
        }
        return values;
    }

    private double cosineSimilarity(float[] left, float[] right) {
        int dimensions = Math.min(left.length, right.length);
        double dot = 0;
        double leftNorm = 0;
        double rightNorm = 0;
        for (int index = 0; index < dimensions; index++) {
            dot += left[index] * right[index];
            leftNorm += left[index] * left[index];
            rightNorm += right[index] * right[index];
        }
        if (leftNorm == 0 || rightNorm == 0) {
            return 0;
        }
        return dot / (Math.sqrt(leftNorm) * Math.sqrt(rightNorm));
    }

    private record ScoredDocument(
            org.springframework.ai.document.Document document,
            double score) {
    }
}
