package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.Restaurant;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RestaurantResponse {
    private Long id;
    private Long buisnessId;
    private String name;
    private String address;
    private String introduction;
    private String contactNumber;
    private boolean isAvailable;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    public RestaurantResponse(Long id, Long buisnessId, String name, String address, String introduction, String contactNumber, boolean isAvailable, LocalDateTime createdAt, LocalDateTime lastModifiedAt) {
        this.id = id;
        this.buisnessId = buisnessId;
        this.name = name;
        this.address = address;
        this.introduction = introduction;
        this.contactNumber = contactNumber;
        this.isAvailable = isAvailable;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
    }

    public RestaurantResponse(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.buisnessId = restaurant.getBuisnessId();
        this.name = restaurant.getName();
        this.address = restaurant.getAddress();
        this.introduction = restaurant.getIntroduction();
        this.contactNumber = restaurant.getContactNumber();
        this.isAvailable = restaurant.isAvailable();
        this.createdAt = restaurant.getCreatedAt();
        this.lastModifiedAt = restaurant.getLastModifiedAt();
    }
}
