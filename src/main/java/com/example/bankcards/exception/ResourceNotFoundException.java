package com.example.bankcards.exception;

public class ResourceNotFoundException extends CustomException {
    public ResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND");
    }
}