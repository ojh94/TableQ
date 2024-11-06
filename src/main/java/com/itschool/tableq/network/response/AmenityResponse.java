package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.Amenity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AmenityResponse {
    private Long id;
    private String name;

    public AmenityResponse(Amenity amenity) {
        this.id = amenity.getId();
        this.name = amenity.getName();
    }
}
