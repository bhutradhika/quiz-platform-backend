package com.quiz.platform.shared.exception;

public class ResourceNotFoundException extends ApiControllerException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
    }

    public ResourceNotFoundException(String message, String errorCode) {
        super(message, errorCode);
    }
}
