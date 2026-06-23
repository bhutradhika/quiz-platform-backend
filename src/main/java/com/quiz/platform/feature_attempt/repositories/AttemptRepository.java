package com.quiz.platform.feature_attempt.repositories;

import com.quiz.platform.feature_attempt.entities.postgres.Attempt;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AttemptRepository extends JpaRepository<Attempt, String> {

    Page<Attempt> findByUserId(String userId, Pageable pageable);

    Optional<Attempt> findByIdAndUserId(String id, String userId);

    List<Attempt> findByUserIdAndQuizId(String userId, String quizId);

    @Query("SELECT a FROM Attempt a WHERE a.quizId = :quizId AND a.completedAt IS NOT NULL ORDER BY a.score DESC, a.completedAt ASC")
    List<Attempt> findLeaderboardByQuizId(@Param("quizId") String quizId, Pageable pageable);

    Boolean existsByUserIdAndQuizIdAndCompletedAtIsNull(String userId, String quizId);

    List<Attempt> findByUserIdAndCompletedAtIsNotNull(String userId);
}
