package com.example.airspace.AirSpace.repositories;

import com.example.airspace.AirSpace.models.Ratings;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RatingsRepository extends MongoRepository<Ratings, ObjectId> {
}
