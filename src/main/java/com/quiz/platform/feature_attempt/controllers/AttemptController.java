package com.quiz.platform.feature_attempt.controllers;

import com.quiz.platform.feature_attempt.dtos.requests.AnswerRequest;
import com.quiz.platform.feature_attempt.services.AttemptService;
import com.quiz.platform.shared.dtos.ApiResponse;
import com.quiz.platform.shared.security.UserInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing quiz attempts.
 * Provides endpoints for starting, completing, and retrieving quiz attempts.
 */
@RestController
@RequestMapping("/api/attempts")
@RequiredArgsConstructor
public class AttemptController {

    private final AttemptService attemptService;

    /**
     * Starts a new quiz attempt for the authenticated user.
     *
     * @param quizId the ID of the quiz to attempt
     * @return ResponseEntity containing the new attempt details
     */
    @PostMapping("/start/{quizId}")
    public ResponseEntity<ApiResponse> startAttempt(@PathVariable String quizId) {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userInfo.getUserId();

        ApiResponse response = new ApiResponse();
        response.setData(attemptService.startAttempt(userId, quizId));
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves the incomplete attempt for a quiz if one exists.
     *
     * @param quizId the ID of the quiz
     * @return ResponseEntity containing the incomplete attempt details
     */
    @GetMapping("/incomplete/{quizId}")
    public ResponseEntity<ApiResponse> getIncompleteAttempt(@PathVariable String quizId) {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userInfo.getUserId();

        ApiResponse response = new ApiResponse();
        response.setData(attemptService.getIncompleteAttempt(userId, quizId));
        return ResponseEntity.ok(response);
    }

    /**
     * Submits an answer for a specific question in an attempt.
     *
     * @param attemptId the ID of the attempt
     * @param request the answer request containing question and choice IDs
     * @return ResponseEntity containing the answer result with correctness feedback
     */
    @PostMapping("/{attemptId}/answers")
    public ResponseEntity<ApiResponse> submitAnswer(
            @PathVariable String attemptId,
            @RequestBody @Valid AnswerRequest request) {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userInfo.getUserId();

        ApiResponse response = new ApiResponse();
        response.setData(attemptService.submitAnswer(userId, attemptId, request));
        return ResponseEntity.ok(response);
    }

    /**
     * Completes an ongoing quiz attempt.
     *
     * @param attemptId the ID of the attempt to complete
     * @return ResponseEntity containing the completed attempt details
     */
    @PostMapping("/{attemptId}/complete")
    public ResponseEntity<ApiResponse> completeAttempt(@PathVariable String attemptId) {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userInfo.getUserId();

        ApiResponse response = new ApiResponse();
        response.setData(attemptService.completeAttempt(userId, attemptId));
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a specific attempt by its ID.
     *
     * @param attemptId the ID of the attempt
     * @return ResponseEntity containing the attempt details
     */
    @GetMapping("/{attemptId}")
    public ResponseEntity<ApiResponse> getAttemptById(@PathVariable String attemptId) {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userInfo.getUserId();

        ApiResponse response = new ApiResponse();
        response.setData(attemptService.getAttemptById(userId, attemptId));
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves all answers submitted for a specific attempt.
     *
     * @param attemptId the ID of the attempt
     * @return ResponseEntity containing the list of answer results
     */
    @GetMapping("/{attemptId}/answers")
    public ResponseEntity<ApiResponse> getAttemptAnswers(@PathVariable String attemptId) {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userInfo.getUserId();

        ApiResponse response = new ApiResponse();
        response.setData(attemptService.getAttemptAnswers(userId, attemptId));
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves all attempts for the authenticated user with pagination.
     *
     * @param page the page number (0-indexed)
     * @param size the number of items per page
     * @return ResponseEntity containing a paginated list of attempts
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getMyAttempts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userInfo.getUserId();

        ApiResponse response = new ApiResponse();
        response.setData(attemptService.getAttemptsByUser(userId, page, size));
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves the IDs of all quizzes completed by the authenticated user.
     *
     * @return ResponseEntity containing a list of completed quiz IDs
     */
    @GetMapping("/completed-quizzes")
    public ResponseEntity<ApiResponse> getCompletedQuizIds() {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userInfo.getUserId();

        ApiResponse response = new ApiResponse();
        response.setData(attemptService.getCompletedQuizIds(userId));
        return ResponseEntity.ok(response);
    }
}
