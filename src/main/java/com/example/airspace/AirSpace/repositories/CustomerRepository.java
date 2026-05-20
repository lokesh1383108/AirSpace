package com.example.airspace.AirSpace.repositories;

import com.example.airspace.AirSpace.models.user.Customer;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, ObjectId> {
//    Customer findByEmail(String email);
//    Customer findByPhone(String phone);
    @NotNull
    List<Customer> findAll();
}