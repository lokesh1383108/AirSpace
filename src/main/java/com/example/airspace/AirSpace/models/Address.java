package com.example.airspace.AirSpace.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "addresses")
@Getter
@Setter
public class Address {

    @NotBlank(message = "Street is required.")
    private String street;

    @NotBlank(message = "City is required.")
    private String city;

    @NotBlank(message = "State is required.")
    private String state;

    @NotBlank(message = "Country is required.")
    private String country;

    @Pattern(regexp = "\\d{6}", message = "Zip code must be a 6-digit number.")
    private String zipCode;

    @GeoSpatialIndexed
    private double[] location; // [longitude, latitude]

    public Address() {}

    public Address(String street, String city, String state, String country, String zipCode, double longitude, double latitude) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipCode = zipCode;
        this.location = new double[]{longitude, latitude};
    }

    public void setLocation(double longitude, double latitude) {
        this.location = new double[]{longitude, latitude};
    }
}
