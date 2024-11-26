package com.itschool.tableq.network.request;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RestaurantUpdateAllRequest {
    private Long id;

    private String name;

    private String address;

    private String information;

    private String contact_number;

    // private boolean isAvailable;

    private List<RestaurantImageRequest> restaurantImageList;

    private List<OpeningHourRequest> openingHourList;

    private List<BreakHourRequest> breakHourList;

    private List<RestaurantAmenityRequest> restaurantAmenityRequestList;

    private List<RestaurantKeywordRequest> restaurantKeywordRequestList;

    private List<MenuItemRequest> menuItemRequestList;

}
