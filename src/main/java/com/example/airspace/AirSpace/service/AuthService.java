package com.example.airspace.AirSpace.service;
import com.example.airspace.AirSpace.DTOs.SignUpRequest;
import com.example.airspace.AirSpace.constants.emailTemplates;
import com.example.airspace.AirSpace.models.user.User;
import com.example.airspace.AirSpace.models.user.UserType;
import com.example.airspace.AirSpace.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    
    private final Map<String, String> otpStorage = new HashMap<>();

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public boolean sendOtp(String email){
        String otpValue = generateOtp();
        otpStorage.put(email,otpValue);
        Map<String, Object> variables = Map.of(
            "otp", otpValue,
            "expiryMinutes", 5
        );
        return emailService.sendTemplatedEmail(email, "otp",emailTemplates.OTP_EMAIL,variables 
                                     );
    }

    public String generateOtp(){
        return String.format("%06d", new Random().nextInt(999999));
    }

    public boolean verifyOtpAndSignup(String email, String otp){
        if(otp.equals(otpStorage.get(email))){
            otpStorage.remove(email); // Remove OTP after successful signup
            return true;
        }
        return false;
    }




//    public String signup(User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        userRepository.save(user);
//        return "Signup successful! Please log in.";

    public void saveUserToDB(SignUpRequest signUpRequest) {
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setPhoneNumber(signUpRequest.getPhoneNumber());
        user.setGender(signUpRequest.getGender());
        user.setRole(UserType.valueOf(signUpRequest.getRole()));

        // Set timestamps automatically by Spring Data MongoDB
        user.prepareForSave();  // This should handle createdAt, updatedAt, and lastLogin

        // Save the user to MongoDB
        userRepository.save(user);

    }

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public void updateUser(User user) {
        userRepository.save(user); // Save updated user
    }


//    public String signin(String name, String password) {
//        User user = userRepository.findByname(name);
//        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
//            return "Login successful! Redirecting to homepage.";
//        } else {
//            return "Invalid username or password.";
//        }
//    }

}
