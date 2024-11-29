package com.itschool.tableq.network.request;

import com.itschool.tableq.network.request.base.RequestWithFile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RestaurantImageRequestWithFile extends RequestWithFile {

    private RestaurantRequest restaurant;
}


