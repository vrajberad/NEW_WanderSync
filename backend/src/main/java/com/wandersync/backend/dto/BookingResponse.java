package com.wandersync.backend.dto;

import com.wandersync.backend.model.Booking;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
@Builder
public class BookingResponse {

    String id;
    String userId;
    String tripId;
    String groupLobbyId;
    List<String> seatIds;
    List<String> addOnIds;
    Booking.PriceBreakdown priceBreakdown;
    Booking.Status status;
    List<String> paymentIds;
    Instant createdAt;
    Instant updatedAt;

    public static BookingResponse from(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .userId(booking.getUserId())
                .tripId(booking.getTripId())
                .groupLobbyId(booking.getGroupLobbyId())
                .seatIds(booking.getSeatIds())
                .addOnIds(booking.getAddOnIds())
                .priceBreakdown(booking.getPriceBreakdown())
                .status(booking.getStatus())
                .paymentIds(booking.getPaymentIds())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }
}
