package com.quiz.platform.shared.cache;

/**
 * Cache key constants for Redis.
 */
public final class CacheKeys {

    private CacheKeys() {}

    public static final String GLOBAL_LEADERBOARD = "leaderboard:global";
    public static final String LEADERBOARD_BY_QUIZ = "leaderboard:quiz:";
    public static final String DASHBOARD_STATS = "dashboard:stats:";
    public static final String CATEGORY_STATS = "category:stats";
    public static final String QUIZ_DETAIL = "quiz:detail:";

    public static String globalLeaderboardKey(String category, String level) {
        return GLOBAL_LEADERBOARD + ":" + (category != null ? category : "all") + ":" + (level != null ? level : "all");
    }

    public static String leaderboardByQuizKey(String quizId) {
        return LEADERBOARD_BY_QUIZ + quizId;
    }

    public static String dashboardStatsKey(String userId) {
        return DASHBOARD_STATS + userId;
    }

    public static String quizDetailKey(String quizId) {
        return QUIZ_DETAIL + quizId;
    }
}
