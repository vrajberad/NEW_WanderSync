package com.wandersync.backend.service;

import com.wandersync.backend.exception.ResourceNotFoundException;
import com.wandersync.backend.exception.SeatConflictException;
import com.wandersync.backend.model.ScheduledTrip;
import com.wandersync.backend.repository.ScheduledTripRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledTripService {

    private static final int LOCK_DURATION_MINUTES = 10;
    private static final int MAX_OPTIMISTIC_RETRIES = 3;

    private final ScheduledTripRepository tripRepository;

    public List<ScheduledTrip> getAllTrips() {
        return tripRepository.findAll();
    }

    public ScheduledTrip getTripById(String id) {
        return tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + id));
    }

    public List<ScheduledTrip> getTripsByVendor(String vendorId) {
        return tripRepository.findByVendorId(vendorId);
    }

    public ScheduledTrip createTrip(ScheduledTrip trip) {
        return tripRepository.save(trip);
    }

    public ScheduledTrip updateTripDetails(String tripId,
                                            String title,
                                            String origin,
                                            String destination,
                                            LocalDate departureDate,
                                            String vehicleType,
                                            BigDecimal basePrice,
                                            BigDecimal dynamicYieldMultiplier) {
        return withOptimisticRetry(() -> {
            ScheduledTrip trip = getTripById(tripId);
            trip.setTitle(title);
            trip.setOrigin(origin);
            trip.setDestination(destination);
            trip.setDepartureDate(departureDate);
            trip.setVehicleType(vehicleType);
            trip.setBasePrice(basePrice);
            trip.setDynamicYieldMultiplier(dynamicYieldMultiplier);
            return tripRepository.save(trip);
        });
    }

    public ScheduledTrip updateDynamicYieldMultiplier(String tripId, BigDecimal multiplier) {
        return withOptimisticRetry(() -> {
            ScheduledTrip trip = getTripById(tripId);
            trip.setDynamicYieldMultiplier(multiplier);
            return tripRepository.save(trip);
        });
    }

    public void deleteTrip(String tripId) {
        withOptimisticRetry(() -> {
            ScheduledTrip trip = getTripById(tripId);
            tripRepository.delete(trip);
            return trip;
        });
    }

    public ScheduledTrip lockSeat(String tripId, String seatId, String userId) {
        return withOptimisticRetry(() -> doLockSeat(tripId, seatId, userId));
    }

    public ScheduledTrip confirmSeatBooking(String tripId, List<String> seatIds, String userId) {
        return withOptimisticRetry(() -> doConfirmSeatBooking(tripId, seatIds, userId));
    }

    public void validateSeatsLocked(String tripId, List<String> seatIds, String userId) {
        ScheduledTrip trip = getTripById(tripId);
        validateSeatsLocked(trip, seatIds, userId);
    }

    public void validateSeatsLocked(ScheduledTrip trip, List<String> seatIds, String userId) {
        Instant now = Instant.now();
        for (String seatId : seatIds) {
            ScheduledTrip.Seat seat = findSeat(trip, seatId);
            if (seat.getStatus() == ScheduledTrip.SeatStatus.BOOKED) {
                throw new SeatConflictException("Seat " + seatId + " is already booked");
            }
            if (seat.getStatus() != ScheduledTrip.SeatStatus.LOCKED
                    || !userId.equals(seat.getLockedByUserId())
                    || seat.getLockExpiryTime() == null
                    || !seat.getLockExpiryTime().isAfter(now)) {
                throw new SeatConflictException(
                        "Seat " + seatId + " is not locked by you or the lock has expired");
            }
        }
    }

    public boolean hasActiveSeatLocks(String tripId, List<String> seatIds, String userId) {
        ScheduledTrip trip;
        try {
            trip = getTripById(tripId);
        } catch (ResourceNotFoundException ex) {
            return false;
        }
        Instant now = Instant.now();
        for (String seatId : seatIds) {
            ScheduledTrip.Seat seat;
            try {
                seat = findSeat(trip, seatId);
            } catch (RuntimeException ex) {
                return false;
            }
            if (seat.getStatus() != ScheduledTrip.SeatStatus.LOCKED
                    || !userId.equals(seat.getLockedByUserId())
                    || seat.getLockExpiryTime() == null
                    || !seat.getLockExpiryTime().isAfter(now)) {
                return false;
            }
        }
        return true;
    }

    public ScheduledTrip releaseSeats(String tripId, List<String> seatIds, String userId) {
        return withOptimisticRetry(() -> doReleaseSeats(tripId, seatIds, userId, true));
    }

    public ScheduledTrip releaseExpiredSeats(String tripId, List<String> seatIds, String userId) {
        return withOptimisticRetry(() -> doReleaseSeats(tripId, seatIds, userId, false));
    }

    private ScheduledTrip doLockSeat(String tripId, String seatId, String userId) {
        ScheduledTrip trip = getTripById(tripId);
        ScheduledTrip.Seat seat = findSeat(trip, seatId);
        Instant now = Instant.now();

        if (seat.getStatus() == ScheduledTrip.SeatStatus.BOOKED) {
            throw new SeatConflictException("Seat " + seatId + " is already booked");
        }
        if (isActivelyLockedByOther(seat, userId, now)) {
            throw new SeatConflictException("Seat " + seatId + " is currently locked by another user");
        }

        seat.setStatus(ScheduledTrip.SeatStatus.LOCKED);
        seat.setLockedByUserId(userId);
        seat.setLockExpiryTime(now.plus(LOCK_DURATION_MINUTES, ChronoUnit.MINUTES));

        return tripRepository.save(trip);
    }

    private ScheduledTrip doConfirmSeatBooking(String tripId, List<String> seatIds, String userId) {
        ScheduledTrip trip = getTripById(tripId);
        Instant now = Instant.now();

        // Validate every seat before mutating anything so the booking is all-or-nothing.
        List<ScheduledTrip.Seat> seatsToBook = seatIds.stream()
                .map(seatId -> findSeat(trip, seatId))
                .toList();

        for (ScheduledTrip.Seat seat : seatsToBook) {
            if (seat.getStatus() == ScheduledTrip.SeatStatus.BOOKED) {
                throw new SeatConflictException("Seat " + seat.getSeatId() + " is already booked");
            }
            boolean hasValidLock = seat.getStatus() == ScheduledTrip.SeatStatus.LOCKED
                    && userId.equals(seat.getLockedByUserId())
                    && seat.getLockExpiryTime() != null
                    && seat.getLockExpiryTime().isAfter(now);
            if (!hasValidLock) {
                throw new SeatConflictException(
                        "Seat " + seat.getSeatId() + " is not locked by you or the lock has expired. Lock it again before confirming.");
            }
        }

        for (ScheduledTrip.Seat seat : seatsToBook) {
            seat.setStatus(ScheduledTrip.SeatStatus.BOOKED);
            seat.setLockedByUserId(null);
            seat.setLockExpiryTime(null);
        }

        return tripRepository.save(trip);
    }

    private ScheduledTrip doReleaseSeats(String tripId, List<String> seatIds, String userId,
                                         boolean releaseBookedSeats) {
        ScheduledTrip trip = getTripById(tripId);
        boolean changed = false;
        for (String seatId : seatIds) {
            ScheduledTrip.Seat seat = findSeat(trip, seatId);
            boolean ownedLock = seat.getStatus() == ScheduledTrip.SeatStatus.LOCKED
                    && userId.equals(seat.getLockedByUserId());
            if (ownedLock || (releaseBookedSeats && seat.getStatus() == ScheduledTrip.SeatStatus.BOOKED)) {
                seat.setStatus(ScheduledTrip.SeatStatus.AVAILABLE);
                seat.setLockedByUserId(null);
                seat.setLockExpiryTime(null);
                changed = true;
            }
        }
        return changed ? tripRepository.save(trip) : trip;
    }

    /**
     * Releases expired seat locks so seats become AVAILABLE again
     * without waiting for another lock attempt.
     */
    @Scheduled(fixedRateString = "${wandersync.seat-lock.cleanup-interval-ms:60000}")
    public void releaseExpiredLocks() {
        Instant now = Instant.now();
        for (ScheduledTrip trip : tripRepository.findAll()) {
            boolean changed = false;
            if (trip.getSeatMap() == null) {
                continue;
            }
            for (ScheduledTrip.Seat seat : trip.getSeatMap()) {
                if (seat.getStatus() == ScheduledTrip.SeatStatus.LOCKED
                        && seat.getLockExpiryTime() != null
                        && seat.getLockExpiryTime().isBefore(now)) {
                    seat.setStatus(ScheduledTrip.SeatStatus.AVAILABLE);
                    seat.setLockedByUserId(null);
                    seat.setLockExpiryTime(null);
                    changed = true;
                }
            }
            if (changed) {
                try {
                    tripRepository.save(trip);
                } catch (OptimisticLockingFailureException e) {
                    log.debug("Skipping lock cleanup for trip {} due to concurrent update", trip.getId());
                }
            }
        }
    }

    private ScheduledTrip.Seat findSeat(ScheduledTrip trip, String seatId) {
        if (trip.getSeatMap() == null) {
            throw new ResourceNotFoundException("Trip " + trip.getId() + " has no seat map");
        }
        return trip.getSeatMap().stream()
                .filter(seat -> seat.getSeatId().equalsIgnoreCase(seatId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Seat ID " + seatId + " not found in trip " + trip.getId()));
    }

    private boolean isActivelyLockedByOther(ScheduledTrip.Seat seat, String userId, Instant now) {
        return seat.getStatus() == ScheduledTrip.SeatStatus.LOCKED
                && seat.getLockExpiryTime() != null
                && seat.getLockExpiryTime().isAfter(now)
                && !userId.equals(seat.getLockedByUserId());
    }

    private ScheduledTrip withOptimisticRetry(TripOperation operation) {
        for (int attempt = 1; attempt <= MAX_OPTIMISTIC_RETRIES; attempt++) {
            try {
                return operation.execute();
            } catch (OptimisticLockingFailureException e) {
                log.debug("Optimistic lock conflict, attempt {}/{}", attempt, MAX_OPTIMISTIC_RETRIES);
            }
        }
        throw new SeatConflictException("Seat state changed concurrently. Please retry.");
    }

    @FunctionalInterface
    private interface TripOperation {
        ScheduledTrip execute();
    }
}
