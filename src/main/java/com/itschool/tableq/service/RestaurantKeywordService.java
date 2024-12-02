package com.itschool.tableq.service;

import com.itschool.tableq.domain.BreakHour;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.RestaurantKeyword;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.RestaurantKeywordRequest;
import com.itschool.tableq.network.response.RestaurantKeywordResponse;
import com.itschool.tableq.repository.KeywordRepository;
import com.itschool.tableq.repository.RestaurantKeywordRepository;
import com.itschool.tableq.repository.RestaurantRepository;
import com.itschool.tableq.service.base.BaseService;
import groovy.lang.DeprecationException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantKeywordService extends BaseService<RestaurantKeywordRequest, RestaurantKeywordResponse, RestaurantKeyword> {

    private final RestaurantRepository restaurantRepository;

    private final KeywordRepository keywordRepository;

    // 생성자
    @Autowired
    public RestaurantKeywordService(RestaurantKeywordRepository baseRepository,
                                    RestaurantRepository restaurantRepository,
                                    KeywordRepository keywordRepository) {
        super(baseRepository);
        this.restaurantRepository = restaurantRepository;
        this.keywordRepository = keywordRepository;
    }


    @Override
    protected RestaurantKeywordRepository getBaseRepository() {
        return (RestaurantKeywordRepository) baseRepository;
    }

    @Override
    protected RestaurantKeywordResponse response(RestaurantKeyword restaurantKeyword) {
        return RestaurantKeywordResponse.of(restaurantKeyword);
    }

    @Override
    protected RestaurantKeyword convertBaseEntityFromRequest(RestaurantKeywordRequest requestEntity) {
        return RestaurantKeyword.builder()
                .restaurant(restaurantRepository.findById(requestEntity.getRestaurant().getId())
                        .orElseThrow(() -> new EntityNotFoundException()))
                .keyword(keywordRepository.findById(requestEntity.getKeyword().getId())
                        .orElseThrow(() -> new EntityNotFoundException()))
                .build();
    }

    @Override
    @Deprecated
    public Header<RestaurantKeywordResponse> update(Long id, Header<RestaurantKeywordRequest> request) {
        throw new DeprecationException("연결 테이블이므로 delete, create API 이용할 것");
    }

    public Header<List<RestaurantKeywordResponse>> readByRestaurantId(Long restaurantId){
        // 식당을 예약한 손님 조회
        // --> 필요한 정보 : reservationNumber(대기번호), people(인원), User.contactNumber(예약자 전화번호)
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException());

        List<RestaurantKeyword> keywordList = getBaseRepository().findByRestaurant(restaurant);

        return Header.OK(responseList(keywordList));
    }

    public void deleteAllByRestaurantId(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());

        List<RestaurantKeyword> restaurantKeywords = getBaseRepository().findAllByRestaurant(restaurant);

        getBaseRepository().deleteAll(restaurantKeywords);
    }
}
