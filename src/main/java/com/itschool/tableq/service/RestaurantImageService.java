package com.itschool.tableq.service;

import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.RestaurantImage;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.RestaurantImageRequest;
import com.itschool.tableq.network.response.RestaurantImageResponse;
import com.itschool.tableq.repository.RestaurantImageRepository;
import com.itschool.tableq.repository.RestaurantRepository;
import com.itschool.tableq.service.base.BaseServiceWithS3;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class RestaurantImageService extends
        BaseServiceWithS3<RestaurantImageRequest, RestaurantImageResponse, RestaurantImage> {
    @Autowired
    RestaurantRepository restaurantRepository;

    @Override
    protected RestaurantImageResponse response(RestaurantImage restaurantImage) {
        return RestaurantImageResponse.of(restaurantImage);
    }

    @Override
    public Header<RestaurantImageResponse> create(Header<RestaurantImageRequest> request) {
        RestaurantImageRequest imageRequest = request.getData();
        RestaurantImage restaurantImage = RestaurantImage.builder()
                .fileUrl(imageRequest.getFile().getName())
                .restaurant(imageRequest.getRestaurant())
                .build();

        baseRepository.save(restaurantImage);
        return Header.OK(response(restaurantImage));
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

    @Override
    public Header<RestaurantImageResponse> update(Long id, Header<RestaurantImageRequest> request) {
        RestaurantImageRequest restaurantImageRequest = request.getData();

        RestaurantImage restaurantImage = baseRepository.findById(id).orElse(null);

        restaurantImage.update(restaurantImage.getFileUrl());

        return Header.OK(response(restaurantImage));
    }

    @Override
    public Header delete(Long id) {
        return baseRepository.findById(id).map(
                restaurantImage -> {
                    baseRepository.delete(restaurantImage);
                    return Header.OK(response(restaurantImage));
                }
        ).orElseThrow(() -> new RuntimeException("Image delete fail"));
    }
}
