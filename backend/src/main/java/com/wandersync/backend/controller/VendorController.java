package com.wandersync.backend.controller;

import com.wandersync.backend.dto.CreateTripRequest;
import com.wandersync.backend.dto.BookingResponse;
import com.wandersync.backend.dto.UpdatePricingRequest;
import com.wandersync.backend.dto.UpdateTripRequest;
import com.wandersync.backend.dto.VendorTripStatsResponse;
import com.wandersync.backend.model.Booking;
import com.wandersync.backend.model.ScheduledTrip;
import com.wandersync.backend.model.User;
import com.wandersync.backend.service.AuthService;
import com.wandersync.backend.service.ScheduledTripService;
import com.wandersync.backend.service.VendorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vendor")
@RequiredArgsConstructor
public class VendorController {

    private final ScheduledTripService tripService;
    private final AuthService authService;
    private final VendorService vendorService;

    // Vendors only ever see and create their own trips; the vendor id
    // comes from the JWT, never from a request parameter. Admins can
    // list all trips via a separate admin endpoint if needed.
    @GetMapping("/trips")
    @PreAuthorize("hasAnyRole('VENDOR', 'ADMIN')")
    public ResponseEntity<List<ScheduledTrip>> getVendorTrips(Authentication authentication) {
        User vendor = authService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(tripService.getTripsByVendor(vendor.getId()));
    }

    @PostMapping("/trips")
    @PreAuthorize("hasAnyRole('VENDOR', 'ADMIN')")
    public ResponseEntity<ScheduledTrip> createTrip(@Valid @RequestBody CreateTripRequest request,
                                                    Authentication authentication) {
        User vendor = authService.getUserByEmail(authentication.getName());

        List<ScheduledTrip.Seat> seatMap = request.getSeats().stream()
                .map(seat -> ScheduledTrip.Seat.builder()
                        .seatId(seat.getSeatId())
                        .row(seat.getRow())
                        .column(seat.getColumn())
                        .price(seat.getPrice())
                        .status(ScheduledTrip.SeatStatus.AVAILABLE)
                        .build())
                .toList();

        List<ScheduledTrip.AddOn> addOns = request.getAvailableAddons() != null
                ? request.getAvailableAddons().stream()
                        .map(addOn -> ScheduledTrip.AddOn.builder()
                                .id(UUID.randomUUID().toString())
                                .name(addOn.getName())
                                .price(addOn.getPrice())
                                .build())
                        .toList()
                : List.of();

        ScheduledTrip trip = ScheduledTrip.builder()
                .vendorId(vendor.getId())
                .title(request.getTitle())
                .origin(request.getOrigin())
                .destination(request.getDestination())
                .departureDate(request.getDepartureDate())
                .vehicleType(request.getVehicleType())
                .basePrice(request.getBasePrice())
                .dynamicYieldMultiplier(request.getDynamicYieldMultiplier())
                .seatMap(seatMap)
                .availableAddons(addOns)
                .build();

        ScheduledTrip created = tripService.createTrip(trip);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/trips/{id}")
    @PreAuthorize("hasAnyRole('VENDOR', 'ADMIN')")
    public ResponseEntity<ScheduledTrip> updateTrip(
            @PathVariable String id,
            @Valid @RequestBody UpdateTripRequest request,
            Authentication authentication) {
        User vendor = authService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(vendorService.updateTrip(id, request, vendor));
    }

    @PatchMapping("/trips/{id}/pricing")
    @PreAuthorize("hasAnyRole('VENDOR', 'ADMIN')")
    public ResponseEntity<ScheduledTrip> updatePricing(
            @PathVariable String id,
            @Valid @RequestBody UpdatePricingRequest request,
            Authentication authentication) {
        User vendor = authService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(vendorService.updatePricing(id, request, vendor));
    }

    @DeleteMapping("/trips/{id}")
    @PreAuthorize("hasAnyRole('VENDOR', 'ADMIN')")
    public ResponseEntity<Void> deleteTrip(
            @PathVariable String id,
            Authentication authentication) {
        User vendor = authService.getUserByEmail(authentication.getName());
        vendorService.deleteTrip(id, vendor);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/bookings")
    @PreAuthorize("hasAnyRole('VENDOR', 'ADMIN')")
    public ResponseEntity<List<BookingResponse>> getVendorBookings(
            @RequestParam(required = false) Booking.Status status,
            Authentication authentication) {
        User vendor = authService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(vendorService.getBookings(vendor, status));
    }

    @GetMapping("/occupancy")
    @PreAuthorize("hasAnyRole('VENDOR', 'ADMIN')")
    public ResponseEntity<List<VendorTripStatsResponse>> getVendorOccupancy(
            Authentication authentication) {
        User vendor = authService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(vendorService.getTripStats(vendor));
    }

    @GetMapping("/trips/{id}/occupancy")
    @PreAuthorize("hasAnyRole('VENDOR', 'ADMIN')")
    public ResponseEntity<VendorTripStatsResponse> getTripOccupancy(
            @PathVariable String id,
            Authentication authentication) {
        User vendor = authService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(vendorService.getTripStats(id, vendor));
    }
}
