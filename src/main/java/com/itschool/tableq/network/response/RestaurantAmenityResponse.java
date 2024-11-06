package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.Amenity;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.RestaurantAmenity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RestaurantAmenityResponse {
    private Long id;
    private Restaurant restaurant;
    private Amenity amenity;

    public RestaurantAmenityResponse(RestaurantAmenity restaurantAmenity) {
        this.id = restaurantAmenity.getId();
        this.restaurant = restaurantAmenity.getRestaurant();
        this.amenity = restaurantAmenity.getAmenity();
    }
}
