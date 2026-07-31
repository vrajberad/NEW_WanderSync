package com.wandersync.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "bookings")
@CompoundIndex(name = "trip_status_idx", def = "{'tripId': 1, 'status': 1}")
public class Booking {

    @Id
    private String id;

    @Indexed
    private String userId;

    private String tripId;

    @Indexed
    private String groupLobbyId;

    private List<String> seatIds;

    private List<String> addOnIds;

    private PriceBreakdown priceBreakdown;

    private Status status;

    private List<String> paymentIds;

    @Version
    private Long version;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public enum Status {
        PENDING_PAYMENT,
        CONFIRMED,
        CANCELLED,
        EXPIRED
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PriceBreakdown {

        private BigDecimal baseSeatsSubtotal;

        private BigDecimal addOnsSubtotal;

        private BigDecimal dynamicYieldMultiplier;

        private BigDecimal grandTotal;

        @Builder.Default
        private String currency = "INR";
    }
}
