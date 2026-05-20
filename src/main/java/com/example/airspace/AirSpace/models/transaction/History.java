package com.example.airspace.AirSpace.models.transaction;//package com.example.airspace.AirSpace.models;
//
//import jakarta.validation.constraints.NotNull;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;
//
//import java.util.List;
//
//@Document(collection = "history")
//public class History {
//
//    @Id
//    private String id;
//
//    @NotNull(message = "Property ID is required")
//    private String propertyId;
//
//    private List<String> failedTransactionIds;
//    private List<String> completedTransactionIds;
//
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getPropertyId() {
//        return propertyId;
//    }
//
//    public void setPropertyId(String propertyId) {
//        this.propertyId = propertyId;
//    }
//
//
//    public List<String> getFailedTransactionIds() {
//        return failedTransactionIds;
//    }
//
//    public void setFailedTransactionIds(List<String> failedTransactionIds) {
//        this.failedTransactionIds = failedTransactionIds;
//    }
//
//    public List<String> getCompletedTransactionIds() {
//        return completedTransactionIds;
//    }
//
//    public void setCompletedTransactionIds(List<String> completedTransactionIds) {
//        this.completedTransactionIds = completedTransactionIds;
//    }
//}

import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Document(collection = "history")
@Getter
public abstract class History {

    @Id
    private ObjectId id;

    @NotNull(message = "Property ID is required")
    private ObjectId propertyId;

    @NotNull(message = "Customer ID is required")
    private ObjectId customerId;

    @NotNull(message = "Timestamp is required")
    private LocalDateTime timestamp;

}
