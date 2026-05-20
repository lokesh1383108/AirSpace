package com.example.airspace.AirSpace.models;

import lombok.Data;
import com.example.airspace.AirSpace.constants.*;

@Data
public class OtpRecord {
    private String otp;
        private long timestamp;
        OtpPurpose otpPurpose;

        public OtpRecord(String otp, long timestamp) {
            this.otp = otp;
            this.timestamp = timestamp;
        }


        
    }

    
    

