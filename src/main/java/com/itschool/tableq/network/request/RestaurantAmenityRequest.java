package com.itschool.tableq.network.request;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RestaurantAmenityRequest {

    private Long id;

    private RestaurantRequest restaurant;

    private AmenityRequest amenity;
}
