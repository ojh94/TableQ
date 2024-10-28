package com.itschool.tableq.service;

import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.response.RestaurantResponse;
import com.itschool.tableq.network.request.RestaurantRequest;
import com.itschool.tableq.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class RestaurantService extends BaseService<RestaurantRequest, RestaurantResponse, Restaurant> {



    @Override
    protected RestaurantResponse response(Restaurant restaurant) {
        return new RestaurantResponse(restaurant);
    }

    @Override
    public Header<RestaurantResponse> create(Header<RestaurantRequest> request) {
        RestaurantRequest restaurantRequest = request.getData();

        Restaurant restaurant = Restaurant.builder()
                .name(restaurantRequest.getName())
                .address(restaurantRequest.getAddress())
                .introduction(restaurantRequest.getIntroduction())
                .contactNumber(restaurantRequest.getContact_number())
                .isAvailable(restaurantRequest.isAvailable())
                .createdAt(LocalDateTime.now())
                .lastModifiedAt(LocalDateTime.now())
                .buisnessInformation(restaurantRequest.getBuisnessInformation())
                .build();

        baseRepository.save(restaurant);
        return Header.OK(response(restaurant));
    }

    @Override
    public Header<RestaurantResponse> read(Long id) {
        return Header.OK(response(baseRepository.findById(id).orElse(null)));
    }

    @Override
    public Header<RestaurantResponse> update(Long id, Header<RestaurantRequest> request) {
        return null;
    }

    @Override
    public Header delete(Long id) {
        return baseRepository.findById(id)
                .map(restaurant -> {
                    baseRepository.delete(restaurant);
                    return Header.OK(response(restaurant));
                }).orElseThrow(() -> new RuntimeException("Restaurant delete fail"));
    }
}
