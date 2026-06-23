package com.quiz.platform.feature_quiz.dtos.requests;

import com.quiz.platform.feature_quiz.entities.postgres.QuizCategory;
import com.quiz.platform.feature_quiz.entities.postgres.QuizLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuizRequest {
    @NotBlank(message = "Title is required") private String title;

    private String description;

    @NotNull(message = "Category is required") private QuizCategory category;

    private QuizLevel level;

    private Boolean isPublic = true;
}
