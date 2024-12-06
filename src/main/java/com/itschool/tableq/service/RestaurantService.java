package com.itschool.tableq.service;

import com.itschool.tableq.domain.BusinessInformation;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.User;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.RestaurantRequest;
import com.itschool.tableq.network.request.update.RestaurantUpdateAllRequest;
import com.itschool.tableq.network.response.RestaurantResponse;
import com.itschool.tableq.repository.*;
import com.itschool.tableq.service.base.BaseService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Component
public class RestaurantService extends BaseService<RestaurantRequest, RestaurantResponse, Restaurant> {

    private final UserRepository userRepository;

    private final BusinessInformationRepository businessInformationRepository;

    // 생성자
    @Autowired
    public RestaurantService(RestaurantRepository baseRepository,
                             BusinessInformationRepository businessInformationRepository,
                             UserRepository userRepository) {
        super(baseRepository);
        this.businessInformationRepository = businessInformationRepository;
        this.userRepository = userRepository;
    }

    @Override
    protected RestaurantRepository getBaseRepository() {
        return (RestaurantRepository) baseRepository;
    }

    @Override
    protected RestaurantResponse response(Restaurant restaurant) {
        return RestaurantResponse.of(restaurant);
    }


    @Override
    protected Restaurant convertBaseEntityFromRequest(RestaurantRequest requestEntity) {
        return Restaurant.builder()
                .name(requestEntity.getName())
                .address(requestEntity.getAddress())
                .information(requestEntity.getInformation())
                .contactNumber(requestEntity.getContactNumber())
                .isAvailable(requestEntity.getIsAvailable())
                .businessInformation(businessInformationRepository.findById(requestEntity.getBusinessInformation().getId())
                        .orElseThrow(() -> new EntityNotFoundException()))
                .build();
    }

    // 별점 높은 순 (review 테이블에 있는 리스트들의 평균을 내야함, JPQL을 써서 Repository 구현이 필요)
    public Header<List<RestaurantResponse>> findRestaurantsOrderByReservationCountDesc(Pageable pageable) {
        return convertPageToList(getBaseRepository().findRestaurantsOrderByReservationCountDesc(pageable));
    }

    // 추천 순 : 리뷰 건별 별점 총합 (JPQL을 써서 GroupBy로 총점을 계산)
    public Header<List<RestaurantResponse>> findTopRatedRestaurants(Pageable pageable) {
        return convertPageToList(getBaseRepository().findTopRatedRestaurants(pageable));
    }

    public Header<List<RestaurantResponse>> searchByName(String keyword, Pageable pageable) {
        Page<Restaurant> searchedList = getBaseRepository().searchByName(keyword, pageable);

        return convertPageToList(searchedList);
    }

    public Header<List<RestaurantResponse>> searchByAddress(String address, Pageable pageable) {
        Page<Restaurant> searchedList = getBaseRepository().searchByAddress(address, pageable);

        return convertPageToList(searchedList);
    }

    public Header<List<RestaurantResponse>> readAllMyRestaurant(Long userId){
        List<BusinessInformation> myInfo = businessInformationRepository.findByUser(userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException())
        );

        List<Restaurant> myRestaurants = new ArrayList<>();

        for(BusinessInformation businessInformation : myInfo){
            myRestaurants.add(getBaseRepository().findByBusinessInformation(businessInformation)
                    .orElse(null)
            );
        }

        return Header.OK(responseList(myRestaurants));
    }
}
