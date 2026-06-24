package com.quiz.platform.feature_quiz.daos;

import com.quiz.platform.feature_quiz.entities.postgres.Question;
import com.quiz.platform.feature_quiz.repositories.QuestionRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * Data Access Object for Question entities.
 * Provides an abstraction layer over the QuestionRepository.
 */
@Repository
@RequiredArgsConstructor
public class QuestionDao {

    private final QuestionRepository questionRepository;

    /**
     * Finds all questions for a specific quiz.
     *
     * @param quizId the ID of the quiz
     * @return list of questions
     */
    public List<Question> findByQuizId(String quizId) {
        return questionRepository.findByQuizId(quizId);
    }

    /**
     * Finds a question by its ID.
     *
     * @param id the ID of the question
     * @return the question if found
     */
    public Optional<Question> findById(String id) {
        return questionRepository.findById(id);
    }

    /**
     * Saves a question entity.
     *
     * @param question the question to save
     * @return the saved question
     */
    public Question save(Question question) {
        return questionRepository.save(question);
    }

    /**
     * Counts all questions.
     *
     * @return total count of questions
     */
    public long count() {
        return questionRepository.count();
    }
}
