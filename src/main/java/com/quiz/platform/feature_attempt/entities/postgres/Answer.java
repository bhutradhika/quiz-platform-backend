package com.quiz.platform.feature_attempt.entities.postgres;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "answers", indexes = {
    @Index(name = "idx_answer_attempt", columnList = "attempt_id"),
    @Index(name = "idx_answer_question", columnList = "question_id")
})
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private String id;

    @Column(name = "attempt_id", nullable = false)
    private String attemptId;

    @Column(name = "question_id", nullable = false)
    private String questionId;

    @Column(name = "selected_choice_id")
    private String selectedChoiceId;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect = false;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;
}
