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

@RestController
@RequestMapping("/api/attempts")
@RequiredArgsConstructor
public class AttemptController {

    private final AttemptService attemptService;

    @PostMapping("/start/{quizId}")
    public ResponseEntity<ApiResponse> startAttempt(@PathVariable String quizId) {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userInfo.getUserId();

        ApiResponse response = new ApiResponse();
        response.setData(attemptService.startAttempt(userId, quizId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/incomplete/{quizId}")
    public ResponseEntity<ApiResponse> getIncompleteAttempt(@PathVariable String quizId) {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userInfo.getUserId();

        ApiResponse response = new ApiResponse();
        response.setData(attemptService.getIncompleteAttempt(userId, quizId));
        return ResponseEntity.ok(response);
    }

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

    @PostMapping("/{attemptId}/complete")
    public ResponseEntity<ApiResponse> completeAttempt(@PathVariable String attemptId) {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userInfo.getUserId();

        ApiResponse response = new ApiResponse();
        response.setData(attemptService.completeAttempt(userId, attemptId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{attemptId}")
    public ResponseEntity<ApiResponse> getAttemptById(@PathVariable String attemptId) {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userInfo.getUserId();

        ApiResponse response = new ApiResponse();
        response.setData(attemptService.getAttemptById(userId, attemptId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{attemptId}/answers")
    public ResponseEntity<ApiResponse> getAttemptAnswers(@PathVariable String attemptId) {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userInfo.getUserId();

        ApiResponse response = new ApiResponse();
        response.setData(attemptService.getAttemptAnswers(userId, attemptId));
        return ResponseEntity.ok(response);
    }

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

    @GetMapping("/completed-quizzes")
    public ResponseEntity<ApiResponse> getCompletedQuizIds() {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userInfo.getUserId();

        ApiResponse response = new ApiResponse();
        response.setData(attemptService.getCompletedQuizIds(userId));
        return ResponseEntity.ok(response);
    }
}
