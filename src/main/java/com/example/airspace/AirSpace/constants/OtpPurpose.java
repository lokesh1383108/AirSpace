package com.example.airspace.AirSpace.constants;

import lombok.Getter;

@Getter
public enum OtpPurpose {
        REGISTRATION("Complete your registration"),
    FORGOT_PASSWORD("Reset your password"),
    EMAIL_VERIFICATION("Verify your email address"),
    TWO_FACTOR_AUTH("Two-factor authentication");

    private final String otpDescription;

    OtpPurpose (String otpDescription){
        this.otpDescription=otpDescription;
    }



    }
