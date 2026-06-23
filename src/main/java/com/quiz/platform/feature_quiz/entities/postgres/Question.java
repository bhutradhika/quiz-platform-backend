package com.quiz.platform.feature_quiz.entities.postgres;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "questions", indexes = {
    @Index(name = "idx_question_quiz", columnList = "quiz_id")
})
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private String id;

    @Column(name = "quiz_id", nullable = false)
    private String quizId;

    @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private QuizType type = QuizType.MULTIPLE_CHOICE;

    @Column(name = "points", nullable = false)
    private Integer points = 1;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
}
