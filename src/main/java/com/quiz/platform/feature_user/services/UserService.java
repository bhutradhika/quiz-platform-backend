package com.quiz.platform.feature_user.services;

import com.quiz.platform.feature_user.dtos.responses.UserResponse;
import com.quiz.platform.feature_user.entities.postgres.User;
import java.util.Optional;

/**
 * Service interface for user operations.
 * Provides business logic for user management.
 */
public interface UserService {

    /**
     * Retrieves a user by their ID.
     *
     * @param userId the ID of the user
     * @return the user entity
     * @throws com.quiz.platform.shared.exception.ResourceNotFoundException if user not found
     */
    User getUserById(String userId);

    /**
     * Retrieves the current user's information.
     *
     * @param userId the ID of the user
     * @return the user response DTO
     */
    UserResponse getCurrentUser(String userId);

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address
     * @return the user if found
     */
    Optional<User> getUserByEmail(String email);

    /**
     * Checks if a user exists by email.
     *
     * @param email the email address
     * @return true if exists
     */
    boolean existsByEmail(String email);

    /**
     * Checks if a user exists by username.
     *
     * @param username the username
     * @return true if exists
     */
    boolean existsByUsername(String username);

    /**
     * Saves a user entity.
     *
     * @param user the user to save
     * @return the saved user
     */
    User saveUser(User user);
}
