package com.wandersync.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VoteRequest {
    @NotBlank
    private String pollItemId;

    private boolean upvote;
}
