package com.example.airspace.AirSpace.models.transaction;//package com.example.airspace.AirSpace.models.transaction;
//
//
//import jakarta.validation.constraints.NotNull;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;
//
//
//import java.time.LocalDateTime;
//
//@Document(collection = "completeTransactions")
//public class CompleteTransaction {
//
//    @Id
//    private String id;
//
//    @NotNull(message = "User ID is required")
//    private String userId;
//
//
//
//    @NotNull(message = "Check-in time is required")
//    private LocalDateTime checkInTime;
//
//    @NotNull(message = "Check-out time is required")
//    private LocalDateTime checkOutTime;
//
//    // Getters and setters...
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }
//
//
//    public LocalDateTime getCheckInTime() {
//        return checkInTime;
//    }
//
//    public void setCheckInTime(LocalDateTime checkInTime) {
//        this.checkInTime = checkInTime;
//    }
//
//    public LocalDateTime getCheckOutTime() {
//        return checkOutTime;
//    }
//
//    public void setCheckOutTime(LocalDateTime checkOutTime) {
//        this.checkOutTime = checkOutTime;
//    }
//}

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CompleteTransaction extends History {

    @NotNull(message = "Check-in time is required")
    private LocalDateTime checkInTime;

    @NotNull(message = "Check-out time is required")
    private LocalDateTime checkOutTime;


    // Getters and setters...
}
