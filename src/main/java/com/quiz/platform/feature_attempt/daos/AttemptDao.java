package com.quiz.platform.feature_attempt.daos;

import com.quiz.platform.feature_attempt.entities.postgres.Attempt;
import com.quiz.platform.feature_attempt.repositories.AttemptRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * Data Access Object for Attempt entities.
 * Provides an abstraction layer over the AttemptRepository.
 */
@Repository
@RequiredArgsConstructor
public class AttemptDao {

    private final AttemptRepository attemptRepository;

    /**
     * Saves an attempt entity.
     *
     * @param attempt the attempt to save
     * @return the saved attempt
     */
    public Attempt save(Attempt attempt) {
        return attemptRepository.save(attempt);
    }

    /**
     * Checks if an incomplete attempt exists for a user and quiz.
     *
     * @param userId the ID of the user
     * @param quizId the ID of the quiz
     * @return true if an incomplete attempt exists
     */
    public boolean existsByUserIdAndQuizIdAndCompletedAtIsNull(String userId, String quizId) {
        return attemptRepository.existsByUserIdAndQuizIdAndCompletedAtIsNull(userId, quizId);
    }

    /**
     * Finds all attempts for a user and quiz.
     *
     * @param userId the ID of the user
     * @param quizId the ID of the quiz
     * @return list of attempts
     */
    public List<Attempt> findByUserIdAndQuizId(String userId, String quizId) {
        return attemptRepository.findByUserIdAndQuizId(userId, quizId);
    }

    /**
     * Finds an attempt by ID and user ID.
     *
     * @param attemptId the ID of the attempt
     * @param userId the ID of the user
     * @return the attempt if found
     */
    public Optional<Attempt> findByIdAndUserId(String attemptId, String userId) {
        return attemptRepository.findByIdAndUserId(attemptId, userId);
    }

    /**
     * Finds all attempts for a user with pagination.
     *
     * @param userId the ID of the user
     * @param pageable pagination parameters
     * @return paginated list of attempts
     */
    public Page<Attempt> findByUserId(String userId, Pageable pageable) {
        return attemptRepository.findByUserId(userId, pageable);
    }

    /**
     * Finds top leaderboard entries for a specific quiz.
     *
     * @param quizId the ID of the quiz
     * @param pageable pagination parameters for limiting results
     * @return list of top attempts sorted by score
     */
    public List<Attempt> findLeaderboardByQuizId(String quizId, Pageable pageable) {
        return attemptRepository.findLeaderboardByQuizId(quizId, pageable);
    }

    /**
     * Finds all completed attempts for a user.
     *
     * @param userId the ID of the user
     * @return list of completed attempts
     */
    public List<Attempt> findByUserIdAndCompletedAtIsNotNull(String userId) {
        return attemptRepository.findByUserIdAndCompletedAtIsNotNull(userId);
    }

    /**
     * Finds global leaderboard entries with optional filters.
     *
     * @param category optional category filter
     * @param level optional level filter
     * @param pageable pagination parameters for limiting results
     * @return list of top attempts sorted by score
     */
    public List<Attempt> findGlobalLeaderboard(String category, String level, Pageable pageable) {
        return attemptRepository.findGlobalLeaderboard(category, level, pageable);
    }

    /**
     * Counts completed attempts for a user.
     *
     * @param userId the ID of the user
     * @return count of completed attempts
     */
    public Long countCompletedByUserId(String userId) {
        return attemptRepository.countCompletedByUserId(userId);
    }
}
