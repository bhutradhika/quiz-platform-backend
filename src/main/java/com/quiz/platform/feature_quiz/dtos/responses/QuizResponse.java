package com.quiz.platform.feature_quiz.dtos.responses;

import com.quiz.platform.feature_quiz.entities.postgres.Choice;
import com.quiz.platform.feature_quiz.entities.postgres.Question;
import com.quiz.platform.feature_quiz.entities.postgres.QuizType;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuizResponse {
    private String id;
    private String title;
    private String description;
    private String category;
    private String level;
    private Boolean isPublic;
    private Integer questionCount;
    private Integer totalPoints;
    private Integer pointsPerQuestion;
    private List<QuestionResponse> questions = new ArrayList<>();

    @Data
    @NoArgsConstructor
    public static class QuestionResponse {
        private String id;
        private String text;
        private QuizType type;
        private Integer points;
        private List<ChoiceResponse> choices = new ArrayList<>();

        @Data
        @NoArgsConstructor
        public static class ChoiceResponse {
            private String id;
            private String text;
            private Boolean isCorrect;
            private Integer orderIndex;

            public static ChoiceResponse fromEntity(Choice choice, boolean hideCorrectAnswer) {
                ChoiceResponse response = new ChoiceResponse();
                response.setId(choice.getId());
                response.setText(choice.getText());
                response.setIsCorrect(hideCorrectAnswer ? null : choice.getIsCorrect());
                response.setOrderIndex(choice.getOrderIndex());
                return response;
            }
        }

        public static QuestionResponse fromEntity(Question question, List<Choice> choices, boolean hideCorrectAnswers) {
            QuestionResponse response = new QuestionResponse();
            response.setId(question.getId());
            response.setText(question.getText());
            response.setType(question.getType());
            response.setPoints(question.getPoints());

            response.setChoices(choices.stream()
                    .sorted((c1, c2) -> c1.getOrderIndex().compareTo(c2.getOrderIndex()))
                    .map(choice -> ChoiceResponse.fromEntity(choice, hideCorrectAnswers))
                    .toList());

            return response;
        }
    }
}
