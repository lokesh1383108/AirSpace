package com.example.airspace.AirSpace.service;

import com.example.airspace.AirSpace.models.BookingRequest;
import com.example.airspace.AirSpace.models.user.Customer;
import com.example.airspace.AirSpace.repositories.CustomerRepository;
import com.example.airspace.AirSpace.repositories.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

@Service
public class NotificationService {
    // @Value("${fcm.server.key}")
    private String serverKey = "your_server_key_here"; // Replace with your actual server key

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private final RestTemplate restTemplate;

    public NotificationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendBookingNotification(String token, BookingRequest bookingRequest) {
        String url = "https://fcm.googleapis.com/fcm/send";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "key=" + serverKey);
        headers.set("Content-Type", "application/json");

        String message = String.format(
                "New booking request: %d guests from %s. Check-in: %s, Check-out: %s",
//                bookingRequest.getTotalGuests(),
               customerRepository.findById(bookingRequest.getUserId()),
                bookingRequest.getCheckInTime(),
                bookingRequest.getCheckOutTime()
        );

        String body = String.format(
                "{\"to\":\"%s\",\"notification\":{\"title\":\"New Booking Request\",\"body\":\"%s\"}}",
                token,
                message
        );

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        // Handle response if needed
    }
}
