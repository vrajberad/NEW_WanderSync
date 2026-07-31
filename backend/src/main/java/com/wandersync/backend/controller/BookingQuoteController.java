package com.wandersync.backend.controller;

import com.wandersync.backend.dto.QuoteRequest;
import com.wandersync.backend.dto.QuoteResponse;
import com.wandersync.backend.model.ScheduledTrip;
import com.wandersync.backend.service.PricingService;
import com.wandersync.backend.service.ScheduledTripService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingQuoteController {

    private final ScheduledTripService scheduledTripService;
    private final PricingService pricingService;

    @PostMapping("/quote")
    public ResponseEntity<QuoteResponse> quote(@Valid @RequestBody QuoteRequest request) {
        ScheduledTrip trip = scheduledTripService.getTripById(request.getTripId());
        return ResponseEntity.ok(pricingService.calculateQuote(
                trip,
                request.getSeatIds(),
                request.getAddOnIds()
        ));
    }
}
