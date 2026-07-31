package com.wandersync.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "travel_knowledge_source")
public class TravelKnowledgeDocument {

    @Id
    private String id;

    private String title;

    private String destination;

    private String region;

    private String content;

    private String budgetTier;

    private String mobilityLevel;

    private List<String> tags;

    private List<String> themes;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
