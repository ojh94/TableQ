package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.Amenity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AmenityResponse {
    private Long id;
    private String name;

    public static AmenityResponse of(Amenity amenity) {
        return AmenityResponse.builder()
                .id(amenity.getId())
                .name(amenity.getName())
                .build();
    }
}
