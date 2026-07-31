package com.wandersync.backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

// vendorId is intentionally absent — it is bound to the authenticated vendor.
// Seat statuses are not accepted from the client; all seats start AVAILABLE.
@Data
public class CreateTripRequest {

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

    @NotEmpty
    @Valid
    private List<SeatDefinition> seats;

    @Valid
    private List<AddOnDefinition> availableAddons;

    @Data
    public static class SeatDefinition {
        @NotBlank
        private String seatId;
        @NotNull
        @Positive
        private Integer row;
        @NotBlank
        private String column;
        @NotNull
        @Positive
        private BigDecimal price;
    }

    @Data
    public static class AddOnDefinition {
        @NotBlank
        private String name;
        @NotNull
        @PositiveOrZero
        private BigDecimal price;
    }
}
