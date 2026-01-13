package com.example.bankcards.exception;

public class InvalidRequestException extends CustomException {
    public InvalidRequestException(String message) {
        super(message, "INVALID_REQUEST");
    }
}