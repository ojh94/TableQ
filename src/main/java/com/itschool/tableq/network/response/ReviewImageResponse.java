package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.MenuItem;
import com.itschool.tableq.domain.ReviewImage;
import com.itschool.tableq.network.response.base.FileResponse;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class ReviewImageResponse extends FileResponse {
    private Long id;

    private Long reviewId;

    // 정적 팩토리 메서드 추가
    public static ReviewImageResponse of(ReviewImage reviewImage) {
        return ReviewImageResponse.builder()
                .fileUrl(reviewImage.getFileUrl())
                .reviewId(reviewImage.getReview().getId())
                .build();
    }
}
