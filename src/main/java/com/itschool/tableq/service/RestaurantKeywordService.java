package com.itschool.tableq.service;

import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.RestaurantKeyword;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.RestaurantKeywordRequest;
import com.itschool.tableq.network.response.RestaurantKeywordResponse;
import com.itschool.tableq.repository.KeywordRepository;
import com.itschool.tableq.repository.RestaurantKeywordsRepository;
import com.itschool.tableq.repository.RestaurantRepository;
import com.itschool.tableq.service.base.BaseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RestaurantKeywordService extends BaseService<RestaurantKeywordRequest, RestaurantKeywordResponse, RestaurantKeyword> {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    KeywordRepository keywordRepository;

    @Override
    protected RestaurantKeywordResponse response(RestaurantKeyword restaurantKeyword) {
        return RestaurantKeywordResponse.of(restaurantKeyword);
    }

    @Override
    public Header<RestaurantKeywordResponse> create(Header<RestaurantKeywordRequest> request) {
        RestaurantKeywordRequest restaurantKeywordRequest = request.getData();

        RestaurantKeyword restaurantKeyword = RestaurantKeyword.builder()
                .restaurant(restaurantRepository.findById(restaurantKeywordRequest.getRestaurant().getId())
                        .orElseThrow(() -> new EntityNotFoundException()))
                .keyword(keywordRepository.findById(restaurantKeywordRequest.getKeyword().getId())
                        .orElseThrow(() -> new EntityNotFoundException()))
                .build();

        baseRepository.save(restaurantKeyword);
        return Header.OK(response(restaurantKeyword));
    }

    @Override
    public Header<RestaurantKeywordResponse> read(Long id) {
        return Header.OK(response(baseRepository.findById(id).orElse(null)));
    }

    public Header<List<RestaurantKeywordResponse>> readByRestaurantId(Long restaurantId){
        // 식당을 예약한 손님 조회
        // --> 필요한 정보 : reservationNumber(대기번호), people(인원), User.contactNumber(예약자 전화번호)
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException());

        List<RestaurantKeyword> keywordList = ((RestaurantKeywordsRepository)baseRepository).findByRestaurant(restaurant);

        return Header.OK(responseList(keywordList));
    }

    @Override
    public Header<RestaurantKeywordResponse> update(Long id, Header<RestaurantKeywordRequest> request) {
        /*RestaurantKeywordRequest restaurantKeywordRequest = request.getData();
        RestaurantKeyword restaurantKeyword = baseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found"));
        restaurantKeyword.update(restaurantKeywordRequest);
        return Header.OK(response(restaurantKeyword));*/

        return Header.ERROR(this.getClass() + " : update is deprecated");
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
