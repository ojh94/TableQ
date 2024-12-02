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

    private Integer starRating;

    private UserResponse user;

    private RestaurantResponse restaurant;

    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;

    public static ReviewResponse of(Review review){
        User user = review.getUser();
        Restaurant restaurant = review.getRestaurant();

        return ReviewResponse.builder()
                .id(review.getId())
                .content(review.getContent())
                .starRating(review.getStarRating())
                .user(UserResponse.builder()
                        .id(user.getId())
                        .nickname(user.getNickname())
                        .email(user.getEmail())
                        .build())
                .restaurant(RestaurantResponse.builder()
                        .id(restaurant.getId())
                        .name(restaurant.getName())
                        .build())
                .createdAt(review.getCreatedAt())
                .lastModifiedAt(review.getLastModifiedAt())
                .build();
    }
}
