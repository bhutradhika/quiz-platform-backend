package com.quiz.platform.feature_attempt.dtos.responses;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AttemptResponse {
    private String id;
    private String quizId;
    private String quizTitle;
    private String category;
    private String level;
    private Double score;
    private Double maxScore;
    private Double percentage;
    private Date startedAt;
    private Date completedAt;
}
