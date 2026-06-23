package com.quiz.platform.feature_feed.helpers;

import com.quiz.platform.feature_attempt.daos.AttemptDao;
import com.quiz.platform.feature_attempt.dtos.responses.DashboardStats;
import com.quiz.platform.feature_attempt.dtos.responses.LeaderboardEntry;
import com.quiz.platform.feature_attempt.entities.postgres.Attempt;
import com.quiz.platform.feature_quiz.daos.QuizDao;
import com.quiz.platform.feature_user.daos.UserDao;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeedHelper {

    private final QuizDao quizDao;
    private final AttemptDao attemptDao;
    private final UserDao userDao;

    public DashboardStats buildDashboardStats(String userId) {
        DashboardStats stats = new DashboardStats();

        Long totalQuizzes = quizDao.countByIsPublicTrue();
        stats.setTotalQuizzes(totalQuizzes);

        List<Attempt> userAttempts = attemptDao.findByUserIdAndCompletedAtIsNotNull(userId);
        stats.setCompletedQuizzes((long) userAttempts.size());

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

    public List<LeaderboardEntry> buildLeaderboard(List<Attempt> attempts) {
        List<LeaderboardEntry> leaderboard = new ArrayList<>();
        int rank = 1;
        for (Attempt attempt : attempts) {
            leaderboard.add(convertToLeaderboardEntry(attempt, rank++));
        }
        return leaderboard;
    }

    private LeaderboardEntry convertToLeaderboardEntry(Attempt attempt, int rank) {
        LeaderboardEntry entry = new LeaderboardEntry();
        entry.setAttemptId(attempt.getId());
        entry.setUserId(attempt.getUserId());
        entry.setQuizId(attempt.getQuizId());
        entry.setScore(attempt.getScore());
        entry.setMaxScore(attempt.getMaxScore());
        entry.setCompletedAt(attempt.getCompletedAt());
        entry.setRank(rank);
        entry.setPercentage(calculatePercentage(attempt.getScore(), attempt.getMaxScore()));

        userDao.findById(attempt.getUserId()).ifPresent(user -> entry.setUsername(user.getUsername()));
        quizDao.findById(attempt.getQuizId()).ifPresent(quiz -> entry.setQuizTitle(quiz.getTitle()));

        return entry;
    }

    private double calculatePercentage(Double score, Double maxScore) {
        if (maxScore == null || maxScore <= 0) {
            return 0.0;
        }
        return (score / maxScore) * 100;
    }
}
