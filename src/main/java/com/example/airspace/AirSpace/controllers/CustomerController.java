package com.example.airspace.AirSpace.controllers;

import com.example.airspace.AirSpace.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/history")
    public ResponseEntity<?> getBookingHistory() {
        return ResponseEntity.ok(customerService.getBookingHistory());
    }

    @PostMapping("/booking/{propertyId}")
    public ResponseEntity<?> bookProperty(@PathVariable String propertyId) {
        customerService.bookProperty(propertyId);
        return ResponseEntity.ok("Property booked");
    }
}
