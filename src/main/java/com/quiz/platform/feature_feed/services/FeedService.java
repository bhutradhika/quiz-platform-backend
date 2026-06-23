package com.quiz.platform.feature_feed.services;

import com.quiz.platform.feature_attempt.dtos.responses.DashboardStats;
import com.quiz.platform.feature_attempt.dtos.responses.LeaderboardEntry;
import java.util.List;

public interface FeedService {

    DashboardStats getDashboardStats(String userId);

    List<LeaderboardEntry> getLeaderboard(String quizId, int limit);
}
