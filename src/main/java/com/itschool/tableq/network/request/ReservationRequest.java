package com.itschool.tableq.network.request;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReservationRequest {

    private Long id;

    private Long reservationNumber;

    private Boolean isEntered;

    private Integer people;

    private RestaurantRequest restaurant;

    private UserRequest user;
}
