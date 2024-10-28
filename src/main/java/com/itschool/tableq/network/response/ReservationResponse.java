package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.Reservation;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.User;

import java.time.LocalDateTime;

public class ReservationResponse {
    private Long id;
    private Integer reservation_number;
    private boolean isEntered;
    private LocalDateTime reserveTime;
    private LocalDateTime enteredTime;
    private Integer people;
    private Restaurant restaurant;
    private User user;

    public ReservationResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.reservation_number = reservation.getReservationNumber();
        this.isEntered = reservation.isEntered();
        this.reserveTime = reservation.getReserveTime();
        this.enteredTime = reservation.getEnteredTime();
        this.people = reservation.getPeople();
        this.restaurant = reservation.getRestaurant();
        this.user = reservation.getUser();
    }
}
