package com.quiz.platform.feature_attempt.entities.postgres;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "attempts", indexes = {
    @Index(name = "idx_attempt_user_quiz", columnList = "user_id, quiz_id"),
    @Index(name = "idx_attempt_quiz_score", columnList = "quiz_id, score DESC")
})
public class Attempt {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "quiz_id", nullable = false)
    private String quizId;

    @Column(name = "score", nullable = false)
    private Double score = 0.0;

    @Column(name = "max_score", nullable = false)
    private Double maxScore = 0.0;

    @Column(name = "started_at", nullable = false)
    private Date startedAt;

    @Column(name = "completed_at")
    private Date completedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;
}
