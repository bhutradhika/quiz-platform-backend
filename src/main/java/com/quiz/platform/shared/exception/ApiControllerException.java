package com.quiz.platform.shared.exception;

import lombok.Getter;

@Getter
public class ApiControllerException extends RuntimeException {
    private String errorCode;

    public ApiControllerException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ApiControllerException(String message) {
        super(message);
    }
}
