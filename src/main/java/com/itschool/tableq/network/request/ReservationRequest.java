package com.itschool.tableq.network.request;

import com.itschool.tableq.network.request.base.SingleKeyRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ReservationRequest extends SingleKeyRequest {

    private Long id;

    private Long reservationNumber;

    private Boolean isEntered;

    private Integer people;

    private RestaurantRequest restaurant;

    private UserRequest user;
}
