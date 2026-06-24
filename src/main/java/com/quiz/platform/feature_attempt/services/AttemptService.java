package com.quiz.platform.feature_attempt.services;

import com.quiz.platform.feature_attempt.dtos.requests.AnswerRequest;
import com.quiz.platform.feature_attempt.dtos.responses.AnswerResultResponse;
import com.quiz.platform.feature_attempt.dtos.responses.AttemptResponse;
import com.quiz.platform.shared.dtos.PagedResponse;
import java.util.List;

/**
 * Service interface for managing quiz attempts.
 * Provides business logic for starting, completing, and querying quiz attempts.
 */
public interface AttemptService {

    /**
     * Starts a new quiz attempt for a user.
     *
     * @param userId the ID of the user starting the attempt
     * @param quizId the ID of the quiz to attempt
     * @return the created attempt response
     * @throws com.quiz.platform.shared.exception.BadRequestException if user has already completed the quiz
     *                                                                  or has an incomplete attempt
     */
    AttemptResponse startAttempt(String userId, String quizId);

    /**
     * Retrieves an incomplete attempt for a user and quiz if one exists.
     *
     * @param userId the ID of the user
     * @param quizId the ID of the quiz
     * @return the incomplete attempt response
     * @throws com.quiz.platform.shared.exception.BadRequestException if no incomplete attempt exists
     */
    AttemptResponse getIncompleteAttempt(String userId, String quizId);

    /**
     * Submits an answer for a question in an ongoing attempt.
     *
     * @param userId the ID of the user
     * @param attemptId the ID of the attempt
     * @param request the answer request containing question and choice IDs
     * @return the answer result with correctness feedback
     * @throws com.quiz.platform.shared.exception.BadRequestException if attempt is completed or question already answered
     */
    AnswerResultResponse submitAnswer(String userId, String attemptId, AnswerRequest request);

    /**
     * Completes an ongoing quiz attempt.
     *
     * @param userId the ID of the user
     * @param attemptId the ID of the attempt to complete
     * @return the completed attempt response
     * @throws com.quiz.platform.shared.exception.BadRequestException if attempt is already completed
     */
    AttemptResponse completeAttempt(String userId, String attemptId);

    /**
     * Retrieves a specific attempt by its ID.
     *
     * @param userId the ID of the user
     * @param attemptId the ID of the attempt
     * @return the attempt response
     * @throws com.quiz.platform.shared.exception.ResourceNotFoundException if attempt not found
     */
    AttemptResponse getAttemptById(String userId, String attemptId);

    /**
     * Retrieves all answers submitted for a specific attempt.
     *
     * @param userId the ID of the user
     * @param attemptId the ID of the attempt
     * @return list of answer results with correctness feedback
     * @throws com.quiz.platform.shared.exception.ResourceNotFoundException if attempt not found
     */
    List<AnswerResultResponse> getAttemptAnswers(String userId, String attemptId);

    /**
     * Retrieves all attempts for a user with pagination.
     *
     * @param userId the ID of the user
     * @param page the page number (0-indexed)
     * @param size the number of items per page
     * @return paginated response of attempts
     */
    PagedResponse<AttemptResponse> getAttemptsByUser(String userId, int page, int size);

    /**
     * Retrieves the IDs of all quizzes completed by a user.
     *
     * @param userId the ID of the user
     * @return list of completed quiz IDs
     */
    List<String> getCompletedQuizIds(String userId);
}
