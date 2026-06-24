package com.quiz.platform.feature_quiz.repositories;

import com.quiz.platform.feature_quiz.entities.postgres.Choice;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChoiceRepository extends JpaRepository<Choice, String> {

    /**
     * Retrieves all choices for a specific question.
     *
     * @param questionId the ID of the question
     * @return a list of choices belonging to the question
     */
    List<Choice> findByQuestionId(String questionId);

    /**
     * Retrieves all correct choices for a specific question.
     *
     * @param questionId the ID of the question
     * @return a list of correct choices for the question
     */
    List<Choice> findByQuestionIdAndIsCorrectTrue(String questionId);

    /**
     * Retrieves all choices for all questions in a quiz.
     *
     * @param quizId the ID of the quiz
     * @return a list of all choices for the quiz
     */
    @Query("SELECT c FROM Choice c WHERE c.questionId IN (SELECT q.id FROM Question q WHERE q.quizId = :quizId)")
    List<Choice> findByQuizId(@Param("quizId") String quizId);

    /**
     * Deletes all choices for a specific question.
     *
     * @param questionId the ID of the question
     */
    void deleteByQuestionId(String questionId);
}
