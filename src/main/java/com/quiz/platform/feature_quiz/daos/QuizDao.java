package com.quiz.platform.feature_quiz.daos;

import com.quiz.platform.feature_quiz.entities.postgres.Quiz;
import com.quiz.platform.feature_quiz.entities.postgres.QuizCategory;
import com.quiz.platform.feature_quiz.entities.postgres.QuizLevel;
import com.quiz.platform.feature_quiz.repositories.QuizRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuizDao {

    private final QuizRepository quizRepository;

    public Optional<Quiz> findById(String quizId) {
        return quizRepository.findById(quizId);
    }

    public Page<Quiz> findByIsPublicTrue(Pageable pageable) {
        return quizRepository.findByIsPublicTrue(pageable);
    }

    public Page<Quiz> findByIsPublicTrueAndCategory(QuizCategory category, Pageable pageable) {
        return quizRepository.findByIsPublicTrueAndCategory(category, pageable);
    }

    public Page<Quiz> findByCategoryAndLevel(QuizCategory category, QuizLevel level, Pageable pageable) {
        if (category != null && level != null) {
            return quizRepository.findByCategoryAndLevelAndIsPublicTrue(category, level, pageable);
        } else if (category != null) {
            return quizRepository.findByIsPublicTrueAndCategory(category, pageable);
        } else if (level != null) {
            return quizRepository.findByLevelAndIsPublicTrue(level, pageable);
        }
        return quizRepository.findByIsPublicTrue(pageable);
    }

    public Quiz save(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public void delete(Quiz quiz) {
        quizRepository.delete(quiz);
    }

    public Long countByIsPublicTrue() {
        return quizRepository.countByIsPublicTrue();
    }

    public long countByCategoryAndLevelAndIsPublicTrue(QuizCategory category, QuizLevel level) {
        return quizRepository.countByCategoryAndLevelAndIsPublicTrue(category, level);
    }

    public long count() {
        return quizRepository.count();
    }
}
