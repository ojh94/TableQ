package com.itschool.tableq.service;

import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.BreakHourRequest;
import com.itschool.tableq.network.request.OpeningHourRequest;
import com.itschool.tableq.network.request.RestaurantRequest;
import com.itschool.tableq.network.request.update.RestaurantUpdateAllRequest;
import com.itschool.tableq.network.response.RestaurantResponse;
import com.itschool.tableq.repository.BusinessInformationRepository;
import com.itschool.tableq.repository.RestaurantRepository;
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
                                  RestaurantImageService restaurantImageService,
                                  OpeningHourService openingHourService,
                                  BreakHourService breakHourService,
                                  RestaurantAmenityService restaurantAmenityService,
                                  RestaurantKeywordService restaurantKeywordService,
                                  MenuItemService menuItemService) {

        super(baseRepository, businessInformationRepository);
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

        try {
            
            // 레스토랑 찾기
            Restaurant restaurant = getBaseRepository().findById(id)
                    .orElseThrow(() -> new EntityNotFoundException());
            
            
            // 레트토랑 기본 정보 업데이트
            restaurant.update(request);


            // 운영시간 업데이트 (delete 후 insert)
            for (OpeningHourRequest openingHour : request.getOpeningHourList())
                openingHour.setRestaurant(RestaurantRequest.builder().id(id).build());

            openingHourService.deleteAllByRestaurantId(id);
            openingHourService.createByList(request.getOpeningHourList());


            // 브레이크 타임 업데이트 (delete 후 insert)
            for (BreakHourRequest breakHour : request.getBreakHourList())
                breakHour.setRestaurant(RestaurantRequest.builder().id(id).build());

            breakHourService.deleteAllByRestaurantId(id);
            breakHourService.createByList(request.getBreakHourList());


            // 편의시설 업데이트 (delete 후 insert)
            // restaurantAmenityService.upsertList(request.getRestaurantAmenityList());
            restaurantAmenityService.deleteAllByRestaurantId(id);
            restaurantAmenityService.createByList(request.getRestaurantAmenityList());


            // 키워드 업데이트 (delete 후 insert)
            // restaurantKeywordService.upsertList(request.getRestaurantKeywordList());
            restaurantKeywordService.deleteAllByRestaurantId(id);
            restaurantKeywordService.createByList(request.getRestaurantKeywordList());


            // 메뉴 업데이트 (있으면 업데이트 없으면 insert)
            // menuItemService.upsertList(request.getMenuItemList());

            // 레스토랑 이미지 업데이트 (있으면 업데이트 없으면 insert)
            // restaurantImageService.upsertList(request.getRestaurantImageList());


            return Header.OK(response(restaurant));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
