package com.example.airspace.AirSpace.service;

import com.example.airspace.AirSpace.models.user.Customer;
import com.example.airspace.AirSpace.models.transaction.History;
import com.example.airspace.AirSpace.repositories.CustomerRepository;
import com.example.airspace.AirSpace.repositories.HistoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private CustomerRepository customerRepository;

    public Customer createCustomer(Customer customer) {
//        customer.prepareForSave(); // Prepare customer before saving
        return customerRepository.save(customer);
    }
    public List<History> getBookingHistory() {
        return historyRepository.findAll();
    }

    public void bookProperty(String propertyId) {
        // Logic for booking property
    }
}
