package com.example.airspace.AirSpace.repositories;

import com.example.airspace.AirSpace.models.user.Customer;
import com.example.airspace.AirSpace.models.user.Host;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HostRepository extends MongoRepository<Host, ObjectId> {
//    Host findByEmail(String email);
//    Host findByPhone(String phone);
    @NotNull
    List<Host> findAll();
}
