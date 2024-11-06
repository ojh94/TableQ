package com.itschool.tableq.network.request;

import com.itschool.tableq.domain.Keyword;
import com.itschool.tableq.domain.Restaurant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantKeywordRequest {
    private Long id;
    private Restaurant restaurant;
    private Keyword keyword;
}
