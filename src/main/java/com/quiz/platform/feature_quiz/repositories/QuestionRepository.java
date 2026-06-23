package com.quiz.platform.feature_quiz.repositories;

import com.quiz.platform.feature_quiz.entities.postgres.Question;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, String> {

    /**
     * Retrieves all questions for a specific quiz.
     *
     * @param quizId the ID of the quiz
     * @return a list of questions belonging to the quiz
     */
    List<Question> findByQuizId(String quizId);

    /**
     * Deletes all questions for a specific quiz.
     *
     * @param quizId the ID of the quiz
     */
    void deleteByQuizId(String quizId);
}
