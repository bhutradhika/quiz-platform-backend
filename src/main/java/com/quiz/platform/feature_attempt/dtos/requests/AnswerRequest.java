package com.quiz.platform.feature_attempt.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AnswerRequest {
    @NotBlank private String questionId;

    @NotBlank private String choiceId;
}
