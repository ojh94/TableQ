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
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RestaurantAmenityService extends BaseService<RestaurantAmenityRequest, RestaurantAmenityResponse, RestaurantAmenity> {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    AmenityRepository amenityRepository;

    @Override
    protected RestaurantAmenityResponse response(RestaurantAmenity restaurantAmenity) {
        return RestaurantAmenityResponse.of(restaurantAmenity);
    }

    @Override
    public Header<RestaurantAmenityResponse> create(Header<RestaurantAmenityRequest> request) {
        RestaurantAmenityRequest restaurantAmenityRequest = request.getData();

        RestaurantAmenity restaurantAmenity = RestaurantAmenity.builder()
                .restaurant(restaurantRepository.findById(restaurantAmenityRequest.getRestaurant().getId())
                        .orElseThrow(() -> new EntityNotFoundException()))
                .amenity(amenityRepository.findById(restaurantAmenityRequest.getAmenity().getId())
                        .orElseThrow(() -> new EntityNotFoundException()))
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
        /*RestaurantAmenityRequest restaurantAmenityRequest = request.getData();

        RestaurantAmenity restaurantAmenity = baseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());

        restaurantAmenity.update(restaurantAmenityRequest);*/

        return Header.ERROR(this.getClass() + " : update is deprecated");
    }

    @Override
    public Header delete(Long id) {
        return null;
    }

    public Header<List<RestaurantAmenityResponse>> readByRestaurantId(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow();

        return Header.OK(
                responseList(
                        ((RestaurantAmenityRepository)baseRepository).findByRestaurant(restaurant)
                                .orElseThrow(() -> new EntityNotFoundException())));
    }
}
