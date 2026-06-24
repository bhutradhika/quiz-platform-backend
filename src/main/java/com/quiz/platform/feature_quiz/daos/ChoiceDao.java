package com.quiz.platform.feature_quiz.daos;

import com.quiz.platform.feature_quiz.entities.postgres.Choice;
import com.quiz.platform.feature_quiz.repositories.ChoiceRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * Data Access Object for Choice entities.
 * Provides an abstraction layer over the ChoiceRepository.
 */
@Repository
@RequiredArgsConstructor
public class ChoiceDao {

    private final ChoiceRepository choiceRepository;

    /**
     * Finds a choice by its ID.
     *
     * @param choiceId the ID of the choice
     * @return the choice if found
     */
    public Optional<Choice> findById(String choiceId) {
        return choiceRepository.findById(choiceId);
    }

    /**
     * Finds all choices for a specific question.
     *
     * @param questionId the ID of the question
     * @return list of choices
     */
    public List<Choice> findByQuestionId(String questionId) {
        return choiceRepository.findByQuestionId(questionId);
    }

    /**
     * Saves a choice entity.
     *
     * @param choice the choice to save
     * @return the saved choice
     */
    public Choice save(Choice choice) {
        return choiceRepository.save(choice);
    }
}
