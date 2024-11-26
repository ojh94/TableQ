package com.itschool.tableq.network.request;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RestaurantKeywordRequest {

    private Long id;

    private RestaurantRequest restaurant;

    private KeywordRequest keyword;
}
