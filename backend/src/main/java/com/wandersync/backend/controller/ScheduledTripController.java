package com.wandersync.backend.controller;

import com.wandersync.backend.dto.ConfirmBookingRequest;
import com.wandersync.backend.dto.LockSeatRequest;
import com.wandersync.backend.model.ScheduledTrip;
import com.wandersync.backend.service.ScheduledTripService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trips")
@RequiredArgsConstructor
public class ScheduledTripController {

    private final ScheduledTripService tripService;

    @GetMapping
    public ResponseEntity<List<ScheduledTrip>> getAllTrips() {
        return ResponseEntity.ok(tripService.getAllTrips());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduledTrip> getTripById(@PathVariable String id) {
        return ResponseEntity.ok(tripService.getTripById(id));
    }

    @PostMapping("/{id}/lock-seat")
    public ResponseEntity<ScheduledTrip> lockSeat(@PathVariable String id,
                                                  @Valid @RequestBody LockSeatRequest request,
                                                  Authentication authentication) {
        ScheduledTrip updatedTrip = tripService.lockSeat(id, request.getSeatId(), authentication.getName());
        return ResponseEntity.ok(updatedTrip);
    }

    @PostMapping("/{id}/confirm-booking")
    public ResponseEntity<ScheduledTrip> confirmBooking(@PathVariable String id,
                                                        @Valid @RequestBody ConfirmBookingRequest request,
                                                        Authentication authentication) {
        ScheduledTrip confirmedTrip = tripService.confirmSeatBooking(id, request.getSeatIds(), authentication.getName());
        return ResponseEntity.ok(confirmedTrip);
    }
}
