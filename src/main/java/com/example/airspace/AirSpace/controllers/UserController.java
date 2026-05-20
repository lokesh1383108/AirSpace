package com.example.airspace.AirSpace.controllers;

import com.example.airspace.AirSpace.models.user.User;
import com.example.airspace.AirSpace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestParam String customerId) {
        return ResponseEntity.ok("hi");
    }

    // Endpoint to get current user details
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(user);
    }
}
