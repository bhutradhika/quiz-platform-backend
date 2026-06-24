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

/**
 * REST controller for public quiz operations.
 * Provides endpoints for browsing and retrieving quiz information.
 */
@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    /**
     * Retrieves all public quizzes with optional filtering and pagination.
     *
     * @param page the page number (0-indexed)
     * @param size the number of items per page
     * @param category optional category filter
     * @param level optional level filter
     * @return ResponseEntity containing paginated quiz list
     */
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

    /**
     * Retrieves all available quiz categories.
     *
     * @return ResponseEntity containing list of categories
     */
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse> getQuizCategories() {
        ApiResponse response = new ApiResponse();
        response.setData(List.of(QuizCategory.values()));
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves statistics for each category including quiz counts per level.
     *
     * @return ResponseEntity containing category statistics
     */
    @GetMapping("/categories/stats")
    public ResponseEntity<ApiResponse> getCategoryStats() {
        ApiResponse response = new ApiResponse();
        response.setData(quizService.getCategoryStats());
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves all available quiz levels.
     *
     * @return ResponseEntity containing list of levels
     */
    @GetMapping("/levels")
    public ResponseEntity<ApiResponse> getLevels() {
        ApiResponse response = new ApiResponse();
        response.setData(quizService.getLevels());
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a specific quiz by its ID.
     *
     * @param id the ID of the quiz
     * @param showAnswers whether to include correct answers in the response
     * @return ResponseEntity containing the quiz details
     */
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
