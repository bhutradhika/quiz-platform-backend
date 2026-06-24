package com.quiz.platform.feature_quiz.daos;

import com.quiz.platform.feature_quiz.entities.postgres.Quiz;
import com.quiz.platform.feature_quiz.entities.postgres.QuizCategory;
import com.quiz.platform.feature_quiz.entities.postgres.QuizLevel;
import com.quiz.platform.feature_quiz.repositories.QuizRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * Data Access Object for Quiz entities.
 * Provides an abstraction layer over the QuizRepository.
 */
@Repository
@RequiredArgsConstructor
public class QuizDao {

    private final QuizRepository quizRepository;

    /**
     * Finds a quiz by its ID.
     *
     * @param quizId the ID of the quiz
     * @return the quiz if found
     */
    public Optional<Quiz> findById(String quizId) {
        return quizRepository.findById(quizId);
    }

    /**
     * Finds all public quizzes with pagination.
     *
     * @param pageable pagination parameters
     * @return paginated list of public quizzes
     */
    public Page<Quiz> findByIsPublicTrue(Pageable pageable) {
        return quizRepository.findByIsPublicTrue(pageable);
    }

    /**
     * Finds public quizzes by category with pagination.
     *
     * @param category the category to filter by
     * @param pageable pagination parameters
     * @return paginated list of public quizzes in the category
     */
    public Page<Quiz> findByIsPublicTrueAndCategory(QuizCategory category, Pageable pageable) {
        return quizRepository.findByIsPublicTrueAndCategory(category, pageable);
    }

    /**
     * Finds quizzes by category and level with pagination.
     *
     * @param category optional category filter
     * @param level optional level filter
     * @param pageable pagination parameters
     * @return paginated list of quizzes matching the filters
     */
    public Page<Quiz> findByCategoryAndLevel(QuizCategory category, QuizLevel level, Pageable pageable) {
        if (category != null && level != null) {
            return quizRepository.findByCategoryAndLevelAndIsPublicTrue(category, level, pageable);
        } else if (category != null) {
            return quizRepository.findByIsPublicTrueAndCategory(category, pageable);
        } else if (level != null) {
            return quizRepository.findByLevelAndIsPublicTrue(level, pageable);
        }
        return quizRepository.findByIsPublicTrue(pageable);
    }

    /**
     * Saves a quiz entity.
     *
     * @param quiz the quiz to save
     * @return the saved quiz
     */
    public Quiz save(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    /**
     * Deletes a quiz entity.
     *
     * @param quiz the quiz to delete
     */
    public void delete(Quiz quiz) {
        quizRepository.delete(quiz);
    }

    /**
     * Counts all public quizzes.
     *
     * @return count of public quizzes
     */
    public Long countByIsPublicTrue() {
        return quizRepository.countByIsPublicTrue();
    }

    /**
     * Counts quizzes by category and level.
     *
     * @param category the category
     * @param level the level
     * @return count of matching quizzes
     */
    public long countByCategoryAndLevelAndIsPublicTrue(QuizCategory category, QuizLevel level) {
        return quizRepository.countByCategoryAndLevelAndIsPublicTrue(category, level);
    }

    /**
     * Counts all quizzes.
     *
     * @return total count of quizzes
     */
    public long count() {
        return quizRepository.count();
    }
}
