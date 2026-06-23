package com.quiz.platform.feature_feed.services.impl;

import com.quiz.platform.feature_attempt.daos.AttemptDao;
import com.quiz.platform.feature_attempt.dtos.responses.DashboardStats;
import com.quiz.platform.feature_attempt.dtos.responses.LeaderboardEntry;
import com.quiz.platform.feature_attempt.entities.postgres.Attempt;
import com.quiz.platform.feature_feed.helpers.FeedHelper;
import com.quiz.platform.feature_feed.services.FeedService;
import com.quiz.platform.feature_quiz.daos.QuizDao;
import com.quiz.platform.feature_quiz.entities.postgres.Quiz;
import com.quiz.platform.shared.exception.ResourceNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

    private final AttemptDao attemptDao;
    private final QuizDao quizDao;
    private final FeedHelper feedHelper;

    @Override
    public DashboardStats getDashboardStats(String userId) {
        return feedHelper.buildDashboardStats(userId);
    }

    @Override
    public List<LeaderboardEntry> getLeaderboard(String quizId, int limit) {
        Quiz quiz = quizDao.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", quizId));

        Pageable pageable = PageRequest.of(0, limit);
        List<Attempt> topAttempts = attemptDao.findLeaderboardByQuizId(quizId, pageable);

        return feedHelper.buildLeaderboard(topAttempts);
    }
}
