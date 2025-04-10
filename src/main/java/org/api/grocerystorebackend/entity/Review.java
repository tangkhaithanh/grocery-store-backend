package org.api.grocerystorebackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="product_id", nullable = false)
    @JsonBackReference
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private Order order;

    @Column(name = "rating")
    private int rating;

    @Column(name = "comment", columnDefinition = "text")
    private String comment;

    @Column(name = "image_url", columnDefinition = "varchar(255)")
    private String imageUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
