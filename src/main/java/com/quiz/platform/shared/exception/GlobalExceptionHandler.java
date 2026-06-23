package com.quiz.platform.shared.exception;

import com.quiz.platform.shared.dtos.ApiResponse;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiControllerException.class)
    public ResponseEntity<ApiResponse> handleApiControllerException(ApiControllerException ex) {
        log.error("Error occurred: ", ex);

        ApiResponse response = new ApiResponse();
        response.setSuccess(false);
        response.setMessage(ex.getMessage());
        response.setErrorCode(ex.getErrorCode());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException ex) {
        log.error("Access denied: ", ex);

        HttpStatus status = HttpStatus.FORBIDDEN;
        if (ex instanceof AuthorizationDeniedException) {
            status = HttpStatus.NOT_ACCEPTABLE;
        }

        ApiResponse response = new ApiResponse();
        response.setSuccess(false);
        response.setMessage(ex.getMessage());
        response.setErrorCode("ACCESS_DENIED");

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiResponse response = new ApiResponse();
        response.setSuccess(false);
        response.setMessage("Validation failed");
        response.setErrorCode("VALIDATION_ERROR");
        response.setData(errors);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error occurred: ", ex);

        ApiResponse response = new ApiResponse();
        response.setSuccess(false);
        response.setMessage("Something went wrong");
        response.setErrorCode("500");

        return ResponseEntity.internalServerError().body(response);
    }
}
