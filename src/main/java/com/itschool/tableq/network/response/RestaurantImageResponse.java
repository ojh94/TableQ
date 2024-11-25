package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.RestaurantImage;
import com.itschool.tableq.network.response.base.FileResponse;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@Getter
public class RestaurantImageResponse extends FileResponse {
    private Long id;

    private LocalDateTime uploadTime;

    private RestaurantResponse restaurant;

    // 정적 팩토리 메서드 추가
    public static RestaurantImageResponse of(RestaurantImage restaurantImage) {
        Restaurant restaurant = restaurantImage.getRestaurant();

        return RestaurantImageResponse.builder()
                .id(restaurantImage.getId())
                .fileUrl(restaurantImage.getFileUrl())
                .restaurant(RestaurantResponse.of(restaurant))
                .build();
    }
}
