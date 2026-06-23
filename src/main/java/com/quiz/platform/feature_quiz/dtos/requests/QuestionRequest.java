package com.quiz.platform.feature_quiz.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.quiz.platform.feature_quiz.entities.postgres.QuizType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class QuestionRequest {
    @NotBlank(message = "Question text is required") private String text;

    private QuizType type = QuizType.MULTIPLE_CHOICE;

    @NotNull(message = "Points are required") private Integer points;

    @NotNull(message = "Choices are required") @JsonProperty("choices")
    private List<ChoiceRequest> choices;

    @Data
    public static class ChoiceRequest {
        @NotBlank(message = "Choice text is required") private String text;

        @NotNull(message = "isCorrect is required") private Boolean isCorrect;

        private Integer orderIndex = 0;
    }
}
