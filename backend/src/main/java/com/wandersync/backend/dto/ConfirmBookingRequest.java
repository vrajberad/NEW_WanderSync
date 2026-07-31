package com.wandersync.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

// userId is intentionally absent — it is derived from the JWT principal.
@Data
public class ConfirmBookingRequest {
    @NotEmpty
    private List<String> seatIds;
}
