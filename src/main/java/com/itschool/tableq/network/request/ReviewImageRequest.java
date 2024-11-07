package com.itschool.tableq.network.request;

import com.itschool.tableq.domain.Review;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ReviewImageRequest {
    private Long id;

    private String filename;

    private String path;

    private Review review;
}
