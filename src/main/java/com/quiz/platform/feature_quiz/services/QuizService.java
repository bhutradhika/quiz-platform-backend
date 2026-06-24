package com.quiz.platform.feature_quiz.services;

import com.quiz.platform.feature_quiz.dtos.requests.QuestionRequest;
import com.quiz.platform.feature_quiz.dtos.requests.QuizRequest;
import com.quiz.platform.feature_quiz.dtos.responses.AdminStats;
import com.quiz.platform.feature_quiz.dtos.responses.CategoryStats;
import com.quiz.platform.feature_quiz.dtos.responses.QuizResponse;
import com.quiz.platform.feature_quiz.entities.postgres.Quiz;
import com.quiz.platform.shared.dtos.PagedResponse;
import com.quiz.platform.shared.security.UserInfo;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for quiz operations.
 * Provides business logic for creating, retrieving, and managing quizzes.
 */
public interface QuizService {

    /**
     * Creates a new quiz.
     *
     * @param request the quiz creation request
     * @return the created quiz response
     */
    QuizResponse createQuiz(QuizRequest request);

    /**
     * Retrieves quizzes with optional filtering and pagination.
     *
     * @param page the page number (0-indexed)
     * @param size the number of items per page
     * @param category optional category filter
     * @param level optional level filter
     * @return paginated response of quizzes
     */
    PagedResponse<QuizResponse> getQuizzes(int page, int size, String category, String level);

    /**
     * Retrieves a specific quiz by its ID.
     *
     * @param quizId the ID of the quiz
     * @param userInfo the authenticated user info
     * @param showAnswers whether to include correct answers
     * @return the quiz response
     * @throws com.quiz.platform.shared.exception.ResourceNotFoundException if quiz not found
     * @throws com.quiz.platform.shared.exception.BadRequestException if quiz is not public
     */
    QuizResponse getQuizById(String quizId, UserInfo userInfo, boolean showAnswers);

    /**
     * Updates an existing quiz.
     *
     * @param quizId the ID of the quiz to update
     * @param request the quiz update request
     * @return the updated quiz response
     * @throws com.quiz.platform.shared.exception.ResourceNotFoundException if quiz not found
     */
    QuizResponse updateQuiz(String quizId, QuizRequest request);

    /**
     * Deletes a quiz.
     *
     * @param quizId the ID of the quiz to delete
     * @throws com.quiz.platform.shared.exception.ResourceNotFoundException if quiz not found
     */
    void deleteQuiz(String quizId);

    /**
     * Adds a question to a quiz.
     *
     * @param quizId the ID of the quiz
     * @param request the question creation request
     * @return the updated quiz response
     * @throws com.quiz.platform.shared.exception.ResourceNotFoundException if quiz not found
     */
    QuizResponse addQuestion(String quizId, QuestionRequest request);

    /**
     * Retrieves a quiz entity by its ID.
     *
     * @param quizId the ID of the quiz
     * @return the quiz entity if found
     */
    Optional<Quiz> getQuizEntityById(String quizId);

    /**
     * Retrieves statistics for each category.
     *
     * @return list of category statistics
     */
    List<CategoryStats> getCategoryStats();

    /**
     * Retrieves all available quiz levels.
     *
     * @return list of level names
     */
    List<String> getLevels();

    /**
     * Retrieves admin statistics.
     *
     * @return admin statistics
     */
    AdminStats getAdminStats();
}
