package com.itschool.tableq.service;

import com.itschool.tableq.domain.RestaurantAmenity;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.RestaurantAmenityRequest;
import com.itschool.tableq.network.response.RestaurantAmenityResponse;
import com.itschool.tableq.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RestaurantAmenityService extends BaseService<RestaurantAmenityRequest, RestaurantAmenityResponse, RestaurantAmenity> {
    @Override
    public Header<List<RestaurantAmenityResponse>> getPaginatedList(Pageable pageable) {
        return null;
    }

    @Override
    protected RestaurantAmenityResponse response(RestaurantAmenity restaurantAmenity) {
        return new RestaurantAmenityResponse(restaurantAmenity);
    }

    @Override
    public Header<RestaurantAmenityResponse> create(Header<RestaurantAmenityRequest> request) {
        RestaurantAmenityRequest restaurantAmenityRequest = request.getData();

        RestaurantAmenity restaurantAmenity = RestaurantAmenity.builder()
                .restaurant(restaurantAmenityRequest.getRestaurant())
                .amenity(restaurantAmenityRequest.getAmenity())
                .build();
        baseRepository.save(restaurantAmenity);
        return Header.OK(response(restaurantAmenity));
    }

    @Override
    public Header<RestaurantAmenityResponse> read(Long id) {
        return Header.OK(response(baseRepository.findById(id).orElse(null)));
    }

    @Override
    public Header<RestaurantAmenityResponse> update(Long id, Header<RestaurantAmenityRequest> request) {
        RestaurantAmenityRequest restaurantAmenityRequest = request.getData();
        RestaurantAmenity restaurantAmenity = baseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("RestaurantAmenity not found"));
        restaurantAmenity.update(restaurantAmenityRequest);
        return Header.OK(response(restaurantAmenity));
    }

    @Override
    public Header delete(Long id) {
        return null;
    }
}
