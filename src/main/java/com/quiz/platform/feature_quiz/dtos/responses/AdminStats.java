package com.quiz.platform.feature_quiz.dtos.responses;

import lombok.Data;

@Data
public class AdminStats {
    private Long totalQuizzes;
    private Long totalQuestions;
    private Long totalCategories;
}
