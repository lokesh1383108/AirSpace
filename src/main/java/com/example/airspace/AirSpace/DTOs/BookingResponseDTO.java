package com.example.airspace.AirSpace.DTOs;

import java.time.LocalDateTime;

import com.example.airspace.AirSpace.models.BookingRequest;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Builder
public class BookingResponseDTO {
private String BookingId;
    private String propertyId;
    private String userId;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private BookingRequestDTO.GuestComposition guests;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
