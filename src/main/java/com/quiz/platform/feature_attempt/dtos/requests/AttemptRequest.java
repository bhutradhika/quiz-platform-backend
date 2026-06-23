package com.quiz.platform.feature_attempt.dtos.requests;

import java.util.List;
import lombok.Data;

@Data
public class AttemptRequest {
    private List<AnswerSubmission> answers;

    @Data
    public static class AnswerSubmission {
        private String questionId;
        private String choiceId;
    }
}
