package com.userservice.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends UserServiceException {
    public EmailAlreadyExistsException(String message) {
        super(HttpStatus.CONFLICT.value(), "EMAIL_ALREADY_EXISTS", message);
    }
} 