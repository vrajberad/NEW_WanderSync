package com.wandersync.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

// hostUserId is intentionally absent — the host is the authenticated user.
// Members are provided as ids/names only; payment state is always
// initialized server-side to PENDING.
@Data
public class CreateLobbyRequest {

    @NotBlank
    private String tripName;

    @NotNull
    @Positive
    private BigDecimal totalCost;

    @NotEmpty
    @Valid
    private List<MemberDefinition> members;

    @Data
    public static class MemberDefinition {
        @NotBlank
        private String userId;
        @NotBlank
        private String name;
    }
}
