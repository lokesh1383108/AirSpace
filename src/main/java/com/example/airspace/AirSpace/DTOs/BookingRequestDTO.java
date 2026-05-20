package com.example.airspace.AirSpace.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
 
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDTO {
    // Removed database-specific fields like ID
//    private String userId; // Optional, can be set programmatically
    
    @NotNull(message = "Property ID is required")
    private String propertyId;

    @Valid
    private GuestComposition guests;

    @NotNull(message = "Check-in time is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS][XXX]")
    private LocalDateTime checkInTime;

    @NotNull(message = "Check-out time is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS][XXX]")
    private LocalDateTime checkOutTime;


    // Nested DTO for guest composition
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
        private int transGuests;

        public int getTotalGuestCount() {
            return maleGuests + femaleGuests + children + transGuests;
        }
    }


}