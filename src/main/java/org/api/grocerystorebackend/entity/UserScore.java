package org.api.grocerystorebackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.api.grocerystorebackend.enums.PotentialType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "frequency", nullable = false)
    private int frequency;

    @Column(name = "avg_spending", precision = 10, scale = 2, nullable = false)
    private BigDecimal avgSpending;

    @Column(name = "avg_rating", precision = 3, scale = 2, nullable = false)
    private BigDecimal avgRating;

    @Column(name = "total_score", precision = 5, scale = 2, nullable = false)
    private BigDecimal totalScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "potential_type", columnDefinition = "varchar(20)", nullable = false)
    private PotentialType potentialType;

    @Column(name = "month_year", columnDefinition = "varchar(7)", nullable = false)
    private String monthYear;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
