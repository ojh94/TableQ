package com.itschool.tableq.service;

import com.itschool.tableq.domain.Review;
import com.itschool.tableq.domain.ReviewImage;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.Pagination;
import com.itschool.tableq.network.request.ReviewImageRequest;
import com.itschool.tableq.network.response.ReviewImageResponse;
import com.itschool.tableq.repository.ReviewImageRespository;
import com.itschool.tableq.repository.ReviewRepository;
import com.itschool.tableq.service.base.BaseServiceWithS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewImageService
        extends BaseServiceWithS3<ReviewImageRequest, ReviewImageResponse, ReviewImage> {
    @Autowired
    ReviewRepository reviewRepository;

    @Override
    public Header<List<ReviewImageResponse>> getPaginatedList(Pageable pageable) {
        Page<ReviewImage> entities =  baseRepository.findAll(pageable);

        List<ReviewImageResponse> reviewImageResponsesList = entities.stream()
                .map(entity -> response(entity))
                .collect(Collectors.toList());

        Pagination pagination = Pagination.builder()
                .totalPages(entities.getTotalPages())
                .totalElements(entities.getTotalElements())
                .currentPage(entities.getNumber())
                .currentElements(entities.getNumberOfElements())
                .build();

        return Header.OK(reviewImageResponsesList, pagination);
    }

    @Override
    protected ReviewImageResponse response(ReviewImage reviewImage) {
        return ReviewImageResponse.of(reviewImage);
    }

    protected List<ReviewImageResponse> responseList(List<ReviewImage> reviewImages){
        List<ReviewImageResponse> responseList = new ArrayList<>();

        for(ReviewImage reviewImage : reviewImages){
            responseList.add(response(reviewImage));
        }

        return responseList;
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
