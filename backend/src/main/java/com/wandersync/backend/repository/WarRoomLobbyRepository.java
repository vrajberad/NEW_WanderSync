package com.wandersync.backend.repository;

import com.wandersync.backend.model.WarRoomLobby;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarRoomLobbyRepository extends MongoRepository<WarRoomLobby, String> {
    List<WarRoomLobby> findByTripId(String tripId);
}
