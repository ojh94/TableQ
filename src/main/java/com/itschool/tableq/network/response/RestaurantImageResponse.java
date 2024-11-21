package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.RestaurantImage;
import com.itschool.tableq.network.response.base.FileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RestaurantImageResponse extends FileResponse {
    private Long id;

    private LocalDateTime uploadTime;

    private Long restaurantId;

    public RestaurantImageResponse(RestaurantImage restaurantImage){
        this.id = restaurantImage.getId();
        this.fileUrl = restaurantImage.getFileUrl();
        this.restaurantId = restaurantImage.getRestaurant().getId();
    }
}
