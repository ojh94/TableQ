package com.itschool.tableq.network.request.update.restaurant;

import com.itschool.tableq.network.request.KeywordRequest;
import com.itschool.tableq.network.request.RestaurantRequest;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RestaurantKeywordUpdateRequest {

    private Long id;

    private KeywordRequest keyword;
}
