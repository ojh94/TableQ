package com.itschool.tableq.service;

import com.itschool.tableq.domain.Review;
import com.itschool.tableq.domain.ReviewImage;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.ReviewImageRequest;
import com.itschool.tableq.network.response.ReviewImageResponse;
import com.itschool.tableq.repository.ReviewImageRespository;
import com.itschool.tableq.repository.ReviewRepository;
import com.itschool.tableq.service.base.BaseServiceWithS3;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ReviewImageService
        extends BaseServiceWithS3<ReviewImageRequest, ReviewImageResponse, ReviewImage> {
    @Autowired
    ReviewRepository reviewRepository;

    @Override
    protected ReviewImageResponse response(ReviewImage reviewImage) {
        return ReviewImageResponse.of(reviewImage);
    }

    @Override
    public Header<ReviewImageResponse> create(Header<ReviewImageRequest> request) {
        ReviewImageRequest imageRequest = request.getData();
        ReviewImage reviewImage = ReviewImage.builder()
                .fileUrl(imageRequest.getFile().getName())
                .review(imageRequest.getReview())
                .build();

        baseRepository.save(reviewImage);
        return Header.OK(response(reviewImage));
    }

    @Override
    public Header<ReviewImageResponse> read(Long id) {
        return Header.OK(response(baseRepository.findById(id).orElse(null)));
    }

    public Header<List<ReviewImageResponse>> readByReview(Long reviewId){
        Review review = reviewRepository.findById(reviewId).get();
        List<ReviewImage> reviewImages = ((ReviewImageRespository)baseRepository).findByReview(review).orElse(null);

        return Header.OK(responseList(reviewImages));
    }
    @Override
    public Header<ReviewImageResponse> update(Long id, Header<ReviewImageRequest> request) {
        return null;
    }

    @Override
    public Header delete(Long id) {
        return baseRepository.findById(id).map(reviewImage -> {
            baseRepository.delete(reviewImage);
            return Header.OK(response(reviewImage));
        }).orElseThrow(() -> new RuntimeException("Image delete fail"));
    }
}
