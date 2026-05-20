package com.example.airspace.AirSpace.DTOs;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
public class LoginRequest {
    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;
}

