package com.example.airspace.AirSpace.service;

import com.example.airspace.AirSpace.models.transaction.History;
import com.example.airspace.AirSpace.models.Property;
import com.example.airspace.AirSpace.repositories.HistoryRepository;
import com.example.airspace.AirSpace.repositories.PropertyRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class HostService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private HistoryRepository historyRepository;


    public boolean updateProperty(ObjectId propertyId, Double price, String aboutUs, Boolean isAvailable, Integer limit) {
        Optional<Property> propertyOpt = propertyRepository.findById(propertyId);
        if (propertyOpt.isPresent()) {
            Property property = propertyOpt.get();
            if (price != null) {
                property.setPrice(price);
            }
            if (aboutUs != null) {
                property.setAboutUs(aboutUs);
            }
            if (isAvailable != null) {
                property.setAvailable(isAvailable);
            }
            if (limit != null) {
                property.setLimit(limit);
            }

            propertyRepository.save(property);
            return true;
        }else{
            return false;
        }
    }

    // Read a property by its ID
    public Property readProperty(ObjectId propertyId) {
        return propertyRepository.findById(propertyId).orElse(null);
    }

    // Get all history entries for a specific host
    public List<History> getAllHistory(ObjectId hostId) {
        // Fetch all properties by hostId
        List<Property> properties = propertyRepository.findAllByHostId(hostId);

        // Create a list to store all histories
        List<History> allHistories = new ArrayList<>();

        // Iterate over each property and collect histories
        for (Property property : properties) {
                List<History> historyList=historyRepository.findByPropertyId(property.getId());
            if (historyList != null) {
                allHistories.addAll(historyList);
            }
        }

        // Sort all histories by timestamp (assuming History has a getTimestamp method)
        allHistories.sort(Comparator.comparing(History::getTimestamp));

        return allHistories;
    }

    // Get history for a specific property by property ID
    public List<History> getHistory(ObjectId propertyId) {
        return historyRepository.findByPropertyId(propertyId);
    }

    // Get all properties owned by a specific host
    public List<Property> getAllPropertyByHostId(ObjectId hostId) {
        return propertyRepository.findByHostId(hostId);
    }

    // Get all properties //for our need
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }
}
