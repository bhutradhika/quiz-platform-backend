package com.quiz.platform.feature_attempt.services;

import com.quiz.platform.feature_attempt.dtos.requests.AnswerRequest;
import com.quiz.platform.feature_attempt.dtos.responses.AnswerResultResponse;
import com.quiz.platform.feature_attempt.dtos.responses.AttemptResponse;
import com.quiz.platform.shared.dtos.PagedResponse;
import java.util.List;

public interface AttemptService {

    AttemptResponse startAttempt(String userId, String quizId);

    AttemptResponse getIncompleteAttempt(String userId, String quizId);

    AnswerResultResponse submitAnswer(String userId, String attemptId, AnswerRequest request);

    AttemptResponse completeAttempt(String userId, String attemptId);

    AttemptResponse getAttemptById(String userId, String attemptId);

    List<AnswerResultResponse> getAttemptAnswers(String userId, String attemptId);

    PagedResponse<AttemptResponse> getAttemptsByUser(String userId, int page, int size);

    List<String> getCompletedQuizIds(String userId);
}
