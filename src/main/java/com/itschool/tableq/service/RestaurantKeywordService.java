package com.itschool.tableq.service;

import com.itschool.tableq.domain.*;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.Pagination;
import com.itschool.tableq.network.request.RestaurantKeywordRequest;
import com.itschool.tableq.network.response.ReservationResponse;
import com.itschool.tableq.network.response.RestaurantKeywordResponse;
import com.itschool.tableq.network.response.ReviewResponse;
import com.itschool.tableq.network.response.UserResponse;
import com.itschool.tableq.repository.ReservationRepository;
import com.itschool.tableq.repository.RestaurantKeywordsRepository;
import com.itschool.tableq.repository.RestaurantRepository;
import com.itschool.tableq.repository.ReviewRepository;
import com.itschool.tableq.service.base.BaseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RestaurantKeywordService extends BaseService<RestaurantKeywordRequest, RestaurantKeywordResponse, RestaurantKeyword> {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Override
    public Header<List<RestaurantKeywordResponse>> getPaginatedList(Pageable pageable) {
        Page<RestaurantKeyword> entities =  baseRepository.findAll(pageable);

        List<RestaurantKeywordResponse> restaurantKeywordResponsesList = entities.stream()
                .map(entity -> response(entity))
                .collect(Collectors.toList());

        Pagination pagination = Pagination.builder()
                .totalPages(entities.getTotalPages())
                .totalElements(entities.getTotalElements())
                .currentPage(entities.getNumber())
                .currentElements(entities.getNumberOfElements())
                .build();

        return Header.OK(restaurantKeywordResponsesList, pagination);
    }

    @Override
    protected RestaurantKeywordResponse response(RestaurantKeyword restaurantKeyword) {
        return new RestaurantKeywordResponse(restaurantKeyword);
    }

    protected List<RestaurantKeywordResponse> responseList(List<RestaurantKeyword> keywordList) {
        List<RestaurantKeywordResponse> responseList = new ArrayList<>();

        for(RestaurantKeyword keyword : keywordList){
            responseList.add(response(keyword));
        }

        return responseList;
    }
    @Override
    public Header<RestaurantKeywordResponse> create(Header<RestaurantKeywordRequest> request) {
        RestaurantKeywordRequest restaurantKeywordRequest = request.getData();

        RestaurantKeyword restaurantKeyword = RestaurantKeyword.builder()
                .restaurant(restaurantKeywordRequest.getRestaurant())
                .keyword(restaurantKeywordRequest.getKeyword())
                .build();

        baseRepository.save(restaurantKeyword);
        return Header.OK(response(restaurantKeyword));
    }

    @Override
    public Header<RestaurantKeywordResponse> read(Long id) {
        return Header.OK(response(baseRepository.findById(id).orElse(null)));
    }

    public Header<List<RestaurantKeywordResponse>> readByRestaurantId(Long restaurantId, Pageable pageable){
        // 식당을 예약한 손님 조회
        // --> 필요한 정보 : reservationNumber(대기번호), people(인원), User.contactNumber(예약자 전화번호)
        Restaurant restaurant = restaurantRepository.findById(restaurantId).get();
        List<RestaurantKeyword> keywordList = ((RestaurantKeywordsRepository)baseRepository)
                .findByRestaurant(restaurant).orElse(null);

        return Header.OK(responseList(keywordList));
    }

    @Override
    public Header<RestaurantKeywordResponse> update(Long id, Header<RestaurantKeywordRequest> request) {
        RestaurantKeywordRequest restaurantKeywordRequest = request.getData();
        RestaurantKeyword restaurantKeyword = baseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found"));
        restaurantKeyword.update(restaurantKeywordRequest);
        return Header.OK(response(restaurantKeyword));
    }

    @Override
    public Header delete(Long id) {
        return baseRepository.findById(id)
                .map(restaurantKeyword -> {
                    baseRepository.delete(restaurantKeyword);
                    return Header.OK(response(restaurantKeyword));
                })
                .orElseThrow(() -> new IllegalArgumentException("not found"));
    }
}
