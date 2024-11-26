package com.itschool.tableq.network.request;

import com.itschool.tableq.network.request.base.FileRequest;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MenuItemRequest extends FileRequest {

    private Long id;

    private String name;

    private String price;

    private String description;

    private Boolean recommendation;

    private RestaurantRequest restaurant;
}
