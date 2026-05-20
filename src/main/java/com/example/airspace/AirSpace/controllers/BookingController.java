package com.example.airspace.AirSpace.controllers;


import com.example.airspace.AirSpace.DTOs.ApiResponse;
import com.example.airspace.AirSpace.DTOs.AuthResponse;
import com.example.airspace.AirSpace.DTOs.BookingRequestDTO;
import com.example.airspace.AirSpace.DTOs.BookingResponseDTO;
import com.example.airspace.AirSpace.DTOs.RejectBookingRequest;
import com.example.airspace.AirSpace.models.BookingRequest;
import com.example.airspace.AirSpace.service.BookingService;

import jakarta.validation.Valid;

import org.bson.types.ObjectId;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/request")
    public ResponseEntity<?> requestBooking(@RequestBody @Valid BookingRequestDTO bookingRequest, Principal principal) {
            // Call the booking service to handle the booking request(create new booking)
             BookingResponseDTO bookingResponse = bookingService.createBookingRequest(bookingRequest, principal);

            // Build and return a success response
            ApiResponse response = ApiResponse.builder()
                    .success(true)
                    .error(null) // No error, so set to null
                    .object(bookingResponse)
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
   /*
   Booking -> guest/booking --> /create, /cancel,
   host/booking  /accept, /cancel ,

    */



    /*
     * Check booking is present in db
     * Check it should not already rejected "Can not accept the rejected booking"
     * Use booking id and update the staus in DB as ACCEPTED 
     * Return Booking details 
     */
    @PutMapping("/accept/{bookingId}")
    public ResponseEntity<?> acceptBooking(@PathVariable ObjectId bookingId, String Note) {
        bookingService.acceptBooking(bookingId);
        // Build and return response of Booking details 


        ApiResponse response = ApiResponse.builder()
        .success(true)
        .object(bookingId)
        .error(null).build();


        return new ResponseEntity<>(null);
    }

    @PutMapping("/reject/{bookingId}")
    public ResponseEntity<?> rejectBooking(@PathVariable ObjectId bookingId, @RequestBody RejectBookingRequest request) {

        bookingService.rejectBookingByHost(bookingId, request.getReason());

        ApiResponse response = ApiResponse.builder()
            .success(true)
            .error(null)
            .object("Booking rejected. Reason: " + request.getReason())
            .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
