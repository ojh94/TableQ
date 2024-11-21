package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.Review;
import com.itschool.tableq.domain.ReviewImage;
import com.itschool.tableq.network.response.base.FileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReviewImageResponse extends FileResponse {
    private Long id;

    private Long reviewId;

    public ReviewImageResponse(ReviewImage reviewImage){
        this.fileUrl = reviewImage.getFileUrl();
        this.reviewId = reviewImage.getReview().getId();
    }
}
