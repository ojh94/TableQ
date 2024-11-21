package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.RestaurantImage;
import com.itschool.tableq.domain.ReviewImage;
import com.itschool.tableq.network.response.base.FileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@Getter
public class RestaurantImageResponse extends FileResponse {
    private Long id;

    private LocalDateTime uploadTime;

    private Long restaurantId;

    // 정적 팩토리 메서드 추가
    public static RestaurantImageResponse of(RestaurantImage restaurantImage) {
        return RestaurantImageResponse.builder()
                .id(restaurantImage.getId())
                .fileUrl(restaurantImage.getFileUrl())
                .restaurantId(restaurantImage.getRestaurant().getId())
                .build();
    }
}
