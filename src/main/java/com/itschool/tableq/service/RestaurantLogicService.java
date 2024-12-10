package com.itschool.tableq.service;

import com.itschool.tableq.domain.MenuItem;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.RestaurantAmenity;
import com.itschool.tableq.domain.RestaurantKeyword;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.*;
import com.itschool.tableq.network.request.update.RestaurantUpdateAllRequest;
import com.itschool.tableq.network.response.RestaurantResponse;
import com.itschool.tableq.repository.BusinessInformationRepository;
import com.itschool.tableq.repository.RestaurantRepository;
import com.itschool.tableq.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RestaurantLogicService extends RestaurantService {

    private final RestaurantImageService restaurantImageService;

    private final OpeningHourService openingHourService;

    private final BreakHourService breakHourService;

    private final RestaurantAmenityService restaurantAmenityService;

    private final RestaurantKeywordService restaurantKeywordService;

    private final MenuItemService menuItemService;

    public RestaurantLogicService(RestaurantRepository baseRepository,
                                  BusinessInformationRepository businessInformationRepository,
                                  UserRepository userRepository,
                                  RestaurantImageService restaurantImageService,
                                  OpeningHourService openingHourService,
                                  BreakHourService breakHourService,
                                  RestaurantAmenityService restaurantAmenityService,
                                  RestaurantKeywordService restaurantKeywordService,
                                  MenuItemService menuItemService) {

        super(baseRepository, businessInformationRepository, userRepository);
        this.restaurantImageService = restaurantImageService;
        this.openingHourService = openingHourService;
        this.breakHourService = breakHourService;
        this.restaurantAmenityService = restaurantAmenityService;
        this.restaurantKeywordService = restaurantKeywordService;
        this.menuItemService = menuItemService;
    }

    // @AuthorCheck
    // AuthorCheck를 위해서는 /*@CreatedBy @LastModifiedBy 필요*/
    @Transactional
    public Header<RestaurantResponse> updateAll(Long id,
                                                RestaurantUpdateAllRequest request) {

        // 레스토랑 찾기
        Restaurant restaurant = getBaseRepository().findById(id)
                .orElseThrow(() -> new EntityNotFoundException());


        // 레트토랑 기본 정보 업데이트
        restaurant.update(request);


        // 운영시간 업데이트 (read 성공 : update, read 실패 : insert)
        for(OpeningHourRequest openingHourRequest : request.getOpeningHourList()) {
            openingHourRequest.setRestaurant(RestaurantRequest.builder().id(restaurant.getId()).build());
        }
        openingHourService.upsertListByDayOfWeek(restaurant, request.getOpeningHourList());


        // 브레이크 타임 업데이트 (read 성공 : update, read 실패 : insert)
        breakHourService.upsertListByDayOfWeek(restaurant, request.getBreakHourList());


        // 편의시설 업데이트 (delete 후 insert)
        for (RestaurantAmenityRequest restaurantAmenity : request.getRestaurantAmenityList())
            restaurantAmenity.setRestaurant(RestaurantRequest.builder().id(id).build());

        restaurantAmenityService.deleteAllByRestaurant(restaurant);
        restaurantAmenityService.createByList(request.getRestaurantAmenityList());


        // 키워드 업데이트 (delete 후 insert)
        for (RestaurantKeywordRequest restaurantKeyword : request.getRestaurantKeywordList())
            restaurantKeyword.setRestaurant(RestaurantRequest.builder().id(id).build());

        restaurantKeywordService.deleteAllByRestaurant(restaurant);
        restaurantKeywordService.createByList(request.getRestaurantKeywordList());

        // 메뉴 업데이트 (전부 삭제 후 다시 insert 향후 update로 변경 필요)
        if (request.getMenuItemList().size() >= 1) {
            for (MenuItemRequestWithFile menuItem : request.getMenuItemList())
                menuItem.setRestaurant(RestaurantRequest.builder().id(id).build());

            menuItemService.deleteAllByRestaurant(restaurant);
            menuItemService.createByList(request.getMenuItemList());
        }

        // 레스토랑 이미지 업데이트 (전부 삭제 후 다시 insert 향후 update로 변경 필요)
        if (request.getRestaurantImageList().size() >= 1) {
            for (RestaurantImageRequestWithFile restaurantImage : request.getRestaurantImageList())
                restaurantImage.setRestaurant(RestaurantRequest.builder().id(id).build());
            restaurantImageService.deleteAllByRestaurant(restaurant);
            restaurantImageService.createByList(request.getRestaurantImageList());
        }

        return Header.OK(response(restaurant));
    }
}