package com.example.airspace.AirSpace.DTOs;


import com.example.airspace.AirSpace.constants.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OtpRequest {
    @NotNull
    private String email;

    @NotNull
    private String otp;

    @NotNull
    OtpPurpose purpose;
}
