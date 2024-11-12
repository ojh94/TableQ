package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.Keyword;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.RestaurantKeyword;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RestaurantKeywordResponse {
    private Long id;
    private Long restaurantId;
    private Keyword keyword;

    public RestaurantKeywordResponse(RestaurantKeyword restaurantKeyword) {
        this.id = restaurantKeyword.getId();
        this.restaurantId = restaurantKeyword.getRestaurant().getId();
        this.keyword = restaurantKeyword.getKeyword();
    }
}
