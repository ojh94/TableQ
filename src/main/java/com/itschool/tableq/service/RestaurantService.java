package com.itschool.tableq.service;

import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.RestaurantRequest;
import com.itschool.tableq.network.response.RestaurantResponse;
import com.itschool.tableq.repository.RestaurantRepository;
import com.itschool.tableq.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RestaurantService extends BaseService<RestaurantRequest, RestaurantResponse, Restaurant> {

    @Override
    protected RestaurantResponse response(Restaurant restaurant) {
        return RestaurantResponse.of(restaurant);
    }

    @Override
    public Header<RestaurantResponse> create(Header<RestaurantRequest> request) {
        RestaurantRequest restaurantRequest = request.getData();

        Restaurant restaurant = Restaurant.builder()
                .name(restaurantRequest.getName())
                .address(restaurantRequest.getAddress())
                .information(restaurantRequest.getIntroduction())
                .contactNumber(restaurantRequest.getContact_number())
                .isAvailable(restaurantRequest.isAvailable())
                .businessInformation(restaurantRequest.getBusinessInformation())
                .build();

        baseRepository.save(restaurant);
        return Header.OK(response(restaurant));
    }

    @Override
    public Header<RestaurantResponse> read(Long id) {
        return Header.OK(response(baseRepository.findById(id).orElse(null)));
    }

    @Override
    @Transactional
    public Header<RestaurantResponse> update(Long id, Header<RestaurantRequest> request) {
        RestaurantRequest restaurantRequest = request.getData();

        Restaurant restaurant = baseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found"));
        restaurant.update(restaurantRequest);

        return Header.OK(response(restaurant));
    }

    @Override
    public Header delete(Long id) {
        return baseRepository.findById(id)
                .map(restaurant -> {
                    baseRepository.delete(restaurant);
                    return Header.OK(response(restaurant));
                }).orElseThrow(() -> new RuntimeException("Restaurant delete fail"));
    }

    public Header<List<RestaurantResponse>> searchByName(String keyword, Pageable pageable) {
        Page<Restaurant> searchedList = ((RestaurantRepository)baseRepository).searchByName(keyword, pageable);

        return convertPageToList(searchedList);
    }

    public Header<List<RestaurantResponse>> searchByAddress(String address, Pageable pageable) {
        Page<Restaurant> searchedList = ((RestaurantRepository)baseRepository).searchByAddress(address, pageable);

        return convertPageToList(searchedList);
    }
}
