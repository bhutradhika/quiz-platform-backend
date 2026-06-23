package com.quiz.platform.feature_attempt.dtos.responses;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LeaderboardEntry {
    private String attemptId;
    private String userId;
    private String username;
    private String quizId;
    private String quizTitle;
    private Double score;
    private Double maxScore;
    private Double percentage;
    private Date completedAt;
    private Integer rank;
}
