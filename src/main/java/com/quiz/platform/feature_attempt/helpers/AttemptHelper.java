package com.quiz.platform.feature_attempt.helpers;

import com.quiz.platform.feature_attempt.daos.AnswerDao;
import com.quiz.platform.feature_attempt.daos.AttemptDao;
import com.quiz.platform.feature_attempt.dtos.requests.AnswerRequest;
import com.quiz.platform.feature_attempt.dtos.responses.AnswerResultResponse;
import com.quiz.platform.feature_attempt.dtos.responses.AttemptResponse;
import com.quiz.platform.feature_attempt.entities.postgres.Answer;
import com.quiz.platform.feature_attempt.entities.postgres.Attempt;
import com.quiz.platform.feature_quiz.daos.ChoiceDao;
import com.quiz.platform.feature_quiz.daos.QuestionDao;
import com.quiz.platform.feature_quiz.daos.QuizDao;
import com.quiz.platform.feature_quiz.entities.postgres.Choice;
import com.quiz.platform.feature_quiz.entities.postgres.Question;
import com.quiz.platform.feature_quiz.entities.postgres.Quiz;
import com.quiz.platform.shared.exception.BadRequestException;
import com.quiz.platform.shared.exception.ResourceNotFoundException;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Helper component for quiz attempt operations.
 * Provides reusable business logic methods for attempt processing.
 */
@Component
public class AttemptHelper {

    private final QuizDao quizDao;
    private final AttemptDao attemptDao;
    private final QuestionDao questionDao;
    private final ChoiceDao choiceDao;
    private final AnswerDao answerDao;

    public AttemptHelper(QuizDao quizDao, AttemptDao attemptDao, QuestionDao questionDao, ChoiceDao choiceDao, AnswerDao answerDao) {
        this.quizDao = quizDao;
        this.attemptDao = attemptDao;
        this.questionDao = questionDao;
        this.choiceDao = choiceDao;
        this.answerDao = answerDao;
    }

    /**
     * Fetches a public quiz by its ID.
     *
     * @param quizId the ID of the quiz
     * @return the quiz entity
     * @throws ResourceNotFoundException if quiz not found
     * @throws BadRequestException if quiz is not public
     */
    public Quiz fetchPublicQuiz(String quizId) {
        Quiz quiz = quizDao.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", quizId));

        if (!quiz.getIsPublic()) {
            throw new BadRequestException("This quiz is not public");
        }

        return quiz;
    }

    /**
     * Validates that no incomplete attempt exists for a user and quiz.
     *
     * @param userId the ID of the user
     * @param quizId the ID of the quiz
     * @throws BadRequestException if an incomplete attempt already exists
     */
    public void validateNoIncompleteAttempt(String userId, String quizId) {
        if (attemptDao.existsByUserIdAndQuizIdAndCompletedAtIsNull(userId, quizId)) {
            throw new BadRequestException("You already have an incomplete attempt for this quiz");
        }
    }

    /**
     * Creates a new attempt entity with initial values.
     *
     * @param userId the ID of the user
     * @param quizId the ID of the quiz
     * @param maxScore the maximum possible score for the quiz
     * @return the created attempt entity (not persisted)
     */
    public Attempt createNewAttempt(String userId, String quizId, Double maxScore) {
        Attempt attempt = new Attempt();
        attempt.setUserId(userId);
        attempt.setQuizId(quizId);
        attempt.setStartedAt(new Date());
        attempt.setMaxScore(maxScore);
        return attempt;
    }

    /**
     * Finds an incomplete attempt for a user and quiz.
     *
     * @param userId the ID of the user
     * @param quizId the ID of the quiz
     * @return the incomplete attempt entity
     * @throws BadRequestException if no incomplete attempt is found
     */
    public Attempt findIncompleteAttempt(String userId, String quizId) {
        List<Attempt> incompleteAttempts = attemptDao.findByUserIdAndQuizId(userId, quizId).stream()
                .filter(a -> a.getCompletedAt() == null)
                .toList();

        if (incompleteAttempts.isEmpty()) {
            throw new BadRequestException("No incomplete attempt found. Please start a new attempt.");
        }

        return incompleteAttempts.get(0);
    }

    /**
     * Processes an answer submission and returns the result.
     * Saves the answer and updates the attempt score if correct.
     *
     * @param attempt the attempt entity
     * @param request the answer request
     * @return the answer result with correctness feedback
     * @throws ResourceNotFoundException if question or choice not found
     */
    public AnswerResultResponse processAnswerAndGetResult(Attempt attempt, AnswerRequest request) {
        Question question = questionDao.findById(request.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", request.getQuestionId()));

        Choice selectedChoice = choiceDao.findById(request.getChoiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Choice", "id", request.getChoiceId()));

        Choice correctChoice = choiceDao.findByQuestionId(question.getId()).stream()
                .filter(Choice::getIsCorrect)
                .findFirst()
                .orElse(null);

        boolean isCorrect = selectedChoice.getIsCorrect();

        saveAnswer(attempt.getId(), question.getId(), selectedChoice.getId(), isCorrect);

        if (isCorrect) {
            attempt.setScore(attempt.getScore() + question.getPoints());
        }

        AnswerResultResponse response = new AnswerResultResponse();
        response.setQuestionId(request.getQuestionId());
        response.setSelectedChoiceId(request.getChoiceId());
        response.setIsCorrect(isCorrect);
        response.setCorrectChoiceId(correctChoice != null ? correctChoice.getId() : null);

        return response;
    }

    /**
     * Calculates the maximum possible score for a quiz.
     *
     * @param quiz the quiz entity
     * @return the sum of all question points
     */
    public Double calculateMaxScore(Quiz quiz) {
        return questionDao.findByQuizId(quiz.getId()).stream()
                .mapToDouble(Question::getPoints)
                .sum();
    }

    /**
     * Converts an attempt entity to a response DTO.
     *
     * @param attempt the attempt entity
     * @param quizTitle the title of the quiz
     * @param category the category of the quiz
     * @param level the level of the quiz
     * @return the attempt response DTO
     */
    public AttemptResponse convertToResponse(Attempt attempt, String quizTitle, String category, String level) {
        AttemptResponse response = new AttemptResponse();
        response.setId(attempt.getId());
        response.setQuizId(attempt.getQuizId());
        response.setQuizTitle(quizTitle);
        response.setCategory(category);
        response.setLevel(level);
        response.setScore(attempt.getScore());
        response.setMaxScore(attempt.getMaxScore());
        response.setStartedAt(attempt.getStartedAt());
        response.setCompletedAt(attempt.getCompletedAt());
        response.setPercentage(calculatePercentage(attempt.getScore(), attempt.getMaxScore()));
        return response;
    }

    private void saveAnswer(String attemptId, String questionId, String selectedChoiceId, Boolean isCorrect) {
        Answer answer = new Answer();
        answer.setAttemptId(attemptId);
        answer.setQuestionId(questionId);
        answer.setSelectedChoiceId(selectedChoiceId);
        answer.setIsCorrect(isCorrect);
        answerDao.save(answer);
    }

    private double calculatePercentage(Double score, Double maxScore) {
        if (maxScore == null || maxScore <= 0) {
            return 0.0;
        }
        return (score / maxScore) * 100;
    }
}
