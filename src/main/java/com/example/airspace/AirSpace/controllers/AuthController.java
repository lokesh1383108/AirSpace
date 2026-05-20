package com.example.airspace.AirSpace.controllers;
import com.example.airspace.AirSpace.models.user.User;
import com.example.airspace.AirSpace.repositories.UserRepository;
import com.example.airspace.AirSpace.service.AuthService;
import com.example.airspace.AirSpace.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.airspace.AirSpace.DTOs.*;
import com.example.airspace.AirSpace.models.CustomOAuth2User;
import com.example.airspace.AirSpace.service.CustomOauthService;
import com.example.airspace.AirSpace.service.OtpService;
import com.example.airspace.AirSpace.security.jwt.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import com.example.airspace.AirSpace.constants.OtpPurpose;
import java.util.*;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private OtpService otpService;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

   

    // Endpoint for user registration
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest)  {
        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            ApiResponse response=ApiResponse.builder()
            .success(false).error("Email is already taken!").build();
            return ResponseEntity.badRequest().body(response);
        }
        authService.saveUserToDB(signUpRequest);
        otpService.sendOtp(signUpRequest.getEmail(), OtpPurpose.REGISTRATION);
        
        return ResponseEntity.ok(ApiResponse.builder().success(true).object("Registration successfull").build());
    }

     // If the user exists and is not verified, send OTP
    @GetMapping("/sendOtpEmailVerify")
    public ResponseEntity<?> getOtp( @RequestBody GetOtp otpRequest) {
        // Check if the user exists in the database
        Optional<User> user = userRepository.findByEmail(otpRequest.getEmail());

        if (user.isEmpty()) {
            // If the user doesn't exist, return a bad request with an error message
            ApiResponse response = ApiResponse.builder()
                    .success(false)
                    .error("Email does not exist")
                    .build();
            return ResponseEntity.badRequest().body(response);
        }

        User existingUser = user.get();

        // Check if the user is already verified
        if (existingUser.isVerified()) {
            // If the user is verified, return a message saying the user is already verified
            ApiResponse response = ApiResponse.builder()
                    .success(false)
                    .error("User is already verified")
                    .build();
            return ResponseEntity.badRequest().body(response);
        }

        // If the user exists and is not verified, send OTP
        otpService.sendOtp(otpRequest.getEmail(), OtpPurpose.EMAIL_VERIFICATION);

        // Return a success response indicating OTP has been sent
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .build();
        return ResponseEntity.ok(response);
    }

    // Endpoint for verifying email with OTP
    @PostMapping("/VerifyYourEmailOtp")
    public ResponseEntity<?> verifyYourEmail(@RequestBody OtpRequest otpRequest){
        boolean isOtpValid = otpService.verifyOtp(otpRequest.getEmail(),otpRequest.getOtp(),otpRequest.getPurpose());
        if(!isOtpValid){
            return ResponseEntity.badRequest().body("Email Verification failed. Invalid Otp");
        }

        Optional<User> user= userRepository.findByEmail(otpRequest.getEmail());
        if(user.isPresent()){
            User existingUser = user.get();
           existingUser.setVerified(true);   //set the verified status true
           userRepository.save(existingUser);   // save the user details in DB
           return ResponseEntity.ok("Email verified successfully.");
        } else {
            return ResponseEntity.badRequest().body("User not found.");
        }
    }

    // Endpoint for user login 
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
//        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(ApiResponse.builder()
        .success(true)
        .object(jwt)
        .build());
    }

    // Endpoint for forgot password
    @GetMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + request.getEmail()));
        boolean isOtpSent = otpService.sendOtp(request.getEmail(), OtpPurpose.FORGOT_PASSWORD);
        if (!isOtpSent) {
            return ResponseEntity.badRequest().body("Error: Unable to send OTP");
        }
        userRepository.findByEmail(request.getEmail());
        ApiResponse response = ApiResponse.builder()
        .success(true)
        .object("OTP sent successfully. Check your registered email.")
        .build();
        
        return ResponseEntity.ok(response);
    }

    // Endpoint for password reset 
    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPassword){
        try {
        String newPassword = resetPassword.getNewPassword();
        String token = resetPassword.getToken();

        if(!tokenProvider.validateTheToken(token,  "FORGOT_PASSWORD")){
            return ResponseEntity.badRequest().body("Invalid or expired password reset token");
        }

        // Update user password logic here
        String email = tokenProvider.extractUsername(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok("Password updated for user. ");

        } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error resetting password: " + e.getMessage());
            }
    }

     // Endpoint for OTP verification
    @PostMapping("/verifyPasswordOtp")
    public ResponseEntity<?> verifyPasswordOtp( @RequestBody OtpRequest otpRequest) {
        String email =otpRequest.getEmail();
        String otp = otpRequest.getOtp();

        boolean isOtpValid = otpService.verifyOtp(email, otp, otpRequest.getPurpose());
        if (!isOtpValid) {
            return new ResponseEntity<>("Invalid OTP", HttpStatus.UNAUTHORIZED);
        }
        String token = tokenProvider.generateTokenForUser(email,otpRequest.getPurpose());
        ApiResponse response = ApiResponse.builder()
        .success(true)
        .object(Map.of(
            "token",token,"message","OTP verified. You can reset your password"

        )).error(null).build();

        return ResponseEntity.ok(response);
    }}

    

//     // Endpoint for OAuth2 login
//     @GetMapping("/oauth2/login")
//     public ResponseEntity<?> oauth2Login(@RequestParam("code") String authorizationCode,
//                                          @RequestParam("registrationId") String registrationId) {
//         try {
//             // Retrieve ClientRegistration based on registrationId (e.g., "google" or "github")
//             ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(registrationId);

//             // Obtain access token using authorization code
//             OAuth2AccessToken accessToken = customOauthService.getAccessToken(authorizationCode, clientRegistration);

//             // Create OAuth2UserRequest with the obtained access token
//             OAuth2UserRequest userRequest = new OAuth2UserRequest(clientRegistration, accessToken);

//             // Load user details using custom OAuth2 service
//             OAuth2User oauthUser = customOauthService.loadUser(userRequest);

//             // Generate JWT for the authenticated user
//             String jwt = tokenProvider.generateTokenForUser(((CustomOAuth2User) oauthUser).getUser());
//             return ResponseEntity.ok(new AuthResponse(jwt));

//         } catch (OAuth2AuthenticationException ex) {
//             return ResponseEntity.badRequest().body("OAuth2 authentication failed: " + ex.getMessage());
//         }
//     }
// }

