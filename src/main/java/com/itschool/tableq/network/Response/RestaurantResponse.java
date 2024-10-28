package com.itschool.tableq.network.Response;

import com.itschool.tableq.domain.Restaurant;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RestaurantResponse {
    private Long id;
    private Long buisness_id;
    private String name;
    private String address;
    private String introduction;
    private String contact_number;
    private boolean is_available;
    private LocalDateTime created_at;
    private LocalDateTime last_modified_at;

    public RestaurantResponse(Long id, Long buisness_id, String name, String address, String introduction, String contact_number, boolean is_available, LocalDateTime created_at, LocalDateTime last_modified_at) {
        this.id = id;
        this.buisness_id = buisness_id;
        this.name = name;
        this.address = address;
        this.introduction = introduction;
        this.contact_number = contact_number;
        this.is_available = is_available;
        this.created_at = created_at;
        this.last_modified_at = last_modified_at;
    }

    public RestaurantResponse(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.buisness_id = restaurant.getBuisness_id();
        this.name = restaurant.getName();
        this.address = restaurant.getAddress();
        this.introduction = restaurant.getIntroduction();
        this.contact_number = restaurant.getContact_number();
        this.is_available = restaurant.is_available();
        this.created_at = restaurant.getCreated_at();
        this.last_modified_at = restaurant.getLast_modified_at();
    }
}
