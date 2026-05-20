package com.example.airspace.AirSpace.constants;


public enum emailTemplates {

    WELCOME_EMAIL("user_welcome.html", "Welcome to AirSpace"),
    OTP_EMAIL("otp_verification.html", "Your Verification Code"),
    BOOKING_CONFIRMATION("booking-confirmation.html", "Booking Confirmed"),
    BOOKING_CANCELLATION("booking-cancellation.html", "Booking Cancelled"),
    PASSWORD_RESET("password-reset.html", "Reset Your Password");
    
    private final String templateFile;
    private final String defaultSubject;
    
    emailTemplates(String templateFile, String defaultSubject) {
        this.templateFile = templateFile;
        this.defaultSubject = defaultSubject;
    } 

    public String getTemplateFile() {
        return templateFile;
    }

    public String getDefaultSubject() {
        return defaultSubject;
    }
    
}

