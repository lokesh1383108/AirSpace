package com.example.airspace.AirSpace.models.user;

import com.example.airspace.AirSpace.models.Property;
import com.example.airspace.AirSpace.models.user.User;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "customers")
@Getter
@Setter
public class Customer   {
    @Id
    @Setter(AccessLevel.NONE)
    private ObjectId id;
    @NotNull
    private ObjectId userId;
    private List<String> preference;

    private List<ObjectId> lastVisitedProperties;

}
