package com.itschool.tableq.network.request;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewRequest {
    private Long id;

    private String content;

    private Integer starRating;

    private RestaurantRequest restaurant;

    private UserRequest user;
}
