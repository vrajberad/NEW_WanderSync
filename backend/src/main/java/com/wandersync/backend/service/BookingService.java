package com.wandersync.backend.service;

import com.wandersync.backend.dto.BookingResponse;
import com.wandersync.backend.dto.CreateBookingRequest;
import com.wandersync.backend.dto.QuoteResponse;
import com.wandersync.backend.exception.ResourceNotFoundException;
import com.wandersync.backend.exception.SeatConflictException;
import com.wandersync.backend.model.Booking;
import com.wandersync.backend.model.ScheduledTrip;
import com.wandersync.backend.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ScheduledTripService scheduledTripService;
    private final PricingService pricingService;

    public BookingResponse createBooking(CreateBookingRequest request, String userId) {
        ScheduledTrip trip = scheduledTripService.getTripById(request.getTripId());
        scheduledTripService.validateSeatsLocked(trip, request.getSeatIds(), userId);

        QuoteResponse quote = pricingService.calculateQuote(
                trip,
                request.getSeatIds(),
                request.getAddOnIds());

        Booking booking = Booking.builder()
                .userId(userId)
                .tripId(trip.getId())
                .seatIds(List.copyOf(request.getSeatIds()))
                .addOnIds(request.getAddOnIds() == null ? List.of() : List.copyOf(request.getAddOnIds()))
                .priceBreakdown(quote.getPriceBreakdown())
                .status(Booking.Status.PENDING_PAYMENT)
                .paymentIds(List.of())
                .build();

        return BookingResponse.from(bookingRepository.save(booking));
    }

    public List<BookingResponse> getMyBookings(String userId) {
        return bookingRepository.findByUserId(userId).stream()
                .map(BookingResponse::from)
                .toList();
    }

    public BookingResponse getBookingForUser(String bookingId, String userId) {
        return BookingResponse.from(findOwnedBooking(bookingId, userId));
    }

    public BookingResponse cancelBooking(String bookingId, String userId) {
        Booking booking = findOwnedBooking(bookingId, userId);
        ensureTransition(booking.getStatus(), Booking.Status.CANCELLED);
        scheduledTripService.releaseSeats(booking.getTripId(), booking.getSeatIds(), userId);
        booking.setStatus(Booking.Status.CANCELLED);
        return BookingResponse.from(bookingRepository.save(booking));
    }

    /**
     * Payment integration in T7 calls this method after gateway verification.
     */
    public Booking confirmBooking(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));
        ensureTransition(booking.getStatus(), Booking.Status.CONFIRMED);
        scheduledTripService.confirmSeatBooking(booking.getTripId(), booking.getSeatIds(), booking.getUserId());
        booking.setStatus(Booking.Status.CONFIRMED);
        return bookingRepository.save(booking);
    }

    @Scheduled(fixedRateString = "${wandersync.seat-lock.cleanup-interval-ms:60000}")
    public void expireStaleBookings() {
        for (Booking booking : bookingRepository.findByStatus(Booking.Status.PENDING_PAYMENT)) {
            if (scheduledTripService.hasActiveSeatLocks(
                    booking.getTripId(), booking.getSeatIds(), booking.getUserId())) {
                continue;
            }

            try {
                scheduledTripService.releaseExpiredSeats(
                        booking.getTripId(), booking.getSeatIds(), booking.getUserId());
            } catch (ResourceNotFoundException ex) {
                log.debug("Trip {} no longer exists while expiring booking {}",
                        booking.getTripId(), booking.getId());
            } catch (OptimisticLockingFailureException ex) {
                log.debug("Skipping seat release for booking {} due to concurrent trip update",
                        booking.getId());
                continue;
            } catch (SeatConflictException ex) {
                log.debug("Skipping seat release for booking {} due to concurrent seat update",
                        booking.getId());
                continue;
            }

            booking.setStatus(Booking.Status.EXPIRED);
            try {
                bookingRepository.save(booking);
            } catch (OptimisticLockingFailureException ex) {
                log.debug("Skipping booking {} expiry due to concurrent update", booking.getId());
            }
        }
    }

    private Booking findOwnedBooking(String bookingId, String userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));
        if (!userId.equals(booking.getUserId())) {
            throw new ResourceNotFoundException("Booking not found with id: " + bookingId);
        }
        return booking;
    }

    private void ensureTransition(Booking.Status current, Booking.Status target) {
        boolean legal = switch (current) {
            case PENDING_PAYMENT -> target == Booking.Status.CONFIRMED
                    || target == Booking.Status.CANCELLED
                    || target == Booking.Status.EXPIRED;
            case CONFIRMED -> target == Booking.Status.CANCELLED;
            case CANCELLED, EXPIRED -> false;
        };
        if (!legal) {
            throw new IllegalStateException(
                    "Cannot transition booking from " + current + " to " + target);
        }
    }
}
