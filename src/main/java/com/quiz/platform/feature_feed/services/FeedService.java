package com.quiz.platform.feature_feed.services;

import com.quiz.platform.feature_attempt.dtos.responses.DashboardStats;
import com.quiz.platform.feature_attempt.dtos.responses.LeaderboardEntry;
import java.util.List;

/**
 * Service interface for feed operations.
 * Provides business logic for dashboard stats and leaderboard.
 */
public interface FeedService {

    /**
     * Retrieves dashboard statistics for a user.
     *
     * @param userId the ID of the user
     * @return dashboard statistics
     */
    DashboardStats getDashboardStats(String userId);

    /**
     * Retrieves the leaderboard for a specific quiz.
     *
     * @param quizId the ID of the quiz
     * @param limit maximum number of entries to return
     * @return list of leaderboard entries
     * @throws com.quiz.platform.shared.exception.ResourceNotFoundException if quiz not found
     */
    List<LeaderboardEntry> getLeaderboard(String quizId, int limit);

    /**
     * Retrieves the global leaderboard with optional filters.
     *
     * @param category optional category filter
     * @param level optional level filter
     * @param limit maximum number of entries to return
     * @return list of leaderboard entries
     */
    List<LeaderboardEntry> getGlobalLeaderboard(String category, String level, int limit);
}
