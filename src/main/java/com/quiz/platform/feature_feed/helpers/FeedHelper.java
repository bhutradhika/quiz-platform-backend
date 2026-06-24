package com.quiz.platform.feature_feed.helpers;

import com.quiz.platform.feature_attempt.daos.AttemptDao;
import com.quiz.platform.feature_attempt.dtos.responses.DashboardStats;
import com.quiz.platform.feature_attempt.dtos.responses.LeaderboardEntry;
import com.quiz.platform.feature_attempt.entities.postgres.Attempt;
import com.quiz.platform.feature_quiz.daos.QuizDao;
import com.quiz.platform.feature_quiz.entities.postgres.Quiz;
import com.quiz.platform.feature_user.daos.UserDao;
import com.quiz.platform.feature_user.entities.postgres.User;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Helper component for feed operations.
 * Provides reusable business logic methods for dashboard stats and leaderboard.
 */
@Component
@RequiredArgsConstructor
public class FeedHelper {

    private final QuizDao quizDao;
    private final AttemptDao attemptDao;
    private final UserDao userDao;

    /**
     * Builds dashboard statistics for a user.
     *
     * @param userId the ID of the user
     * @return dashboard statistics
     */
    public DashboardStats buildDashboardStats(String userId) {
        DashboardStats stats = new DashboardStats();

        Long totalQuizzes = quizDao.countByIsPublicTrue();
        stats.setTotalQuizzes(totalQuizzes);

        Long completedQuizzes = attemptDao.countCompletedByUserId(userId);
        stats.setCompletedQuizzes(completedQuizzes);

        List<Attempt> userAttempts = attemptDao.findByUserIdAndCompletedAtIsNotNull(userId);
        if (!userAttempts.isEmpty()) {
            Double avgPercentage = userAttempts.stream()
                    .mapToDouble(a -> {
                        if (a.getScore() == null || a.getMaxScore() == null || a.getMaxScore() == 0) {
                            return 0.0;
                        }
                        return (a.getScore() / a.getMaxScore()) * 100.0;
                    })
                    .average()
                    .orElse(0.0);
            stats.setAverageScore(avgPercentage);
        } else {
            stats.setAverageScore(0.0);
        }

        return stats;
    }

    /**
     * Builds a leaderboard from a list of attempts.
     * Uses caching to minimize database queries for user and quiz lookups.
     *
     * @param attempts list of attempts to convert
     * @return list of leaderboard entries with ranks
     */
    public List<LeaderboardEntry> buildLeaderboard(List<Attempt> attempts) {
        if (attempts.isEmpty()) {
            return List.of();
        }

        Map<String, User> userCache = loadUserCache(attempts);
        Map<String, Quiz> quizCache = loadQuizCache(attempts);

        return IntStream.range(0, attempts.size())
                .mapToObj(i -> convertToLeaderboardEntry(attempts.get(i), i + 1, userCache, quizCache))
                .collect(Collectors.toList());
    }

    private Map<String, User> loadUserCache(List<Attempt> attempts) {
        return attempts.stream()
                .map(Attempt::getUserId)
                .distinct()
                .map(userId -> userDao.findById(userId).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(User::getId, Function.identity()));
    }

    private Map<String, Quiz> loadQuizCache(List<Attempt> attempts) {
        return attempts.stream()
                .map(Attempt::getQuizId)
                .distinct()
                .map(quizId -> quizDao.findById(quizId).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Quiz::getId, Function.identity()));
    }

    private LeaderboardEntry convertToLeaderboardEntry(Attempt attempt, int rank, Map<String, User> userCache, Map<String, Quiz> quizCache) {
        LeaderboardEntry entry = new LeaderboardEntry();
        entry.setAttemptId(attempt.getId());
        entry.setUserId(attempt.getUserId());
        entry.setQuizId(attempt.getQuizId());
        entry.setScore(attempt.getScore());
        entry.setMaxScore(attempt.getMaxScore());
        entry.setCompletedAt(attempt.getCompletedAt());
        entry.setRank(rank);
        entry.setPercentage(calculatePercentage(attempt.getScore(), attempt.getMaxScore()));

        User user = userCache.get(attempt.getUserId());
        if (user != null) {
            entry.setUsername(user.getUsername());
        }

        Quiz quiz = quizCache.get(attempt.getQuizId());
        if (quiz != null) {
            entry.setQuizTitle(quiz.getTitle());
        }

        return entry;
    }

    private double calculatePercentage(Double score, Double maxScore) {
        if (maxScore == null || maxScore <= 0) {
            return 0.0;
        }
        return (score / maxScore) * 100;
    }
}
