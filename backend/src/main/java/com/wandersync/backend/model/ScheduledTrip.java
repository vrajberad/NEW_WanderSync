package com.wandersync.backend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "scheduled_trips")
public class ScheduledTrip {

    @Id
    private String id;

    private String vendorId;

    private String title;

    private String origin;

    private String destination;

    private LocalDate departureDate;

    private String vehicleType; // e.g. "Scania Luxury Coach 30-seater"

    private BigDecimal basePrice;

    private BigDecimal dynamicYieldMultiplier; // e.g. 1.25x

    private List<Seat> seatMap;

    private List<AddOn> availableAddons;

    @Version
    private Long version; // Optimistic locking prevents double-booking race conditions

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Seat {
        private String seatId; // e.g., "1A", "2B"
        private Integer row;
        private String column; // "A", "B", "C", "D"
        private SeatStatus status; // AVAILABLE, LOCKED, BOOKED
        private BigDecimal price;
        private String lockedByUserId;
        private Instant lockExpiryTime;
    }

    public enum SeatStatus {
        AVAILABLE,
        LOCKED,
        BOOKED
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AddOn {
        private String id;
        private String name;
        private BigDecimal price;
    }
}