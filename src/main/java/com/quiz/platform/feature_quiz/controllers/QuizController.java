package com.quiz.platform.feature_quiz.controllers;

import com.quiz.platform.feature_quiz.entities.postgres.QuizCategory;
import com.quiz.platform.feature_quiz.services.QuizService;
import com.quiz.platform.shared.dtos.ApiResponse;
import com.quiz.platform.shared.security.UserInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @GetMapping
    public ResponseEntity<ApiResponse> getQuizzes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String level) {

        ApiResponse response = new ApiResponse();
        response.setData(quizService.getQuizzes(page, size, category, level));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse> getQuizCategories() {
        ApiResponse response = new ApiResponse();
        response.setData(List.of(QuizCategory.values()));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/categories/stats")
    public ResponseEntity<ApiResponse> getCategoryStats() {
        ApiResponse response = new ApiResponse();
        response.setData(quizService.getCategoryStats());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/levels")
    public ResponseEntity<ApiResponse> getLevels() {
        ApiResponse response = new ApiResponse();
        response.setData(quizService.getLevels());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getQuizById(
            @PathVariable String id,
            @RequestParam(defaultValue = "false") boolean showAnswers) {

        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ApiResponse response = new ApiResponse();
        response.setData(quizService.getQuizById(id, userInfo, showAnswers));
        return ResponseEntity.ok(response);
    }
}
