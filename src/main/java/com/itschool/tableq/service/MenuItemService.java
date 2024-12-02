package com.itschool.tableq.service;

import com.itschool.tableq.domain.MenuItem;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.RestaurantImage;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.MenuItemRequestWithFile;
import com.itschool.tableq.network.response.MenuItemResponse;
import com.itschool.tableq.repository.MenuItemRepository;
import com.itschool.tableq.repository.RestaurantRepository;
import com.itschool.tableq.service.base.BaseServiceWithS3;
import com.itschool.tableq.service.base.S3Service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuItemService extends BaseServiceWithS3<MenuItemRequestWithFile, MenuItemResponse, MenuItem> {

    private final RestaurantRepository restaurantRepository;

    // 생성자
    @Autowired
    public MenuItemService(MenuItemRepository baseRepository, S3Service s3Service, RestaurantRepository restaurantRepository) {
        super(baseRepository, s3Service);
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    protected MenuItemRepository getBaseRepository() {
        return (MenuItemRepository) baseRepository;
    }

    @Override
    protected String getDirectoryNameOfS3Bucket() {
        return "menu";
    }

    @Override
    protected MenuItemResponse response(MenuItem menuItem) {
        return MenuItemResponse.of(menuItem);
    }

    @Override
    protected MenuItem convertBaseEntityFromRequest(MenuItemRequestWithFile requestEntity) {
        return MenuItem.builder()
                .name(requestEntity.getName())
                .price(requestEntity.getPrice())
                .description(requestEntity.getDescription())
                .recommendation(requestEntity.getRecommendation())
                .restaurant(restaurantRepository.findById(requestEntity.getRestaurant().getId())
                        .orElseThrow(() -> new EntityNotFoundException()))
                .build();
    }

    public Header<List<MenuItemResponse>> readByRestaurantId(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow();

        return Header.OK(responseList(getBaseRepository().findByRestaurant(restaurant)));
    }

    public void deleteAllByRestaurant(Restaurant restaurant) {
        List<MenuItem> menuItemList = getBaseRepository().findByRestaurant(restaurant);

        for (MenuItem menuItem : menuItemList) {
            delete(menuItem.getId());
        }
    }
}
