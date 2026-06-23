package com.quiz.platform.shared.exception;

public class BadRequestException extends ApiControllerException {
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, String errorCode) {
        super(message, errorCode);
    }
}
