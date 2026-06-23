package com.quiz.platform.feature_quiz.helpers;

import com.quiz.platform.feature_quiz.daos.ChoiceDao;
import com.quiz.platform.feature_quiz.daos.QuestionDao;
import com.quiz.platform.feature_quiz.daos.QuizDao;
import com.quiz.platform.feature_quiz.dtos.requests.QuestionRequest;
import com.quiz.platform.feature_quiz.dtos.responses.QuizResponse;
import com.quiz.platform.feature_quiz.entities.postgres.Choice;
import com.quiz.platform.feature_quiz.entities.postgres.Question;
import com.quiz.platform.feature_quiz.entities.postgres.Quiz;
import com.quiz.platform.feature_quiz.entities.postgres.QuizCategory;
import com.quiz.platform.feature_quiz.entities.postgres.QuizLevel;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class QuizHelper {

    private final QuizDao quizDao;
    private final QuestionDao questionDao;
    private final ChoiceDao choiceDao;

    public QuizHelper(QuizDao quizDao, QuestionDao questionDao, ChoiceDao choiceDao) {
        this.quizDao = quizDao;
        this.questionDao = questionDao;
        this.choiceDao = choiceDao;
    }

    public int getPointsPerQuestion(QuizLevel level) {
        return switch (level) {
            case BEGINNER -> 1;
            case INTERMEDIATE -> 2;
            case ADVANCED -> 3;
            case EXPERT -> 4;
        };
    }

    public QuizResponse convertToResponse(Quiz quiz, boolean showAnswers) {
        List<Question> questions = questionDao.findByQuizId(quiz.getId());

        QuizResponse response = new QuizResponse();
        response.setId(quiz.getId());
        response.setTitle(quiz.getTitle());
        response.setDescription(quiz.getDescription());
        response.setCategory(quiz.getCategory().name());
        response.setLevel(quiz.getLevel().name());
        response.setIsPublic(quiz.getIsPublic());
        response.setQuestionCount(questions.size());
        response.setTotalPoints(questions.stream().mapToInt(Question::getPoints).sum());
        response.setPointsPerQuestion(getPointsPerQuestion(quiz.getLevel()));

        response.setQuestions(questions.stream()
                .map(q -> {
                    List<Choice> choices = choiceDao.findByQuestionId(q.getId());
                    return QuizResponse.QuestionResponse.fromEntity(q, choices, !showAnswers);
                })
                .toList());

        return response;
    }

    public QuizResponse convertToListResponse(Quiz quiz) {
        List<Question> questions = questionDao.findByQuizId(quiz.getId());

        QuizResponse response = new QuizResponse();
        response.setId(quiz.getId());
        response.setTitle(quiz.getTitle());
        response.setDescription(quiz.getDescription());
        response.setCategory(quiz.getCategory().name());
        response.setLevel(quiz.getLevel().name());
        response.setIsPublic(quiz.getIsPublic());
        response.setQuestionCount(questions.size());
        response.setTotalPoints(questions.stream().mapToInt(Question::getPoints).sum());
        response.setPointsPerQuestion(getPointsPerQuestion(quiz.getLevel()));
        return response;
    }

    public Page<Quiz> fetchQuizzesByCategory(String category, Pageable pageable) {
        if (category == null || category.isEmpty()) {
            return quizDao.findByIsPublicTrue(pageable);
        }

        try {
            QuizCategory quizCategory = QuizCategory.valueOf(category.toUpperCase());
            return quizDao.findByIsPublicTrueAndCategory(quizCategory, pageable);
        } catch (IllegalArgumentException e) {
            return quizDao.findByIsPublicTrue(pageable);
        }
    }

    public Question createQuestionFromRequest(QuestionRequest request, String quizId) {
        Question question = new Question();
        question.setQuizId(quizId);
        question.setText(request.getText());
        question.setType(request.getType());
        question.setPoints(request.getPoints());

        return question;
    }

    public List<Choice> createChoicesFromRequest(QuestionRequest request, String questionId) {
        int orderIndex = 0;
        for (QuestionRequest.ChoiceRequest choiceRequest : request.getChoices()) {
            Choice choice = new Choice();
            choice.setQuestionId(questionId);
            choice.setText(choiceRequest.getText());
            choice.setIsCorrect(choiceRequest.getIsCorrect());
            choice.setOrderIndex(orderIndex++);
            choiceDao.save(choice);
        }

        return choiceDao.findByQuestionId(questionId);
    }
}
