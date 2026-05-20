package com.example.airspace.AirSpace.controllers;

import com.example.airspace.AirSpace.models.Property;
import com.example.airspace.AirSpace.models.transaction.History;
import com.example.airspace.AirSpace.service.HostService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/host")
public class HostController {

    @Autowired
    private HostService hostService;

    @PutMapping("/update/{propertyId}")
    public ResponseEntity<String> updateProperty(
            @PathVariable ObjectId propertyId,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) String aboutUs,
            @RequestParam(required = false) Boolean isAvailable,
            @RequestParam(required = false) Integer limit) {

        boolean isUpdated = hostService.updateProperty(propertyId, price, aboutUs, isAvailable, limit);

        if (isUpdated) {
            return ResponseEntity.ok("Property updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Property not found.");
        }
    }


    // Read a property by its ID
    @GetMapping("/read/{propertyId}")
    public ResponseEntity<Property> readProperty(@PathVariable ObjectId propertyId) {
        Property property = hostService.readProperty(propertyId);
        if (property != null) {
            return ResponseEntity.ok(property);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get all history entries for a specific property
    @GetMapping("/history/{propertyId}")
    public ResponseEntity<Object> getHistory(@PathVariable ObjectId propertyId) {
        List<History> historyList = hostService.getHistory(propertyId);
        return ResponseEntity.ok(historyList);
    }

    // Get all properties owned by a specific host
    @PostMapping("/getAllByHostId")
    public ResponseEntity<List<Property>> getAllPropertiesByHostId(@RequestParam ObjectId hostId) {
        List<Property> properties = hostService.getAllPropertyByHostId(hostId);
        return ResponseEntity.ok(properties);
    }

//    // Get all properties
//    @GetMapping("/getAll")
//    public ResponseEntity<List<Property>> getAllProperties() {
//        List<Property> properties = hostService.getAllProperties();
//        return ResponseEntity.ok(properties);
//    }
}
