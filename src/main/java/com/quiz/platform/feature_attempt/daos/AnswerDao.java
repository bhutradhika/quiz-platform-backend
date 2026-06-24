package com.quiz.platform.feature_attempt.daos;

import com.quiz.platform.feature_attempt.entities.postgres.Answer;
import com.quiz.platform.feature_attempt.repositories.AnswerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * Data Access Object for Answer entities.
 * Provides an abstraction layer over the AnswerRepository.
 */
@Repository
@RequiredArgsConstructor
public class AnswerDao {

    private final AnswerRepository answerRepository;

    /**
     * Saves an answer entity.
     *
     * @param answer the answer to save
     */
    public void save(Answer answer) {
        answerRepository.save(answer);
    }

    /**
     * Finds an answer by attempt ID and question ID.
     *
     * @param attemptId the ID of the attempt
     * @param questionId the ID of the question
     * @return the answer if found, null otherwise
     */
    public Answer findByAttemptIdAndQuestionId(String attemptId, String questionId) {
        return answerRepository.findByAttemptIdAndQuestionId(attemptId, questionId).orElse(null);
    }

    /**
     * Finds all answers for a specific attempt.
     *
     * @param attemptId the ID of the attempt
     * @return list of answers
     */
    public List<Answer> findByAttemptId(String attemptId) {
        return answerRepository.findByAttemptId(attemptId);
    }
}
