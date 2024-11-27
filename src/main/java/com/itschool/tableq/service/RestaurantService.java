package com.itschool.tableq.service;

import com.itschool.tableq.domain.Restaurant;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class RestaurantService extends BaseService<RestaurantRequest, RestaurantResponse, Restaurant> {

    private final BusinessInformationRepository businessInformationRepository;

    private final RestaurantImageRepository restaurantImageRepository;

    private final OpeningHourRepository openingHourRepository;

    private final BreakHourRepository breakHourRepository;

    private final RestaurantAmenityRepository restaurantAmenityRepository;

    private final RestaurantKeywordRepository restaurantKeywordRepository;

    private final MenuItemRepository menuItemRepository;

    // 생성자
    @Autowired
    public RestaurantService(RestaurantRepository baseRepository,
                                BusinessInformationRepository businessInformationRepository,
                                RestaurantImageRepository restaurantImageRepository,
                                OpeningHourRepository openingHourRepository,
                                BreakHourRepository breakHourRepository,
                                RestaurantAmenityRepository restaurantAmenityRepository,
                                RestaurantKeywordRepository restaurantKeywordRepository,
                                MenuItemRepository menuItemRepository) {
        super(baseRepository);
        this.businessInformationRepository = businessInformationRepository;
        this.restaurantImageRepository = restaurantImageRepository;
        this.openingHourRepository = openingHourRepository;
        this.breakHourRepository = breakHourRepository;
        this.restaurantAmenityRepository = restaurantAmenityRepository;
        this.restaurantKeywordRepository = restaurantKeywordRepository;
        this.menuItemRepository = menuItemRepository;
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
    public Header<RestaurantResponse> create(Header<RestaurantRequest> request) {
        RestaurantRequest restaurantRequest = request.getData();

        Restaurant restaurant = Restaurant.builder()
                .name(restaurantRequest.getName())
                .address(restaurantRequest.getAddress())
                .information(restaurantRequest.getInformation())
                .contactNumber(restaurantRequest.getContact_number())
                .isAvailable(restaurantRequest.isAvailable())
                .businessInformation(businessInformationRepository.findById(restaurantRequest.getBusinessInformation().getId())
                        .orElseThrow(() -> new EntityNotFoundException()))
                .build();

        getBaseRepository().save(restaurant);
        return Header.OK(response(restaurant));
    }

    @Override
    public Header<RestaurantResponse> read(Long id) {
        return Header.OK(response(getBaseRepository().findById(id).orElse(null)));
    }

    @Override
    @Transactional
    public Header<RestaurantResponse> update(Long id, Header<RestaurantRequest> request) {
        RestaurantRequest restaurantRequest = request.getData();

        Restaurant restaurant = getBaseRepository().findById(id).orElseThrow(() -> new IllegalArgumentException("not found"));
        restaurant.update(restaurantRequest);

        return Header.OK(response(restaurant));
    }

    @Override
    public Header delete(Long id) {
        return getBaseRepository().findById(id)
                .map(restaurant -> {
                    getBaseRepository().delete(restaurant);
                    return Header.OK(response(restaurant));
                }).orElseThrow(() -> new RuntimeException("Restaurant delete fail"));
    }

    // 별점 높은 순 (review 테이블에 있는 리스트들의 평균을 내야함, JPQL을 써서 Repository 구현이 필요)
    public Header<List<RestaurantResponse>> findRestaurantsOrderByReservationCountDesc(Pageable pageable) {
        return convertPageToList(((RestaurantRepository)baseRepository).findRestaurantsOrderByReservationCountDesc(pageable));
    }

    // 추천 순 : 리뷰 건별 별점 총합 (JPQL을 써서 GroupBy로 총점을 계산)
    public Header<List<RestaurantResponse>> findTopRatedRestaurants(Pageable pageable) {
        return convertPageToList(((RestaurantRepository)baseRepository).findTopRatedRestaurants(pageable));
    }

    public Header<List<RestaurantResponse>> searchByName(String keyword, Pageable pageable) {
        Page<Restaurant> searchedList = ((RestaurantRepository)baseRepository).searchByName(keyword, pageable);

        return convertPageToList(searchedList);
    }

    public Header<List<RestaurantResponse>> searchByAddress(String address, Pageable pageable) {
        Page<Restaurant> searchedList = ((RestaurantRepository)baseRepository).searchByAddress(address, pageable);

        return convertPageToList(searchedList);
    }

    // @AuthorCheck // AuthorCheck를 위해서는 /*@CreatedBy @LastModifiedBy 필요*/
    @Transactional
    public Header<RestaurantResponse> updateAll(Long id, RestaurantUpdateAllRequest request, List<MultipartFile> restaurantImages, List<MultipartFile> menuImages) {

        Restaurant restaurant = getBaseRepository().findById(id)
                .orElseThrow(() -> new EntityNotFoundException());





        return Header.OK(response(restaurant));
    }
}
