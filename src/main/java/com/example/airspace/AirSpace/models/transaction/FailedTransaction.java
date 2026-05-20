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
//@Document(collection = "failedTransactions")
//public class FailedTransaction {
//
//    @Id
//    private String id;
//
//    @NotNull(message = "User ID is required")
//    private String userId;
//
//    @NotNull(message = "Failed By is required")
//    private String failedBy;
//
//    private LocalDateTime timestamp;
//
//    @NotNull(message = "Failed Reason is required")
//    private String failedReason;
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
//    public String getFailedBy() {
//        return failedBy;
//    }
//
//    public void setFailedBy(String failedBy) {
//        this.failedBy = failedBy;
//    }
//
//    public LocalDateTime getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(LocalDateTime timestamp) {
//        this.timestamp = timestamp;
//    }
//
//    public String getFailedReason() {
//        return failedReason;
//    }
//
//    public void setFailedReason(String failedReason) {
//        this.failedReason = failedReason;
//    }
//}
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class FailedTransaction extends History {

    @NotNull(message = "Failed reason is required")
    private String failedReason;

    @NotNull(message = "Failed by (host or system) is required")
    private String failedBy;

}
