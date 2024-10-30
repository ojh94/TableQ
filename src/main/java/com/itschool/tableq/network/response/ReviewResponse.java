package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.Review;
import com.itschool.tableq.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReviewResponse {
    private Long id;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;

    private Restaurant restaurant;

    private User user;

    public ReviewResponse(Review review){
        this.id = review.getId();
        this.content = review.getContent();
        this.createdAt = review.getCreatedAt();
        this.lastModifiedAt = review.getLastModifiedAt();
        this.restaurant = review.getRestaurant();
        this.user = review.getUser();
    }

    public ReviewResponse(List<Review> reviews) {
    }
}
