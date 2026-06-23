package com.quiz.platform.feature_attempt.daos;

import com.quiz.platform.feature_attempt.entities.postgres.Attempt;
import com.quiz.platform.feature_attempt.repositories.AttemptRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AttemptDao {

    private final AttemptRepository attemptRepository;

    public Attempt save(Attempt attempt) {
        return attemptRepository.save(attempt);
    }

    public boolean existsByUserIdAndQuizIdAndCompletedAtIsNull(String userId, String quizId) {
        return attemptRepository.existsByUserIdAndQuizIdAndCompletedAtIsNull(userId, quizId);
    }

    public List<Attempt> findByUserIdAndQuizId(String userId, String quizId) {
        return attemptRepository.findByUserIdAndQuizId(userId, quizId);
    }

    public Optional<Attempt> findByIdAndUserId(String attemptId, String userId) {
        return attemptRepository.findByIdAndUserId(attemptId, userId);
    }

    public Page<Attempt> findByUserId(String userId, Pageable pageable) {
        return attemptRepository.findByUserId(userId, pageable);
    }

    public List<Attempt> findLeaderboardByQuizId(String quizId, Pageable pageable) {
        return attemptRepository.findLeaderboardByQuizId(quizId, pageable);
    }

    public List<Attempt> findByUserIdAndCompletedAtIsNotNull(String userId) {
        return attemptRepository.findByUserIdAndCompletedAtIsNotNull(userId);
    }
}
