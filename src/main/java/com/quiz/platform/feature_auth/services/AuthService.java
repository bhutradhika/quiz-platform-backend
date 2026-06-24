package com.quiz.platform.feature_auth.services;

import com.quiz.platform.feature_auth.dtos.requests.LoginRequest;
import com.quiz.platform.feature_auth.dtos.requests.RegisterRequest;
import com.quiz.platform.feature_auth.dtos.responses.AuthResponse;

/**
 * Service interface for authentication operations.
 * Provides business logic for user registration and login.
 */
public interface AuthService {

    /**
     * Registers a new user account.
     *
     * @param request the registration request
     * @return authentication response with JWT token
     * @throws com.quiz.platform.shared.exception.BadRequestException if email or username already exists
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param request the login request
     * @return authentication response with JWT token
     * @throws com.quiz.platform.shared.exception.BadRequestException if credentials are invalid
     */
    AuthResponse login(LoginRequest request);
}
