package com.quiz.platform.feature_feed.controllers;

import com.quiz.platform.feature_attempt.dtos.responses.DashboardStats;
import com.quiz.platform.feature_attempt.dtos.responses.LeaderboardEntry;
import com.quiz.platform.feature_feed.services.FeedService;
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
 * REST controller for feed operations.
 * Provides endpoints for dashboard stats and leaderboard.
 */
@RestController
@RequestMapping("/api/feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    /**
     * Retrieves dashboard statistics for the authenticated user.
     *
     * @return ResponseEntity containing dashboard stats
     */
    @GetMapping("/dashboard/stats")
    public ResponseEntity<ApiResponse> getDashboardStats() {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = userInfo.getUserId();

        DashboardStats stats = feedService.getDashboardStats(userId);

        ApiResponse response = new ApiResponse();
        response.setData(stats);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves the global leaderboard with optional filters.
     *
     * @param category optional category filter
     * @param level optional level filter
     * @param limit maximum number of entries to return
     * @return ResponseEntity containing leaderboard entries
     */
    @GetMapping("/leaderboard")
    public ResponseEntity<ApiResponse> getGlobalLeaderboard(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String level,
            @RequestParam(defaultValue = "10") int limit) {

        List<LeaderboardEntry> leaderboard = feedService.getGlobalLeaderboard(category, level, limit);

        ApiResponse response = new ApiResponse();
        response.setData(leaderboard);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves the leaderboard for a specific quiz.
     *
     * @param quizId the ID of the quiz
     * @param limit maximum number of entries to return
     * @return ResponseEntity containing leaderboard entries
     */
    @GetMapping("/leaderboard/{quizId}")
    public ResponseEntity<ApiResponse> getLeaderboard(
            @PathVariable String quizId,
            @RequestParam(defaultValue = "10") int limit) {

        List<LeaderboardEntry> leaderboard = feedService.getLeaderboard(quizId, limit);

        ApiResponse response = new ApiResponse();
        response.setData(leaderboard);
        return ResponseEntity.ok(response);
    }
}
