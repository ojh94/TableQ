package com.itschool.tableq.service;

import com.itschool.tableq.domain.Review;
import com.itschool.tableq.domain.ReviewImage;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.ReviewImageRequestWithFile;
import com.itschool.tableq.network.response.ReviewImageResponse;
import com.itschool.tableq.repository.ReviewImageRespository;
import com.itschool.tableq.repository.ReviewRepository;
import com.itschool.tableq.service.base.BaseServiceWithS3;
import com.itschool.tableq.service.base.S3Service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ReviewImageService extends BaseServiceWithS3<ReviewImageRequestWithFile, ReviewImageResponse, ReviewImage> {

    private final ReviewRepository reviewRepository;

    // 생성자
    @Autowired
    public ReviewImageService(ReviewImageRespository baseRepository, S3Service s3Service, ReviewRepository reviewRepository) {
        super(baseRepository, s3Service);
        this.reviewRepository = reviewRepository;
    }

    @Override
    protected ReviewImageRespository getBaseRepository() {
        return (ReviewImageRespository) baseRepository;
    }

    @Override
    protected ReviewImageResponse response(ReviewImage reviewImage) {
        return ReviewImageResponse.of(reviewImage);
    }

    @Override
    protected ReviewImage convertBaseEntityFromRequest(ReviewImageRequestWithFile requestEntity) {
        return ReviewImage.builder()
                .review(reviewRepository.findById(requestEntity.getReview().getId())
                        .orElseThrow(()-> new EntityNotFoundException()))
                .build();
    }

    @Override
    protected String getDirectoryNameOfS3Bucket() {
        return "review-image";
    }

    public Header<List<ReviewImageResponse>> readByReview(Long reviewId){
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException());

        List<ReviewImage> reviewImages = getBaseRepository().findByReview(review)
                .orElseThrow(() -> new EntityNotFoundException());

        return Header.OK(responseList(reviewImages));
    }
}
