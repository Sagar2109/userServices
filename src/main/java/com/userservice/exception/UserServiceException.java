package com.userservice.exception;

public class UserServiceException extends RuntimeException {
    private final int statusCode;
    private final String errorCode;
    private final String message;

    public UserServiceException(int statusCode, String errorCode, String message) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
} 