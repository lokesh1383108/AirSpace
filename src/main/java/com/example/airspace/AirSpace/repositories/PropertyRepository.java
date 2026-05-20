package com.example.airspace.AirSpace.repositories;

import com.example.airspace.AirSpace.models.Property;
import com.example.airspace.AirSpace.models.user.Host;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends MongoRepository<Property, ObjectId> {
    @NotNull
     List<Property> findAll() ;



    Property getById(ObjectId id);
   List<Property> findByHostId(ObjectId hostId);

    Host getHostById(ObjectId propertyId);

    List<Property> findAllByHostId(ObjectId hostId);

}
