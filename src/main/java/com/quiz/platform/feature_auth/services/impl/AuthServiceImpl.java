package com.quiz.platform.feature_auth.services.impl;

import com.quiz.platform.feature_auth.dtos.requests.LoginRequest;
import com.quiz.platform.feature_auth.dtos.requests.RegisterRequest;
import com.quiz.platform.feature_auth.dtos.responses.AuthResponse;
import com.quiz.platform.feature_auth.services.AuthService;
import com.quiz.platform.feature_user.entities.postgres.User;
import com.quiz.platform.feature_user.entities.postgres.UserRole;
import com.quiz.platform.feature_user.services.UserService;
import com.quiz.platform.shared.exception.BadRequestException;
import com.quiz.platform.shared.security.JwtUtil;
import com.quiz.platform.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link AuthService} for authentication operations.
 * Handles user registration and login with JWT token generation.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    /**
     * Registers a new user with the provided details.
     *
     * @param request the registration request containing user details
     * @return authentication response with JWT token
     * @throws BadRequestException if email or username is already registered
     */
    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userService.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered");
        }

        if (userService.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUsername(request.getUsername());
        user.setRole(UserRole.USER);

        User savedUser = userService.saveUser(user);

        UserInfo userInfo = new UserInfo(savedUser);
        String token = jwtUtil.generateToken(userInfo);

        return new AuthResponse(token, "Bearer", savedUser.getEmail(), savedUser.getUsername(), savedUser.getRole().name());
    }

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param request the login request containing credentials
     * @return authentication response with JWT token
     * @throws BadRequestException if credentials are invalid
     */
    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userService.getUserByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        UserInfo userInfo = new UserInfo(user);
        String token = jwtUtil.generateToken(userInfo);

        return new AuthResponse(token, "Bearer", user.getEmail(), user.getUsername(), user.getRole().name());
    }
}
