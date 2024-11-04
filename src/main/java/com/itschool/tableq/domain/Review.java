package com.itschool.tableq.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "reviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer starRating;

    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", updatable = false)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    @Builder
    public Review(String content, Integer starRating, LocalDateTime createdAt, LocalDateTime lastModifiedAt, Restaurant restaurant, User user){
        this.content = content;
        this.starRating = starRating;
        this.restaurant = restaurant;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.user = user;
    }
}
