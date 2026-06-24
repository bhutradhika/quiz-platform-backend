package com.quiz.platform.feature_attempt.services.impl;

import com.quiz.platform.feature_attempt.daos.AnswerDao;
import com.quiz.platform.feature_attempt.daos.AttemptDao;
import com.quiz.platform.feature_attempt.dtos.requests.AnswerRequest;
import com.quiz.platform.feature_attempt.dtos.responses.AnswerResultResponse;
import com.quiz.platform.feature_attempt.dtos.responses.AttemptResponse;
import com.quiz.platform.feature_attempt.entities.postgres.Answer;
import com.quiz.platform.feature_attempt.entities.postgres.Attempt;
import com.quiz.platform.feature_attempt.helpers.AttemptHelper;
import com.quiz.platform.feature_attempt.services.AttemptService;
import com.quiz.platform.feature_quiz.daos.ChoiceDao;
import com.quiz.platform.feature_quiz.daos.QuizDao;
import com.quiz.platform.feature_quiz.entities.postgres.Choice;
import com.quiz.platform.feature_quiz.entities.postgres.Quiz;
import com.quiz.platform.shared.dtos.PagedResponse;
import com.quiz.platform.shared.exception.BadRequestException;
import com.quiz.platform.shared.exception.ResourceNotFoundException;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link AttemptService} for managing quiz attempts.
 * Handles business logic for starting, completing, and querying quiz attempts.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AttemptServiceImpl implements AttemptService {

    private final AttemptDao attemptDao;
    private final QuizDao quizDao;
    private final AnswerDao answerDao;
    private final ChoiceDao choiceDao;
    private final AttemptHelper attemptHelper;

    @Override
    public AttemptResponse startAttempt(String userId, String quizId) {
        Quiz quiz = attemptHelper.fetchPublicQuiz(quizId);

        List<Attempt> completedAttempts = attemptDao.findByUserIdAndQuizId(userId, quizId).stream()
                .filter(a -> a.getCompletedAt() != null)
                .toList();

        if (!completedAttempts.isEmpty()) {
            throw new BadRequestException("You have already completed this quiz");
        }

        attemptHelper.validateNoIncompleteAttempt(userId, quizId);

        Double maxScore = attemptHelper.calculateMaxScore(quiz);
        Attempt attempt = attemptHelper.createNewAttempt(userId, quizId, maxScore);
        Attempt savedAttempt = attemptDao.save(attempt);

        return attemptHelper.convertToResponse(savedAttempt, quiz.getTitle(), quiz.getCategory().name(), quiz.getLevel() != null ? quiz.getLevel().name() : "BEGINNER");
    }

    @Override
    public AttemptResponse getIncompleteAttempt(String userId, String quizId) {
        Quiz quiz = attemptHelper.fetchPublicQuiz(quizId);
        Attempt attempt = attemptHelper.findIncompleteAttempt(userId, quizId);
        return attemptHelper.convertToResponse(attempt, quiz.getTitle(), quiz.getCategory().name(), quiz.getLevel() != null ? quiz.getLevel().name() : "BEGINNER");
    }

    @Override
    public AnswerResultResponse submitAnswer(String userId, String attemptId, AnswerRequest request) {
        Attempt attempt = attemptDao.findByIdAndUserId(attemptId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Attempt", "id", attemptId));

        if (attempt.getCompletedAt() != null) {
            throw new BadRequestException("This attempt has already been completed");
        }

        Answer existingAnswer = answerDao.findByAttemptIdAndQuestionId(attemptId, request.getQuestionId());
        if (existingAnswer != null) {
            throw new BadRequestException("You have already answered this question");
        }

        return attemptHelper.processAnswerAndGetResult(attempt, request);
    }

    @Override
    public AttemptResponse completeAttempt(String userId, String attemptId) {
        Attempt attempt = attemptDao.findByIdAndUserId(attemptId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Attempt", "id", attemptId));

        if (attempt.getCompletedAt() != null) {
            throw new BadRequestException("This attempt has already been completed");
        }

        attempt.setCompletedAt(new Date());
        Attempt savedAttempt = attemptDao.save(attempt);

        Quiz quiz = quizDao.findById(attempt.getQuizId())
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", attempt.getQuizId()));

        return attemptHelper.convertToResponse(savedAttempt, quiz.getTitle(), quiz.getCategory().name(), quiz.getLevel() != null ? quiz.getLevel().name() : "BEGINNER");
    }

    @Override
    public AttemptResponse getAttemptById(String userId, String attemptId) {
        Attempt attempt = attemptDao.findByIdAndUserId(attemptId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Attempt", "id", attemptId));

        Quiz quiz = quizDao.findById(attempt.getQuizId())
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", attempt.getQuizId()));

        return attemptHelper.convertToResponse(attempt, quiz.getTitle(), quiz.getCategory().name(), quiz.getLevel() != null ? quiz.getLevel().name() : "BEGINNER");
    }

    @Override
    public List<AnswerResultResponse> getAttemptAnswers(String userId, String attemptId) {
        Attempt attempt = attemptDao.findByIdAndUserId(attemptId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Attempt", "id", attemptId));

        List<Answer> answers = answerDao.findByAttemptId(attemptId);

        return answers.stream().map(answer -> {
            AnswerResultResponse response = new AnswerResultResponse();
            response.setQuestionId(answer.getQuestionId());
            response.setSelectedChoiceId(answer.getSelectedChoiceId());
            response.setIsCorrect(answer.getIsCorrect());

            choiceDao.findById(answer.getSelectedChoiceId()).ifPresent(choice -> {
                if (choice.getIsCorrect()) {
                    response.setCorrectChoiceId(choice.getId());
                }
            });

            choiceDao.findByQuestionId(answer.getQuestionId()).stream()
                    .filter(Choice::getIsCorrect)
                    .findFirst()
                    .ifPresent(correctChoice -> response.setCorrectChoiceId(correctChoice.getId()));

            return response;
        }).toList();
    }

    @Override
    public PagedResponse<AttemptResponse> getAttemptsByUser(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Attempt> attempts = attemptDao.findByUserId(userId, pageable);
        Page<AttemptResponse> response = attempts.map(attempt -> {
            Quiz quiz = quizDao.findById(attempt.getQuizId())
                    .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", attempt.getQuizId()));
            return attemptHelper.convertToResponse(attempt, quiz.getTitle(), quiz.getCategory().name(), quiz.getLevel() != null ? quiz.getLevel().name() : "BEGINNER");
        });

        return PagedResponse.fromPage(response);
    }

    @Override
    public List<String> getCompletedQuizIds(String userId) {
        return attemptDao.findByUserIdAndCompletedAtIsNotNull(userId).stream()
                .map(Attempt::getQuizId)
                .toList();
    }
}
