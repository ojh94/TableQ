package com.itschool.tableq.network.request;

import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.User;
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

    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;

    private Restaurant restaurant;

    private User user;
}
