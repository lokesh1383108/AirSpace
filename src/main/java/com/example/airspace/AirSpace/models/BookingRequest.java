package com.example.airspace.AirSpace.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Duration;
import java.time.LocalDateTime;

@Document(collection = "bookingRequests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequest {


    @MongoId(FieldType.OBJECT_ID)
    private String id;

    @NotNull(message = "User ID is required")
    private ObjectId userId;

    @NotNull(message = "Property ID is required")
    private ObjectId propertyId;

    // Embedded for MongoDB
    private GuestComposition guests;

    @NotNull(message = "Check-in time is required")
    private LocalDateTime checkInTime;

    @NotNull(message = "Check-out time is required")
    private LocalDateTime checkOutTime;
    
    @NotNull(message = "Booking created at")
    private LocalDateTime createdAt;

    @NotNull(message = "Booking Request last updated at")
    private LocalDateTime updatedAt;

//    private ObjectId hostId;

   @Builder.Default
    private BookingStatus status = BookingStatus.PENDING;

    // Additional Validation Methods
    public boolean isValidBookingDuration() {
        // Ensure booking is at least 1 hour and not more than 30 days
        Duration duration = Duration.between(checkInTime, checkOutTime);
        return !duration.isNegative() &&
                duration.toHours() >= 1 &&
                duration.toDays() <= 30;
    }

    public int getTotalGuests() {
        return guests.getTotalGuestCount();
    }
    // Nested class for guest composition with more robust validation
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GuestComposition {
        @Min(value = 0, message = "Male guests cannot be negative")
        @Max(value = 2, message = "Maximum 2 male guests allowed")
        private int maleGuests;

        @Min(value = 0, message = "Female guests cannot be negative")
        @Max(value = 2, message = "Maximum 2 female guests allowed")
        private int femaleGuests;

        @Min(value = 0, message = "Children count cannot be negative")
        @Max(value = 2, message = "Maximum 2 children allowed")
        private int children;

        @Min(value = 0, message = "Transgender guests cannot be negative")
        @Max(value = 2, message = "Maximum 2 transgender guests allowed")
        private int transgenderGuests;

        // Convenient method to get total guest count
        public int getTotalGuestCount() {
            return maleGuests + femaleGuests + children + transgenderGuests;
        }

        // Additional validation method
        public boolean isValidGuestComposition() {
            return getTotalGuestCount() <= 5; // Total guest limit
        }
    }

    // Booking Status Enum
    public enum BookingStatus {
        PENDING,       // Initial state
        CONFIRMED,     // Host accepted
        REJECTED,      // Host declined
        CANCELLED,     // Booking cancelled by User
        COMPLETED      // Booking finished
    }

}