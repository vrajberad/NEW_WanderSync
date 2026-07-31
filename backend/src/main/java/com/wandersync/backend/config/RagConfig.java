package com.wandersync.backend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class RagConfig {

    @Bean
    @ConditionalOnMissingBean(EmbeddingModel.class)
    @org.springframework.boot.autoconfigure.condition.ConditionalOnProperty(
            name = "spring.ai.model.embedding.text",
            havingValue = "none",
            matchIfMissing = true)
    public EmbeddingModel localEmbeddingModel(
            @Value("${spring.ai.google.genai.embedding.text.options.dimensions:768}") int dimensions) {
        return new LocalFallbackEmbeddingModel(dimensions);
    }

    @Slf4j
    private static final class LocalFallbackEmbeddingModel implements EmbeddingModel {

        private final int dimensions;

        private LocalFallbackEmbeddingModel(int dimensions) {
            this.dimensions = dimensions;
            log.warn("Using deterministic local embedding fallback with {} dimensions; "
                    + "semantic search requires the Gemini embedding model and Atlas vector search",
                    dimensions);
        }

        @Override
        public EmbeddingResponse call(EmbeddingRequest request) {
            List<Embedding> embeddings = new ArrayList<>();
            List<String> instructions = request.getInstructions();
            for (int index = 0; index < instructions.size(); index++) {
                embeddings.add(new Embedding(hashEmbedding(instructions.get(index)), index));
            }
            return new EmbeddingResponse(embeddings);
        }

        @Override
        public float[] embed(Document document) {
            return hashEmbedding(document.getText());
        }

        @Override
        public int dimensions() {
            return dimensions;
        }

        private float[] hashEmbedding(String text) {
            float[] vector = new float[dimensions];
            double norm = 0;
            for (int index = 0; index < dimensions; index++) {
                byte[] digest = digest(text + "#" + index);
                vector[index] = digest[0] / 128.0f;
                norm += vector[index] * vector[index];
            }
            if (norm == 0) {
                return vector;
            }
            float magnitude = (float) Math.sqrt(norm);
            for (int index = 0; index < vector.length; index++) {
                vector[index] /= magnitude;
            }
            return vector;
        }

        private byte[] digest(String value) {
            try {
                return MessageDigest.getInstance("SHA-256")
                        .digest(value.getBytes(StandardCharsets.UTF_8));
            } catch (NoSuchAlgorithmException ex) {
                throw new IllegalStateException("SHA-256 is not available", ex);
            }
        }
    }
}
