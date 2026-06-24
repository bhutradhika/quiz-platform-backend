package com.quiz.platform.feature_quiz.controllers;

import com.quiz.platform.feature_quiz.dtos.requests.QuestionRequest;
import com.quiz.platform.feature_quiz.dtos.requests.QuizRequest;
import com.quiz.platform.feature_quiz.services.QuizService;
import com.quiz.platform.shared.dtos.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for admin quiz operations.
 * Provides endpoints for creating, updating, and deleting quizzes.
 * All endpoints require ADMIN role.
 */
@RestController
@RequestMapping("/api/admin/quizzes")
@RequiredArgsConstructor
public class AdminQuizController {

    private final QuizService quizService;

    /**
     * Retrieves admin statistics including total quizzes, questions, and categories.
     *
     * @return ResponseEntity containing admin statistics
     */
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAdminStats() {
        ApiResponse response = new ApiResponse();
        response.setData(quizService.getAdminStats());
        return ResponseEntity.ok(response);
    }

    /**
     * Creates a new quiz.
     *
     * @param request the quiz creation request
     * @return ResponseEntity containing the created quiz
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createQuiz(@Valid @RequestBody QuizRequest request) {
        ApiResponse response = new ApiResponse();
        response.setData(quizService.createQuiz(request));
        return ResponseEntity.ok(response);
    }

    /**
     * Updates an existing quiz.
     *
     * @param id the ID of the quiz to update
     * @param request the quiz update request
     * @return ResponseEntity containing the updated quiz
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateQuiz(
            @PathVariable String id,
            @Valid @RequestBody QuizRequest request) {
        ApiResponse response = new ApiResponse();
        response.setData(quizService.updateQuiz(id, request));
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a quiz.
     *
     * @param id the ID of the quiz to delete
     * @return ResponseEntity with empty response
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteQuiz(@PathVariable String id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.ok(new ApiResponse());
    }

    /**
     * Adds a question to a quiz.
     *
     * @param id the ID of the quiz
     * @param request the question creation request
     * @return ResponseEntity containing the updated quiz
     */
    @PostMapping("/{id}/questions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> addQuestion(
            @PathVariable String id,
            @Valid @RequestBody QuestionRequest request) {
        ApiResponse response = new ApiResponse();
        response.setData(quizService.addQuestion(id, request));
        return ResponseEntity.ok(response);
    }
}
