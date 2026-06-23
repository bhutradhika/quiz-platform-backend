package com.quiz.platform.feature_auth.services;

import com.quiz.platform.feature_auth.dtos.requests.LoginRequest;
import com.quiz.platform.feature_auth.dtos.requests.RegisterRequest;
import com.quiz.platform.feature_auth.dtos.responses.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
