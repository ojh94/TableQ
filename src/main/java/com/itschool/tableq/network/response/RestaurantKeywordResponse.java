package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.Keyword;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.RestaurantKeyword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RestaurantKeywordResponse {

    private Long id;

    private KeywordResponse keyword;

    private RestaurantResponse restaurant;

    public static RestaurantKeywordResponse of(RestaurantKeyword restaurantKeyword) {
        Restaurant restaurant = restaurantKeyword.getRestaurant();
        Keyword keyword = restaurantKeyword.getKeyword();

        return RestaurantKeywordResponse.builder()
                .id(restaurantKeyword.getId())
                .restaurant(RestaurantResponse.builder()
                        .id(restaurant.getId())
                        .build())
                .keyword(KeywordResponse.of(keyword))
                .build();
    }
}
