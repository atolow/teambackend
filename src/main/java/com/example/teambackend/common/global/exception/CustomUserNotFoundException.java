package com.example.teambackend.common.global.exception;

public class CustomUserNotFoundException extends RuntimeException {
    public CustomUserNotFoundException(String message) {
        super(message);
    }
}
