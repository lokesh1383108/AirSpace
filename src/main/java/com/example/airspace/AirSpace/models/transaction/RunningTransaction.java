package com.example.airspace.AirSpace.models.transaction;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "runningTransactions")
@Getter
@Setter
public class RunningTransaction {

    @Id
    private ObjectId id;

    @NotNull(message = "User ID is required")
    private ObjectId userId;

    @NotNull(message = "Property ID is required")
    private ObjectId propertyId;

    @NotNull(message = "Payment status is required")
    private String paymentStatus;

    private LocalDateTime timestamp;

}
