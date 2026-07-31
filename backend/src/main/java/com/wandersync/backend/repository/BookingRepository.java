package com.wandersync.backend.repository;

import com.wandersync.backend.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String> {

    List<Booking> findByUserId(String userId);

    List<Booking> findByTripIdAndStatus(String tripId, Booking.Status status);

    List<Booking> findByTripIdIn(Collection<String> tripIds);

    List<Booking> findByGroupLobbyId(String groupLobbyId);

    List<Booking> findByStatus(Booking.Status status);
}
