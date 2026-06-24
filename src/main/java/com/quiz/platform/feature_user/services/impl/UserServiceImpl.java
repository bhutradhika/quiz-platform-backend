package com.quiz.platform.feature_user.services.impl;

import com.quiz.platform.feature_user.daos.UserDao;
import com.quiz.platform.feature_user.dtos.responses.UserResponse;
import com.quiz.platform.feature_user.entities.postgres.User;
import com.quiz.platform.feature_user.services.UserService;
import com.quiz.platform.shared.exception.ResourceNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link UserService} for user operations.
 * Handles business logic for user management.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Override
    public User getUserById(String userId) {
        return userDao.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    @Override
    public UserResponse getCurrentUser(String userId) {
        User user = getUserById(userId);
        return new UserResponse(user.getId(), user.getEmail(), user.getUsername(), user.getRole().name());
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userDao.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userDao.existsByUsername(username);
    }

    @Override
    public User saveUser(User user) {
        return userDao.save(user);
    }
}
