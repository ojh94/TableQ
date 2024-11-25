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
    private Boolean isEntered;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private Integer people;
    private Restaurant restaurant;
    private Long userId;

    public ReservationResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.reservationNumber = reservation.getReservationNumber();
        this.isEntered = reservation.getIsEntered();
        this.people = reservation.getPeople();
        this.restaurant = reservation.getRestaurant();
        this.userId = reservation.getUser().getId();
        this.createdAt = reservation.getCreatedAt();
        this.lastModifiedAt = reservation.getLastModifiedAt();
    }
}
