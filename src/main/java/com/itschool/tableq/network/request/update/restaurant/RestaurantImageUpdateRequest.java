package com.itschool.tableq.network.request.update.restaurant;

import com.itschool.tableq.network.request.RestaurantRequest;
import com.itschool.tableq.network.request.base.FileRequest;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RestaurantImageUpdateRequest extends FileRequest {

    private Long id;

}


