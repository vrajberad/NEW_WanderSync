package com.wandersync.backend.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UpdateTripRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String origin;

    @NotBlank
    private String destination;

    @NotNull
    @Future
    private LocalDate departureDate;

    private String vehicleType;

    @NotNull
    @Positive
    private BigDecimal basePrice;

    @Positive
    private BigDecimal dynamicYieldMultiplier;
}
