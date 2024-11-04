package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.RestaurantImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RestaurantImageResponse {
    private Long id;

    private String filename;

    private String path;

    private LocalDateTime uploadTime;

    private Long restaurantId;

    public RestaurantImageResponse(RestaurantImage restaurantImage){
        this.id = restaurantImage.getId();
        this.filename = restaurantImage.getFilename();
        this.path = restaurantImage.getPath();
        this.uploadTime = restaurantImage.getUploadTime();
        this.restaurantId = restaurantImage.getRestaurant().getId();
    }
}
