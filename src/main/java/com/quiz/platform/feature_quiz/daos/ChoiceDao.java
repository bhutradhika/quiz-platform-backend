package com.quiz.platform.feature_quiz.daos;

import com.quiz.platform.feature_quiz.entities.postgres.Choice;
import com.quiz.platform.feature_quiz.repositories.ChoiceRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChoiceDao {

    private final ChoiceRepository choiceRepository;

    public Optional<Choice> findById(String choiceId) {
        return choiceRepository.findById(choiceId);
    }

    public List<Choice> findByQuestionId(String questionId) {
        return choiceRepository.findByQuestionId(questionId);
    }

    public Choice save(Choice choice) {
        return choiceRepository.save(choice);
    }
}
