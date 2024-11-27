package com.itschool.tableq.network.request.update.restaurant;

import com.itschool.tableq.network.request.AmenityRequest;
import com.itschool.tableq.network.request.RestaurantRequest;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RestaurantAmenityUpdateRequest {

    private Long id;

    private AmenityRequest amenity;
}
