package com.wandersync.backend.repository;

import com.wandersync.backend.model.ScheduledTrip;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduledTripRepository extends MongoRepository<ScheduledTrip, String> {
    List<ScheduledTrip> findByVendorId(String vendorId);
    List<ScheduledTrip> findByDestinationContainingIgnoreCase(String destination);
}
