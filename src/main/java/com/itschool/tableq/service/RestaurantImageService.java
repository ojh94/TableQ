package com.itschool.tableq.service;

import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.RestaurantImage;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.RestaurantImageRequestWithFile;
import com.itschool.tableq.network.response.RestaurantImageResponse;
import com.itschool.tableq.repository.RestaurantImageRepository;
import com.itschool.tableq.repository.RestaurantRepository;
import com.itschool.tableq.service.base.BaseServiceWithS3;
import com.itschool.tableq.service.base.S3Service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantImageService extends BaseServiceWithS3<RestaurantImageRequestWithFile, RestaurantImageResponse, RestaurantImage> {

    private final RestaurantRepository restaurantRepository;

    // 생성자
    @Autowired
    public RestaurantImageService(RestaurantImageRepository baseRepository, S3Service s3Service, RestaurantRepository restaurantRepository) {
        super(baseRepository, s3Service);
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    protected RestaurantImageRepository getBaseRepository() {
        return (RestaurantImageRepository) baseRepository;
    }

    @Override
    protected RestaurantImageResponse response(RestaurantImage restaurantImage) {
        return RestaurantImageResponse.of(restaurantImage);
    }

    @Override
    protected RestaurantImage convertBaseEntityFromRequest(RestaurantImageRequestWithFile requestEntity) {
        return RestaurantImage.builder()
                .restaurant(restaurantRepository.findById(requestEntity.getRestaurant().getId())
                        .orElseThrow(() -> new EntityNotFoundException()))
                .build();
    }

    @Override
    protected String getDirectoryNameOfS3Bucket() {
        return "restaurant-image";
    }

    public Header<List<RestaurantImageResponse>> readByRestaurantId(Long restaurantId){
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException());
        List<RestaurantImage> imageList = getBaseRepository().findByRestaurant(restaurant);

        return Header.OK(responseList(imageList));
    }
}
