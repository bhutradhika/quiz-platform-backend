package com.quiz.platform.feature_auth.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Email is required") private String email;

    @NotBlank(message = "Password is required") private String password;
}
