package com.example.airspace.AirSpace.DTOs;

import lombok.*;

@Getter
@Setter
@Builder
public class ApiResponse {
    private boolean success;
    private Object object;    // We can provide any response message as per requirement
    private String error;

}
