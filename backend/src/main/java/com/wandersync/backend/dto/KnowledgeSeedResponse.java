package com.wandersync.backend.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class KnowledgeSeedResponse {

    int documentsIngested;
    int chunksCreated;
}
