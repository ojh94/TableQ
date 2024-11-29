package com.itschool.tableq.network.request;

import com.itschool.tableq.network.request.base.SingleKeyRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ReviewRequest extends SingleKeyRequest {

    private String content;

    private Integer starRating;

    private RestaurantRequest restaurant;

    private UserRequest user;

    private ReservationRequest reservation;
}
