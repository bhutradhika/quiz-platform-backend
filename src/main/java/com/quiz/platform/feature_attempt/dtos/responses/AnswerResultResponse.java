package com.quiz.platform.feature_attempt.dtos.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AnswerResultResponse {
    private String questionId;
    private String selectedChoiceId;
    private Boolean isCorrect;
    private String correctChoiceId;
}
