package com.wandersync.backend.dto;

import com.wandersync.backend.model.Booking;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuoteResponse {

    private String tripId;

    private List<SeatLineItem> seatItems;

    private List<AddOnLineItem> addOnItems;

    private Booking.PriceBreakdown priceBreakdown;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SeatLineItem {
        private String seatId;
        private BigDecimal seatPrice;
        private BigDecimal multiplier;
        private BigDecimal total;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AddOnLineItem {
        private String addOnId;
        private String name;
        private BigDecimal price;
    }
}
