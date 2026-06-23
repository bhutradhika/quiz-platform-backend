package com.quiz.platform.feature_quiz.dtos.responses;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryStats {
    private String name;
    private List<LevelStats> levels;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LevelStats {
        private String name;
        private Long totalQuizzes;
    }
}
