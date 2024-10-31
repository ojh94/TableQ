package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.Reservation;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReservationResponse {
    private Long id;
    private Integer reservationNumber;
    private boolean isEntered;
    private LocalDateTime reserveTime;
    private LocalDateTime enteredTime;
    private Integer people;
    private Restaurant restaurant;
    private User user;

    public ReservationResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.reservationNumber = reservation.getReservationNumber();
        this.isEntered = reservation.isEntered();
        this.reserveTime = reservation.getReserveTime();
        this.enteredTime = reservation.getEnteredTime();
        this.people = reservation.getPeople();
        this.restaurant = reservation.getRestaurant();
        this.user = reservation.getUser();
    }
}
