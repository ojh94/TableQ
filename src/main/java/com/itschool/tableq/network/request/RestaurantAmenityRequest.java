package com.itschool.tableq.network.request;

import com.itschool.tableq.domain.Amenity;
import com.itschool.tableq.domain.Restaurant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantAmenityRequest {
    private Long id;
    private Restaurant restaurant;
    private Amenity amenity;
}
