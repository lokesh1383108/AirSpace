package com.example.airspace.AirSpace.repositories;

import com.example.airspace.AirSpace.models.transaction.History;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HistoryRepository extends MongoRepository<History, ObjectId> {
    public List<History> findAll() ;


    public List<History> findByPropertyId(ObjectId propertyId);

    public List<History> findBycustomerId(ObjectId customerId);

}
