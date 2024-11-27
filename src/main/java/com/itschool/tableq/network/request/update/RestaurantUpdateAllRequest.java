package com.itschool.tableq.network.request.update;

import com.itschool.tableq.network.request.RestaurantImageRequest;
import com.itschool.tableq.network.request.update.restaurant.*;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RestaurantUpdateAllRequest {

    private String name;

    private String address;

    private String information;

    private String contact_number;

    // private boolean isAvailable;

    private List<RestaurantImageUpdateRequest> restaurantImageList;

    private List<OpeningHourUpdateRequest> openingHourList;

    private List<BreakHourUpdateRequest> breakHourList;

    private List<RestaurantAmenityUpdateRequest> restaurantAmenityList;

    private List<RestaurantKeywordUpdateRequest> restaurantKeywordList;

    private List<MenuItemUpdateRequest> menuItemList;

}
