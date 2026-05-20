package com.example.airspace.AirSpace.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ratings")
@Getter
@Setter
public class Ratings {

    @Id
    @Setter(AccessLevel.NONE)
    private ObjectId id;

    @NotNull(message = "User ID is required")
    private ObjectId userId;

    private String experience;

    @NotNull(message = "Property ID  is required")
    private ObjectId propertyId;

    @NotNull(message = "Host ID is required")
    private ObjectId hostId;

    @Min(1)
    @Max(5)
    private int hostRating;

    @Min(1)
    @Max(5)
    private int propertyRating;
}