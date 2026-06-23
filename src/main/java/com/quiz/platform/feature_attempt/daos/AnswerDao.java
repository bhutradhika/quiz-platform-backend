package com.quiz.platform.feature_attempt.daos;

import com.quiz.platform.feature_attempt.entities.postgres.Answer;
import com.quiz.platform.feature_attempt.repositories.AnswerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AnswerDao {

    private final AnswerRepository answerRepository;

    public void save(Answer answer) {
        answerRepository.save(answer);
    }

    public Answer findByAttemptIdAndQuestionId(String attemptId, String questionId) {
        return answerRepository.findByAttemptIdAndQuestionId(attemptId, questionId).orElse(null);
    }

    public List<Answer> findByAttemptId(String attemptId) {
        return answerRepository.findByAttemptId(attemptId);
    }
}
