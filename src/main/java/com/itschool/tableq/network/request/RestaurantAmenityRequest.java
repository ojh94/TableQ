package com.itschool.tableq.network.request;

import com.itschool.tableq.network.request.base.SingleKeyRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RestaurantAmenityRequest extends SingleKeyRequest {

    private Long id;

    private RestaurantRequest restaurant;

    private AmenityRequest amenity;
}
