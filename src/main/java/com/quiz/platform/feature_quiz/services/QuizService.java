package com.quiz.platform.feature_quiz.services;

import com.quiz.platform.feature_quiz.dtos.requests.QuestionRequest;
import com.quiz.platform.feature_quiz.dtos.requests.QuizRequest;
import com.quiz.platform.feature_quiz.dtos.responses.AdminStats;
import com.quiz.platform.feature_quiz.dtos.responses.CategoryStats;
import com.quiz.platform.feature_quiz.dtos.responses.QuizResponse;
import com.quiz.platform.feature_quiz.entities.postgres.Quiz;
import com.quiz.platform.shared.dtos.PagedResponse;
import com.quiz.platform.shared.security.UserInfo;
import java.util.List;
import java.util.Optional;

public interface QuizService {

    QuizResponse createQuiz(QuizRequest request);

    PagedResponse<QuizResponse> getQuizzes(int page, int size, String category, String level);

    QuizResponse getQuizById(String quizId, UserInfo userInfo, boolean showAnswers);

    QuizResponse updateQuiz(String quizId, QuizRequest request);

    void deleteQuiz(String quizId);

    QuizResponse addQuestion(String quizId, QuestionRequest request);

    Optional<Quiz> getQuizEntityById(String quizId);

    List<CategoryStats> getCategoryStats();

    List<String> getLevels();

    AdminStats getAdminStats();
}
