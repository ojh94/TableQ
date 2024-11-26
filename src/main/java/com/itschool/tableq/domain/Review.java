package com.itschool.tableq.domain;

import com.itschool.tableq.domain.base.AuditableEntity;
import com.itschool.tableq.network.request.ReviewRequest;
import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
@Table(name = "reviews")
public class Review extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer starRating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", updatable = false)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY) // (fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    @Builder
    public Review(String content, Integer starRating, Restaurant restaurant, User user){
        this.content = content;
        this.starRating = starRating;
        this.restaurant = restaurant;
        this.user = user;
    }

    public void update(ReviewRequest reviewRequest) {
        this.content = reviewRequest.getContent() == null? this.content : reviewRequest.getContent();;
        this.starRating = reviewRequest.getStarRating() == null? this.starRating : reviewRequest.getStarRating();;
    }
}
