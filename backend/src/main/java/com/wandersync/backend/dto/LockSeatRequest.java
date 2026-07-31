package com.wandersync.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// userId is intentionally absent — it is derived from the JWT principal.
@Data
public class LockSeatRequest {
    @NotBlank
    private String seatId;
}
