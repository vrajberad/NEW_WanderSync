package com.wandersync.backend.service;

import com.wandersync.backend.dto.BookingResponse;
import com.wandersync.backend.dto.UpdatePricingRequest;
import com.wandersync.backend.dto.UpdateTripRequest;
import com.wandersync.backend.dto.VendorTripStatsResponse;
import com.wandersync.backend.exception.ResourceConflictException;
import com.wandersync.backend.model.Booking;
import com.wandersync.backend.model.ScheduledTrip;
import com.wandersync.backend.model.User;
import com.wandersync.backend.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorService {

    private static final BigDecimal ZERO_REVENUE = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    private final ScheduledTripService tripService;
    private final BookingRepository bookingRepository;

    public ScheduledTrip updateTrip(String tripId, UpdateTripRequest request, User vendor) {
        requireTripAccess(tripId, vendor);
        return tripService.updateTripDetails(
                tripId,
                request.getTitle(),
                request.getOrigin(),
                request.getDestination(),
                request.getDepartureDate(),
                request.getVehicleType(),
                request.getBasePrice(),
                request.getDynamicYieldMultiplier());
    }

    public ScheduledTrip updatePricing(String tripId, UpdatePricingRequest request, User vendor) {
        requireTripAccess(tripId, vendor);
        return tripService.updateDynamicYieldMultiplier(
                tripId,
                request.getDynamicYieldMultiplier());
    }

    public void deleteTrip(String tripId, User vendor) {
        ScheduledTrip trip = requireTripAccess(tripId, vendor);
        boolean hasActiveBookings = !bookingRepository.findByTripIdAndStatus(
                        tripId, Booking.Status.PENDING_PAYMENT).isEmpty()
                || !bookingRepository.findByTripIdAndStatus(
                        tripId, Booking.Status.CONFIRMED).isEmpty();
        if (hasActiveBookings) {
            throw new ResourceConflictException(
                    "Trip cannot be deleted while it has active bookings");
        }
        tripService.deleteTrip(trip.getId());
    }

    public List<BookingResponse> getBookings(User vendor, Booking.Status status) {
        List<String> tripIds = tripService.getTripsByVendor(vendor.getId()).stream()
                .map(ScheduledTrip::getId)
                .toList();
        if (tripIds.isEmpty()) {
            return List.of();
        }

        Collection<Booking> bookings;
        if (status == null) {
            bookings = bookingRepository.findByTripIdIn(tripIds);
        } else {
            bookings = tripIds.stream()
                    .flatMap(tripId -> bookingRepository.findByTripIdAndStatus(tripId, status).stream())
                    .toList();
        }
        return bookings.stream()
                .map(BookingResponse::from)
                .toList();
    }

    public List<VendorTripStatsResponse> getTripStats(User vendor) {
        return tripService.getTripsByVendor(vendor.getId()).stream()
                .map(this::buildStats)
                .toList();
    }

    public VendorTripStatsResponse getTripStats(String tripId, User vendor) {
        return buildStats(requireTripAccess(tripId, vendor));
    }

    private ScheduledTrip requireTripAccess(String tripId, User vendor) {
        ScheduledTrip trip = tripService.getTripById(tripId);
        if (isAdmin(vendor) || vendor.getId().equals(trip.getVendorId())) {
            return trip;
        }
        throw new AccessDeniedException("You do not have access to this trip");
    }

    private VendorTripStatsResponse buildStats(ScheduledTrip trip) {
        List<ScheduledTrip.Seat> seats = trip.getSeatMap() == null
                ? List.of()
                : trip.getSeatMap();
        int availableSeats = countSeats(seats, ScheduledTrip.SeatStatus.AVAILABLE);
        int lockedSeats = countSeats(seats, ScheduledTrip.SeatStatus.LOCKED);
        int bookedSeats = countSeats(seats, ScheduledTrip.SeatStatus.BOOKED);
        int totalSeats = seats.size();
        BigDecimal occupancyRate = totalSeats == 0
                ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.valueOf(bookedSeats)
                        .divide(BigDecimal.valueOf(totalSeats), 2, RoundingMode.HALF_UP);

        List<Booking> confirmedBookings = bookingRepository.findByTripIdAndStatus(
                trip.getId(), Booking.Status.CONFIRMED);
        BigDecimal confirmedRevenue = confirmedBookings.stream()
                .map(Booking::getPriceBreakdown)
                .filter(java.util.Objects::nonNull)
                .map(Booking.PriceBreakdown::getGrandTotal)
                .filter(java.util.Objects::nonNull)
                .reduce(ZERO_REVENUE, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        return VendorTripStatsResponse.builder()
                .tripId(trip.getId())
                .title(trip.getTitle())
                .totalSeats(totalSeats)
                .availableSeats(availableSeats)
                .lockedSeats(lockedSeats)
                .bookedSeats(bookedSeats)
                .occupancyRate(occupancyRate)
                .confirmedBookings(confirmedBookings.size())
                .confirmedRevenue(confirmedRevenue)
                .build();
    }

    private int countSeats(List<ScheduledTrip.Seat> seats, ScheduledTrip.SeatStatus status) {
        return (int) seats.stream()
                .filter(seat -> seat.getStatus() == status)
                .count();
    }

    private boolean isAdmin(User vendor) {
        return vendor.getRoles() != null && vendor.getRoles().contains(User.Role.ROLE_ADMIN);
    }
}
