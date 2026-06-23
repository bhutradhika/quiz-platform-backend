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

@RestController
@RequestMapping("/api/public/auth")
@Validated
@RequiredArgsConstructor
public class AuthPublicController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
        ApiResponse response = new ApiResponse();
        response.setData(authService.register(request));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        ApiResponse response = new ApiResponse();
        response.setData(authService.login(request));
        return ResponseEntity.ok(response);
    }
}
