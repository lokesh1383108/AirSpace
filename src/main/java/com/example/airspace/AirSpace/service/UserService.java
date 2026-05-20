package com.example.airspace.AirSpace.service;

import com.example.airspace.AirSpace.models.transaction.History;
import com.example.airspace.AirSpace.models.Property;
import com.example.airspace.AirSpace.models.user.Gender;
import com.example.airspace.AirSpace.models.user.User;
import com.example.airspace.AirSpace.repositories.HistoryRepository;
import com.example.airspace.AirSpace.repositories.PropertyRepository;
import com.example.airspace.AirSpace.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    


    public boolean updateProfile(ObjectId userId, String email, String password, String phoneNumber, Gender gender, LocalDateTime updatedAt, LocalDateTime lastLogin, String fcmToken, String profilePicture) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (email != null) {
                user.setEmail(email);
            }
            if (password != null) {
                user.setPassword(password);
            }
            if (phoneNumber != null) {
                user.setPhoneNumber(phoneNumber);
            }
            if (gender != null) {
                user.setGender(gender);
            }
            userRepository.save(user);
            return true;
        }else{
            return false;
        }
    }

    // Read a property by its ID

}
