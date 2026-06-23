package com.quiz.platform.feature_quiz.repositories;

import com.quiz.platform.feature_quiz.entities.postgres.Quiz;
import com.quiz.platform.feature_quiz.entities.postgres.QuizCategory;
import com.quiz.platform.feature_quiz.entities.postgres.QuizLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, String> {

    Page<Quiz> findByIsPublicTrue(Pageable pageable);

    Page<Quiz> findByCategory(QuizCategory category, Pageable pageable);

    Page<Quiz> findByIsPublicTrueAndCategory(QuizCategory category, Pageable pageable);

    Page<Quiz> findByCategoryAndLevelAndIsPublicTrue(QuizCategory category, QuizLevel level, Pageable pageable);

    Page<Quiz> findByLevelAndIsPublicTrue(QuizLevel level, Pageable pageable);

    Long countByIsPublicTrue();

    long countByCategoryAndLevelAndIsPublicTrue(QuizCategory category, QuizLevel level);
}
