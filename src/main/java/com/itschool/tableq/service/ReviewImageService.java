package com.itschool.tableq.service;

import com.itschool.tableq.domain.Review;
import com.itschool.tableq.domain.ReviewImage;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.ReviewImageRequest;
import com.itschool.tableq.network.response.ReviewImageResponse;
import com.itschool.tableq.repository.ReviewImageRespository;
import com.itschool.tableq.repository.ReviewRepository;
import com.itschool.tableq.service.base.BaseServiceWithS3;
import com.itschool.tableq.util.FileUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ReviewImageService extends BaseServiceWithS3<ReviewImageRequest, ReviewImageResponse, ReviewImage> {

    @Autowired
    ReviewRepository reviewRepository;

    private static final String DIRECTORY_NAME = "reviewImage";

    @Override
    protected ReviewImageResponse response(ReviewImage reviewImage) {
        return ReviewImageResponse.of(reviewImage);
    }

    @Transactional
    @Override
    public Header<ReviewImageResponse> create(Header<ReviewImageRequest> request) {
        try {
            ReviewImageRequest reviewImageRequest = request.getData();

            ReviewImage entity = ReviewImage.builder()
                    .review(reviewRepository.findById(reviewImageRequest.getReview().getId())
                            .orElseThrow(()-> new EntityNotFoundException()))
                    .build();

            entity = baseRepository.save(entity);

            String fileUrl = uploadFile(reviewImageRequest.getFile(), DIRECTORY_NAME,
                    entity.getId() + FileUtil.getFileExtension(request.getData().getFile()));

            entity.updateFileUrl(fileUrl);

            ReviewImageResponse menuItemResponse = response(entity);

            return Header.OK(menuItemResponse);
        } catch (Exception e){
            throw new RuntimeException(this.getClass() + "의 create 메소드 실패", e);
        }
    }

    @Override
    public Header<ReviewImageResponse> read(Long id) {
        return Header.OK(response(baseRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException())));
    }

    public Header<List<ReviewImageResponse>> readByReview(Long reviewId){
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException());

        List<ReviewImage> reviewImages = ((ReviewImageRespository)baseRepository).findByReview(review)
                .orElseThrow(() -> new EntityNotFoundException());

        return Header.OK(responseList(reviewImages));
    }

    @Transactional
    @Override
    public Header<ReviewImageResponse> update(Long id, Header<ReviewImageRequest> request) {
        try {
            ReviewImageRequest reviewImageRequest = request.getData();

            MultipartFile file = reviewImageRequest.getFile();

            ReviewImage findEntity = baseRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("not found"));

            String existingUrl = findEntity.getFileUrl();

            if (reviewImageRequest.isNeedFileChange()) { // 프론트에서 파일 변경이 필요하다 한 경우
                if (file.isEmpty() && existingUrl != null) { // 대체 파일이 없고 기존 url이 있는 경우
                    deleteFile(existingUrl); // 기존 파일 삭제
                    findEntity.updateFileUrl(null); // 파일 URL 삭제
                } else if (!file.isEmpty()) { // 대체 파일이 있는 경우
                    String newUrl = updateFile(existingUrl, file); // 새 파일 업로드
                    findEntity.updateFileUrl(newUrl); // 새 파일 URL 설정
                }
            }

            // findEntity.updateWithoutFileUrl(reviewImageRequest);

            return Header.OK(response(findEntity));
        } catch (Exception e){
            throw new RuntimeException(this.getClass() + "의 update 메소드 실패", e);
        }
    }

    @Transactional
    @Override
    public Header delete(Long id) {
        try {
            ReviewImage entity = baseRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("not found"));

            if(entity.getFileUrl() != null)
                deleteFile(entity.getFileUrl());

            baseRepository.delete(entity);

            return Header.OK(response(entity));
        } catch (Exception e){
            throw new RuntimeException(this.getClass() + "의 delete 메소드 실패", e);
        }
    }
}
