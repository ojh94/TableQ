package com.itschool.tableq.service;

import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.RestaurantAmenity;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.RestaurantAmenityRequest;
import com.itschool.tableq.network.response.RestaurantAmenityResponse;
import com.itschool.tableq.repository.AmenityRepository;
import com.itschool.tableq.repository.RestaurantAmenityRepository;
import com.itschool.tableq.repository.RestaurantRepository;
import com.itschool.tableq.service.base.BaseService;
import groovy.lang.DeprecationException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantAmenityService extends BaseService<RestaurantAmenityRequest, RestaurantAmenityResponse, RestaurantAmenity> {

    private final RestaurantRepository restaurantRepository;

    private final AmenityRepository amenityRepository;

    // 생성자
    @Autowired
    public RestaurantAmenityService(RestaurantAmenityRepository baseRepository,
                                    RestaurantRepository restaurantRepository,
                                    AmenityRepository amenityRepository) {
        super(baseRepository);
        this.restaurantRepository = restaurantRepository;
        this.amenityRepository = amenityRepository;
    }

    @Override
    protected RestaurantAmenityRepository getBaseRepository() {
        return (RestaurantAmenityRepository) baseRepository;
    }

    @Override
    protected RestaurantAmenityResponse response(RestaurantAmenity restaurantAmenity) {
        return RestaurantAmenityResponse.of(restaurantAmenity);
    }

    @Override
    protected RestaurantAmenity convertBaseEntityFromRequest(RestaurantAmenityRequest requestEntity) {
        return RestaurantAmenity.builder()
                .restaurant(restaurantRepository.findById(requestEntity.getRestaurant().getId())
                        .orElseThrow(() -> new EntityNotFoundException()))
                .amenity(amenityRepository.findById(requestEntity.getAmenity().getId())
                        .orElseThrow(() -> new EntityNotFoundException()))
                .build();
    }

    @Override
    @Deprecated
    public Header<RestaurantAmenityResponse> update(Long id, Header<RestaurantAmenityRequest> request) {
        throw new DeprecationException("연결 테이블이므로 delete, create API 이용할 것");
    }

    public Header<List<RestaurantAmenityResponse>> readByRestaurantId(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow();

        return Header.OK(responseList(getBaseRepository().findByRestaurant(restaurant)
                                .orElseThrow(() -> new EntityNotFoundException())));
    }
}
