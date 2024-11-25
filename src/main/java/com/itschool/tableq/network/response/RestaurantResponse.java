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
    private BusinessInformation businessInformation;

    public RestaurantResponse(Long id, String name, String address, String information, String contactNumber,
                              boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.information = information;
        this.contactNumber = contactNumber;
        this.isAvailable = isAvailable;
    }

    public RestaurantResponse(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.address = restaurant.getAddress();
        this.information = restaurant.getInformation();
        this.contactNumber = restaurant.getContactNumber();
        this.isAvailable = restaurant.isAvailable();
        this.businessInformation = restaurant.getBusinessInformation();
    }
}
