package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.Amenity;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.RestaurantAmenity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RestaurantAmenityResponse {
    private Long id;
    private RestaurantResponse restaurant;
    private AmenityResponse amenity;

    public static RestaurantAmenityResponse of(RestaurantAmenity restaurantAmenity) {
        Restaurant restaurant = restaurantAmenity.getRestaurant();
        Amenity amenity = restaurantAmenity.getAmenity();

        return RestaurantAmenityResponse.builder()
                .id(restaurantAmenity.getId())
                .restaurant(RestaurantResponse.of(restaurant))
                .amenity(AmenityResponse.of(amenity))
                .build();
    }
}
