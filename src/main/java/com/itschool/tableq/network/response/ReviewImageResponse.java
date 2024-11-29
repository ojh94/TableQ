package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.Review;
import com.itschool.tableq.domain.ReviewImage;
import com.itschool.tableq.network.response.base.FileResponse;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class ReviewImageResponse extends FileResponse {

    private Long id;

    private ReviewResponse review;

    // 정적 팩토리 메서드 추가
    public static ReviewImageResponse of(ReviewImage reviewImage) {
        Review review = reviewImage.getReview();

        return ReviewImageResponse.builder()
                .id(reviewImage.getId())
                .fileUrl(reviewImage.getFileUrl())
                .review(ReviewResponse.builder()
                        .id(review.getId())
                        .build())
                .build();
    }
}
