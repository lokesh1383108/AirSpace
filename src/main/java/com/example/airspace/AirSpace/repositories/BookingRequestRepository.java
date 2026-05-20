package com.example.airspace.AirSpace.repositories;


import com.example.airspace.AirSpace.models.BookingRequest;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRequestRepository extends MongoRepository<BookingRequest, ObjectId> {


    List<BookingRequest> findByUserId(ObjectId userId);

    List<BookingRequest> findByPropertyId(ObjectId propertyId);

    boolean existsByPropertyIdAndCheckInTimeLessThanEqualAndCheckOutTimeGreaterThanEqual(ObjectId propertyId, LocalDateTime endDate, LocalDateTime startDate);
}
