package com.userservice.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends UserServiceException {
    public ValidationException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), "VALIDATION_ERROR", message);
    }
} 