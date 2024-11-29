package com.itschool.tableq.network.request.update;

import com.itschool.tableq.network.request.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class RestaurantUpdateAllRequest extends RestaurantRequest {

    private List<RestaurantImageRequestWithFile> restaurantImageList;

    private List<OpeningHourRequest> openingHourList;

    private List<BreakHourRequest> breakHourList;

    private List<RestaurantAmenityRequest> restaurantAmenityList;

    private List<RestaurantKeywordRequest> restaurantKeywordList;

    private List<MenuItemRequestWithFile> menuItemList;

}
