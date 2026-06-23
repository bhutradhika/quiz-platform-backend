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

@RestController
@RequestMapping("/api/admin/quizzes")
@RequiredArgsConstructor
public class AdminQuizController {

    private final QuizService quizService;

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAdminStats() {
        ApiResponse response = new ApiResponse();
        response.setData(quizService.getAdminStats());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createQuiz(@Valid @RequestBody QuizRequest request) {
        ApiResponse response = new ApiResponse();
        response.setData(quizService.createQuiz(request));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateQuiz(
            @PathVariable String id,
            @Valid @RequestBody QuizRequest request) {
        ApiResponse response = new ApiResponse();
        response.setData(quizService.updateQuiz(id, request));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteQuiz(@PathVariable String id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.ok(new ApiResponse());
    }

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
