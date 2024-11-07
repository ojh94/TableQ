package com.itschool.tableq.service;

import com.itschool.tableq.domain.RestaurantAmenity;
import com.itschool.tableq.domain.User;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.Pagination;
import com.itschool.tableq.network.request.RestaurantAmenityRequest;
import com.itschool.tableq.network.response.RestaurantAmenityResponse;
import com.itschool.tableq.network.response.UserResponse;
import com.itschool.tableq.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RestaurantAmenityService extends BaseService<RestaurantAmenityRequest, RestaurantAmenityResponse, RestaurantAmenity> {
    @Override
    public Header<List<RestaurantAmenityResponse>> getPaginatedList(Pageable pageable) {
        Page<RestaurantAmenity> entities =  baseRepository.findAll(pageable);

        List<RestaurantAmenityResponse> restaurantAmenityResponsesList = entities.stream()
                .map(entity -> response(entity))
                .collect(Collectors.toList());

        Pagination pagination = Pagination.builder()
                .totalPages(entities.getTotalPages())
                .totalElements(entities.getTotalElements())
                .currentPage(entities.getNumber())
                .currentElements(entities.getNumberOfElements())
                .build();

        return Header.OK(restaurantAmenityResponsesList, pagination);
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
