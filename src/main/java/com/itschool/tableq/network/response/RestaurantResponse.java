package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.BusinessInformation;
import com.itschool.tableq.domain.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RestaurantResponse {
    private Long id;
    private String name;
    private String address;
    private String information;
    private String contactNumber;
    private boolean isAvailable;
    private BusinessInformationResponse businessInformation;

    public static RestaurantResponse of(Restaurant restaurant) {
        BusinessInformation businessInformation = restaurant.getBusinessInformation();

        return RestaurantResponse.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .information(restaurant.getInformation())
                .contactNumber(restaurant.getContactNumber())
                .isAvailable(restaurant.isAvailable())
                .businessInformation(BusinessInformationResponse.builder()
                        .id(businessInformation.getId())
                        .build())
                .build();
    }
}