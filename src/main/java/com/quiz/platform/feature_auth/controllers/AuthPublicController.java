package com.quiz.platform.feature_auth.controllers;

import com.quiz.platform.feature_auth.dtos.requests.LoginRequest;
import com.quiz.platform.feature_auth.dtos.requests.RegisterRequest;
import com.quiz.platform.feature_auth.services.AuthService;
import com.quiz.platform.shared.dtos.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for public authentication operations.
 * Provides endpoints for user registration and login.
 */
@RestController
@RequestMapping("/api/public/auth")
@Validated
@RequiredArgsConstructor
public class AuthPublicController {

    private final AuthService authService;

    /**
     * Registers a new user account.
     *
     * @param request the registration request
     * @return ResponseEntity containing authentication token and user info
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
        ApiResponse response = new ApiResponse();
        response.setData(authService.register(request));
        return ResponseEntity.ok(response);
    }

    /**
     * Authenticates a user and returns a JWT token.
     *
     * @param request the login request
     * @return ResponseEntity containing authentication token and user info
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        ApiResponse response = new ApiResponse();
        response.setData(authService.login(request));
        return ResponseEntity.ok(response);
    }
}
