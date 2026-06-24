package com.quiz.platform.feature_feed.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.quiz.platform.feature_attempt.daos.AttemptDao;
import com.quiz.platform.feature_attempt.dtos.responses.DashboardStats;
import com.quiz.platform.feature_attempt.dtos.responses.LeaderboardEntry;
import com.quiz.platform.feature_attempt.entities.postgres.Attempt;
import com.quiz.platform.feature_feed.helpers.FeedHelper;
import com.quiz.platform.feature_feed.services.FeedService;
import com.quiz.platform.feature_quiz.daos.QuizDao;
import com.quiz.platform.feature_quiz.entities.postgres.Quiz;
import com.quiz.platform.shared.cache.CacheKeys;
import com.quiz.platform.shared.cache.CacheService;
import com.quiz.platform.shared.exception.ResourceNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link FeedService} for feed operations.
 * Handles business logic for dashboard stats and leaderboard with Redis caching.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

    private static final long ONE_HOUR_SECONDS = 3600L;

    private final AttemptDao attemptDao;
    private final QuizDao quizDao;
    private final FeedHelper feedHelper;
    private final CacheService cacheService;

    @Override
    public DashboardStats getDashboardStats(String userId) {
        String cacheKey = CacheKeys.dashboardStatsKey(userId);
        DashboardStats cached = cacheService.get(cacheKey, DashboardStats.class);
        if (cached != null) {
            return cached;
        }

        DashboardStats stats = feedHelper.buildDashboardStats(userId);
        cacheService.set(cacheKey, stats, ONE_HOUR_SECONDS);
        return stats;
    }

    @Override
    public List<LeaderboardEntry> getLeaderboard(String quizId, int limit) {
        Quiz quiz = quizDao.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", quizId));

        String cacheKey = CacheKeys.leaderboardByQuizKey(quizId);
        List<LeaderboardEntry> cached = cacheService.get(cacheKey, new TypeReference<List<LeaderboardEntry>>() {});
        if (cached != null) {
            return cached;
        }

        Pageable pageable = PageRequest.of(0, limit);
        List<Attempt> topAttempts = attemptDao.findLeaderboardByQuizId(quizId, pageable);
        List<LeaderboardEntry> leaderboard = feedHelper.buildLeaderboard(topAttempts);
        cacheService.set(cacheKey, leaderboard, ONE_HOUR_SECONDS);
        return leaderboard;
    }

    @Override
    public List<LeaderboardEntry> getGlobalLeaderboard(String category, String level, int limit) {
        String cacheKey = CacheKeys.globalLeaderboardKey(category, level);
        List<LeaderboardEntry> cached = cacheService.get(cacheKey, new TypeReference<List<LeaderboardEntry>>() {});
        if (cached != null) {
            return cached;
        }

        Pageable pageable = PageRequest.of(0, limit);
        List<Attempt> topAttempts = attemptDao.findGlobalLeaderboard(category, level, pageable);
        List<LeaderboardEntry> leaderboard = feedHelper.buildLeaderboard(topAttempts);
        cacheService.set(cacheKey, leaderboard, ONE_HOUR_SECONDS);
        return leaderboard;
    }
}
