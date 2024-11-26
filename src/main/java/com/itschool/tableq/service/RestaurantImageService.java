package com.itschool.tableq.service;

import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.RestaurantImage;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.RestaurantImageRequest;
import com.itschool.tableq.network.response.RestaurantImageResponse;
import com.itschool.tableq.repository.RestaurantImageRepository;
import com.itschool.tableq.repository.RestaurantRepository;
import com.itschool.tableq.service.base.BaseServiceWithS3;
import com.itschool.tableq.util.FileUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class RestaurantImageService extends BaseServiceWithS3<RestaurantImageRequest, RestaurantImageResponse, RestaurantImage> {
    @Autowired
    RestaurantRepository restaurantRepository;

    private static final String DIRECTORY_NAME = "restaurantImage";

    @Override
    protected RestaurantImageResponse response(RestaurantImage restaurantImage) {
        return RestaurantImageResponse.of(restaurantImage);
    }

    @Transactional
    @Override
    public Header<RestaurantImageResponse> create(Header<RestaurantImageRequest> request) {
        try {
            RestaurantImageRequest menuItemRequest = request.getData();

            RestaurantImage entity = RestaurantImage.builder()
                    .restaurant(restaurantRepository.findById(menuItemRequest.getRestaurant().getId())
                            .orElseThrow(() -> new EntityNotFoundException()))
                    .build();

            entity = baseRepository.save(entity);

            String fileUrl = uploadFile(menuItemRequest.getFile(), DIRECTORY_NAME,
                    entity.getId() + FileUtil.getFileExtension(request.getData().getFile()));

            entity.updateFileUrl(fileUrl);

            RestaurantImageResponse restaurantImageResponse = response(entity);

            return Header.OK(restaurantImageResponse);
        } catch (Exception e){
            throw new RuntimeException(this.getClass() + "의 create 메소드 실패", e);
        }
    }

    @Override
    public Header<RestaurantImageResponse> read(Long id) {
        return Header.OK(response(baseRepository.findById(id).orElse(null)));
    }

    public Header<List<RestaurantImageResponse>> readByRestaurantId(Long restaurantId){
        Restaurant restaurant = restaurantRepository.findById(restaurantId).get();
        List<RestaurantImage> imageList = ((RestaurantImageRepository)baseRepository).findByRestaurant(restaurant).orElse(null);

        return Header.OK(responseList(imageList));
    }

    @Transactional
    @Override
    public Header<RestaurantImageResponse> update(Long id, Header<RestaurantImageRequest> request) {
        try {
            RestaurantImageRequest restaurantImageRequest = request.getData();

            MultipartFile file = restaurantImageRequest.getFile();

            RestaurantImage findEntity = baseRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("not found"));

            String existingUrl = findEntity.getFileUrl();

            if (restaurantImageRequest.isNeedFileChange()) { // 프론트에서 파일 변경이 필요하다 한 경우
                if (file.isEmpty() && existingUrl != null) { // 대체 파일이 없고 기존 url이 있는 경우
                    deleteFile(existingUrl); // 기존 파일 삭제
                    findEntity.updateFileUrl(null); // 파일 URL 삭제
                } else if (!file.isEmpty()) { // 대체 파일이 있는 경우
                    String newUrl = updateFile(existingUrl, file); // 새 파일 업로드
                    findEntity.updateFileUrl(newUrl); // 새 파일 URL 설정
                }
            }

            // findEntity.updateWithoutFileUrl(restaurantImageRequest);

            return Header.OK(response(findEntity));
        } catch (Exception e){
            throw new RuntimeException(this.getClass() + "의 update 메소드 실패", e);
        }
    }

    @Transactional
    @Override
    public Header delete(Long id) {
        try {
            RestaurantImage entity = baseRepository.findById(id)
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
