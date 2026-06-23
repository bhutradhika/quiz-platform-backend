package com.quiz.platform.feature_attempt.repositories;

import com.quiz.platform.feature_attempt.entities.postgres.Answer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, String> {

    List<Answer> findByAttemptId(String attemptId);

    Optional<Answer> findByAttemptIdAndQuestionId(String attemptId, String questionId);
}
