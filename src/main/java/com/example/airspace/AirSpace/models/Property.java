package com.example.airspace.AirSpace.models;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "properties")
@Getter
@Setter
public class Property {

    @Id
    private ObjectId id;

    @NotNull(message = "Host is required")
//    @DBRef(lazy = true)
    private ObjectId hostId;

    @NotNull(message = "Property name is required")
    private String name;

    private boolean isAvailable;
    private int limit;

    private double rating;

    @NotNull(message = "Price is required")
    private double price;

    private int totalVisitors;

    private int availableDays;


    private List<String> imagesId; // List of image IDs

    private String schedule; // Future booking / limit

    @NotNull(message = "About Us section is required")
    private String aboutUs;

//    @DBRef(lazy = true)
    private List<ObjectId> history;

//    @DBRef(lazy = true)
    private ObjectId currentTransactionId; // Reference to current running transaction

    @NotNull
    private List<Rooms> rooms;

    // Embedded Amenities
    private List<Amenity> amenities;

    private int TotalAvailableBeds;

    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
    public static class Rooms{
        private BedType bedType; // Single, Double, etc.
        private RoomType roomType;  //Single , Double, Triple, Dormatory
        private int vacantBeds;
        private boolean isShared;
        private int maxOccupancy;
    }
//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
    public static enum BedType{
        SingleBed,
        DoubleBed,
        BunkBed
    }

    public static enum RoomType{
        Standard,
        TwinSharing,
        TripleSharing,
        QuadSharing,
        Dormitory
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Amenity {
        private String name; // Example: "Wi-Fi", "Parking", "Gym"
    }



}
