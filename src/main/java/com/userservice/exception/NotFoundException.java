package com.userservice.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends UserServiceException {
    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND.value(), "NOT_FOUND", message);
    }
} 