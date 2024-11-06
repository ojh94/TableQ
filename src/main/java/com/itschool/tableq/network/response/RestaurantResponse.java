package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.BusinessInformation;
import com.itschool.tableq.domain.Restaurant;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RestaurantResponse {
    private Long id;
    private String name;
    private String address;
    private String introduction;
    private String contactNumber;
    private boolean isAvailable;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private BusinessInformation businessInformation;

    public RestaurantResponse(Long id, String name, String address, String introduction, String contactNumber, boolean isAvailable,
                              LocalDateTime createdAt, LocalDateTime lastModifiedAt, BusinessInformation businessInformation) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.introduction = introduction;
        this.contactNumber = contactNumber;
        this.isAvailable = isAvailable;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.businessInformation = businessInformation;
    }

    public RestaurantResponse(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.address = restaurant.getAddress();
        this.introduction = restaurant.getInformation();
        this.contactNumber = restaurant.getContactNumber();
        this.isAvailable = restaurant.isAvailable();
        this.createdAt = restaurant.getCreatedAt();
        this.lastModifiedAt = restaurant.getLastModifiedAt();
        this.businessInformation = restaurant.getBusinessInformation();
    }
}
