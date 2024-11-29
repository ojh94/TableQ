package com.itschool.tableq.network.request;

import com.itschool.tableq.network.request.base.SingleKeyRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RestaurantKeywordRequest extends SingleKeyRequest {

    private RestaurantRequest restaurant;

    private KeywordRequest keyword;
}
