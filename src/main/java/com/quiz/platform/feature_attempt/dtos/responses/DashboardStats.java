package com.quiz.platform.feature_attempt.dtos.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DashboardStats {
    private Long totalQuizzes;
    private Long completedQuizzes;
    private Double averageScore;
}
