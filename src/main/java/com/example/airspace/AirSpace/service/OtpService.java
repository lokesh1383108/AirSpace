package com.example.airspace.AirSpace.service;


import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.airspace.AirSpace.models.OtpRecord;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import com.example.airspace.AirSpace.constants.*;

@Service
public class OtpService {

    @Autowired
    private JavaMailSender mailSender;

    
    private final EmailService emailService;

    public OtpService (EmailService emailService){
        this.emailService = emailService;
    }


    private final Logger logger =  LoggerFactory.getLogger(OtpService.class);
    @Value("${spring.mail.username}") private String sender;

    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRATION_TIME = 5;

    private final ConcurrentHashMap<String, OtpRecord> otpCache = new ConcurrentHashMap<>();

    public boolean sendOtp(String email, OtpPurpose purpose) {
        String otp = generateOtp();
        String buildSubject = purpose.getOtpDescription();
Map<String, Object> variables = Map.of(
                "userName", email,
            "otp", otp,
            "purpose",purpose.getOtpDescription(),
            "expiryMinutes", 5                // it is hard coded we will change it 
        );

        // boolean isSent= sendEmailOtp(email, otp, purpose);
        boolean isSent= emailService.sendTemplatedEmail(email, buildSubject ,emailTemplates.OTP_EMAIL,variables 
                                     );
        if(isSent){
                otpCache.put(email+" "+purpose.name(), new OtpRecord(otp, System.currentTimeMillis()));
        }
        return  isSent;
    }

    public boolean verifyOtp(String email, String otp, OtpPurpose purpose) {
        String key = email+" "+purpose.name();
        OtpRecord otpRecord = otpCache.get(key);

        if (otpRecord == null) {
            return false;
        }

        long elapsedTime = System.currentTimeMillis() - otpRecord.getTimestamp();
        if (elapsedTime > TimeUnit.MINUTES.toMillis(OTP_EXPIRATION_TIME)) {
            otpCache.remove(email);
            return false;
        }

        return otpRecord.getOtp().equals(otp);
    }

    private String generateOtp() {
        // Generate a random OTP of specified length
        Random random = new Random();
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));  // Random digit
        }
        return otp.toString();
    }

    private boolean sendEmailOtp(String email, String otp,OtpPurpose purpose) {
        try{
            MimeMessage Message = mailSender.createMimeMessage();
            Message.setFrom(sender);
            Message.setRecipients(MimeMessage.RecipientType.TO,email);
            Message.setSubject("Email from AirSpace : "+purpose);
            Message.setText("Your Verification OTP is "+otp);


            // Sending the mail
            mailSender.send(Message);
            System.out.println("Sending OTP " + otp+" to "+email);
            return true;
        }// Catch block to handle the exceptions
        catch (Exception e) {
            logger.error("Exception Happened while sending Mail: "+e.getMessage()+e);
            System.out.println( "Error while Sending Mail"+e);
            return false;
        }

    }
   
}
