package com.quiz.platform.feature_user.services;

import com.quiz.platform.feature_user.dtos.responses.UserResponse;
import com.quiz.platform.feature_user.entities.postgres.User;
import java.util.Optional;

public interface UserService {

    User getUserById(String userId);

    UserResponse getCurrentUser(String userId);

    Optional<User> getUserByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    User saveUser(User user);
}
