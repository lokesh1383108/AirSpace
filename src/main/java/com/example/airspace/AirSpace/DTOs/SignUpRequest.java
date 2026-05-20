package com.example.airspace.AirSpace.DTOs;


import com.example.airspace.AirSpace.models.user.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignUpRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private Gender gender;

    @NotBlank
    @Pattern(regexp = "^\\+?[0-9]*$")
    private String phoneNumber;

    @NotBlank
    private String role;

}


