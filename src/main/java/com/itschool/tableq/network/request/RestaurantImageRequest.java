package com.itschool.tableq.network.request;

import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.network.request.base.FileRequest;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RestaurantImageRequest extends FileRequest {
    private Long id;
    private Restaurant restaurant;
}


