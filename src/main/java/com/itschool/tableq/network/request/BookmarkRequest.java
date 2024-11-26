package com.itschool.tableq.network.request;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookmarkRequest {

    private Long id;

    private RestaurantRequest restaurant;

    private UserRequest user;
}
