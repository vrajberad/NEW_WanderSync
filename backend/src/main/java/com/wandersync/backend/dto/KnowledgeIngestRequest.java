package com.wandersync.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeIngestRequest {

    private String sourceId;

    @NotBlank
    private String title;

    @NotBlank
    private String destination;

    private String region;

    @NotBlank
    private String content;

    private String budgetTier;

    private String mobilityLevel;

    private List<String> tags;

    private List<String> themes;
}
