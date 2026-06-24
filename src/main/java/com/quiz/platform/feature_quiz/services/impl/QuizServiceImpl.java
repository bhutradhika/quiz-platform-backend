package com.quiz.platform.feature_quiz.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.quiz.platform.feature_quiz.daos.ChoiceDao;
import com.quiz.platform.feature_quiz.daos.QuestionDao;
import com.quiz.platform.feature_quiz.daos.QuizDao;
import com.quiz.platform.feature_quiz.dtos.requests.QuestionRequest;
import com.quiz.platform.feature_quiz.dtos.requests.QuizRequest;
import com.quiz.platform.feature_quiz.dtos.responses.AdminStats;
import com.quiz.platform.feature_quiz.dtos.responses.CategoryStats;
import com.quiz.platform.feature_quiz.dtos.responses.QuizResponse;
import com.quiz.platform.feature_quiz.entities.postgres.Choice;
import com.quiz.platform.feature_quiz.entities.postgres.Question;
import com.quiz.platform.feature_quiz.entities.postgres.Quiz;
import com.quiz.platform.feature_quiz.entities.postgres.QuizCategory;
import com.quiz.platform.feature_quiz.entities.postgres.QuizLevel;
import com.quiz.platform.feature_quiz.helpers.QuizHelper;
import com.quiz.platform.feature_quiz.services.QuizService;
import com.quiz.platform.shared.cache.CacheKeys;
import com.quiz.platform.shared.cache.CacheService;
import com.quiz.platform.shared.dtos.PagedResponse;
import com.quiz.platform.shared.exception.BadRequestException;
import com.quiz.platform.shared.exception.ResourceNotFoundException;
import com.quiz.platform.shared.security.UserInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link QuizService} for quiz operations.
 * Handles business logic for creating, retrieving, and managing quizzes with Redis caching.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private static final long SIX_HOURS_SECONDS = 6 * 3600L;

    private final QuizDao quizDao;
    private final QuestionDao questionDao;
    private final ChoiceDao choiceDao;
    private final QuizHelper quizHelper;
    private final CacheService cacheService;

    @Override
    public QuizResponse createQuiz(QuizRequest request) {
        Quiz quiz = new Quiz();
        quiz.setTitle(request.getTitle());
        quiz.setDescription(request.getDescription());
        quiz.setCategory(request.getCategory());
        quiz.setLevel(request.getLevel() != null ? request.getLevel() : QuizLevel.BEGINNER);
        quiz.setIsPublic(request.getIsPublic());

        Quiz savedQuiz = quizDao.save(quiz);
        invalidateCategoryStatsCache();
        return quizHelper.convertToResponse(savedQuiz, false);
    }

    @Override
    public PagedResponse<QuizResponse> getQuizzes(int page, int size, String category, String level) {
        if (level != null && !level.isEmpty()) {
            return getQuizzesByCategoryAndLevel(category, level, page, size);
        }
        return getAllQuizzes(page, size, category);
    }

    @Override
    public QuizResponse getQuizById(String quizId, UserInfo userInfo, boolean showAnswers) {
        if (!showAnswers) {
            String cacheKey = CacheKeys.quizDetailKey(quizId);
            QuizResponse cached = cacheService.get(cacheKey, QuizResponse.class);
            if (cached != null) {
                return cached;
            }
        }

        Quiz quiz = quizDao.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", quizId));

        if (!quiz.getIsPublic()) {
            throw new BadRequestException("This quiz is not public");
        }

        QuizResponse response = quizHelper.convertToResponse(quiz, showAnswers);
        
        if (!showAnswers) {
            cacheService.set(CacheKeys.quizDetailKey(quizId), response, SIX_HOURS_SECONDS);
        }
        
        return response;
    }

    @Override
    public QuizResponse updateQuiz(String quizId, QuizRequest request) {
        Quiz quiz = quizDao.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", quizId));

        quiz.setTitle(request.getTitle());
        quiz.setDescription(request.getDescription());
        quiz.setCategory(request.getCategory());
        quiz.setIsPublic(request.getIsPublic());

        Quiz updatedQuiz = quizDao.save(quiz);
        cacheService.delete(CacheKeys.quizDetailKey(quizId));
        invalidateCategoryStatsCache();
        return quizHelper.convertToResponse(updatedQuiz, true);
    }

    @Override
    public void deleteQuiz(String quizId) {
        Quiz quiz = quizDao.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", quizId));

        quizDao.delete(quiz);
        cacheService.delete(CacheKeys.quizDetailKey(quizId));
        cacheService.delete(CacheKeys.leaderboardByQuizKey(quizId));
        invalidateCategoryStatsCache();
    }

    @Override
    public QuizResponse addQuestion(String quizId, QuestionRequest request) {
        Quiz quiz = quizDao.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", quizId));

        int points = quizHelper.getPointsPerQuestion(quiz.getLevel());

        Question question = new Question();
        question.setQuizId(quizId);
        question.setText(request.getText());
        question.setType(request.getType());
        question.setPoints(points);
        Question savedQuestion = questionDao.save(question);

        int orderIndex = 0;
        for (QuestionRequest.ChoiceRequest choiceRequest : request.getChoices()) {
            Choice choice = new Choice();
            choice.setQuestionId(savedQuestion.getId());
            choice.setText(choiceRequest.getText());
            choice.setIsCorrect(choiceRequest.getIsCorrect());
            choice.setOrderIndex(orderIndex++);
            choiceDao.save(choice);
        }

        cacheService.delete(CacheKeys.quizDetailKey(quizId));
        return quizHelper.convertToResponse(quiz, true);
    }

    @Override
    public Optional<Quiz> getQuizEntityById(String quizId) {
        return quizDao.findById(quizId);
    }

    @Override
    public List<CategoryStats> getCategoryStats() {
        List<CategoryStats> cached = cacheService.get(CacheKeys.CATEGORY_STATS, new TypeReference<List<CategoryStats>>() {});
        if (cached != null) {
            return cached;
        }

        List<CategoryStats> stats = new ArrayList<>();

        for (QuizCategory category : QuizCategory.values()) {
            List<CategoryStats.LevelStats> levelStats = new ArrayList<>();
            long total = 0;

            for (QuizLevel level : QuizLevel.values()) {
                long count = quizDao.countByCategoryAndLevelAndIsPublicTrue(category, level);
                if (count > 0) {
                    levelStats.add(new CategoryStats.LevelStats(level.name(), count));
                    total += count;
                }
            }

            if (total > 0) {
                stats.add(new CategoryStats(category.name(), levelStats));
            }
        }

        cacheService.set(CacheKeys.CATEGORY_STATS, stats, Long.MAX_VALUE);
        return stats;
    }

    @Override
    public List<String> getLevels() {
        return Arrays.stream(QuizLevel.values())
                .map(Enum::name)
                .toList();
    }

    @Override
    public AdminStats getAdminStats() {
        AdminStats stats = new AdminStats();
        stats.setTotalQuizzes(quizDao.count());
        stats.setTotalQuestions(questionDao.count());
        stats.setTotalCategories((long) QuizCategory.values().length);
        return stats;
    }

    private PagedResponse<QuizResponse> getAllQuizzes(int page, int size, String category) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Quiz> quizzes = quizHelper.fetchQuizzesByCategory(category, pageable);
        Page<QuizResponse> response = quizzes.map(q -> quizHelper.convertToListResponse(q));
        return PagedResponse.fromPage(response);
    }

    private PagedResponse<QuizResponse> getQuizzesByCategoryAndLevel(String category, String level, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        QuizCategory quizCategory = null;
        QuizLevel quizLevel = null;

        if (category != null && !category.isEmpty()) {
            try {
                quizCategory = QuizCategory.valueOf(category.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid category: " + category);
            }
        }

        if (level != null && !level.isEmpty()) {
            try {
                quizLevel = QuizLevel.valueOf(level.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid level: " + level);
            }
        }

        Page<Quiz> quizzes = quizDao.findByCategoryAndLevel(quizCategory, quizLevel, pageable);
        Page<QuizResponse> response = quizzes.map(q -> quizHelper.convertToListResponse(q));
        return PagedResponse.fromPage(response);
    }

    private void invalidateCategoryStatsCache() {
        cacheService.delete(CacheKeys.CATEGORY_STATS);
    }
}
