package com.quiz.platform.shared.security;

import com.quiz.platform.feature_user.entities.postgres.UserRole;
import lombok.Data;

@Data
public class TokenPayload {
    private String userId;
    private String email;
    private String username;
    private UserRole role;

    public TokenPayload() {}

    public TokenPayload(String userId, String email, String username, UserRole role) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.role = role;
    }
}
