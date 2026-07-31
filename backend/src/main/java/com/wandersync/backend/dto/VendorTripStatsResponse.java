package com.wandersync.backend.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class VendorTripStatsResponse {

    String tripId;
    String title;
    int totalSeats;
    int availableSeats;
    int lockedSeats;
    int bookedSeats;
    BigDecimal occupancyRate;
    long confirmedBookings;
    BigDecimal confirmedRevenue;
}
