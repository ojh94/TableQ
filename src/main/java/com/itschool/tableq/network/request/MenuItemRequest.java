package com.itschool.tableq.network.request;

import com.itschool.tableq.network.request.base.FileRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
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
