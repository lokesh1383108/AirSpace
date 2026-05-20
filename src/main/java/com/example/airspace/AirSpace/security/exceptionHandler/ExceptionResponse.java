package com.example.airspace.AirSpace.security.exceptionHandler;

import java.time.ZonedDateTime;

public class ExceptionResponse {

    private String message;
    private int code;
    private ZonedDateTime timestamp;

    public ExceptionResponse(String message, int code, ZonedDateTime timestamp) {
        this.message = message;
        this.code = code;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }
}

