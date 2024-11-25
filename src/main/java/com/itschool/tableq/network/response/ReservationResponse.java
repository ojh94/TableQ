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
    private RestaurantResponse restaurant;
    private UserResponse user;

    public static ReservationResponse of(Reservation reservation) {
        Restaurant restaurant = reservation.getRestaurant();
        User user = reservation.getUser();

        return ReservationResponse.builder()
                .id(reservation.getId())
                .reservationNumber(reservation.getReservationNumber())
                .isEntered(reservation.getIsEntered())
                .people(reservation.getPeople())
                .createdAt(reservation.getCreatedAt())
                .lastModifiedAt(reservation.getLastModifiedAt())
                .user(UserResponse.builder()
                        .id(user.getId())
                        .build())
                .restaurant(RestaurantResponse.builder()
                        .id(restaurant.getId())
                        .name(restaurant.getName())
                        .address(restaurant.getAddress())
                        .information(restaurant.getInformation())
                        .contactNumber(restaurant.getInformation())
                        .isAvailable(restaurant.isAvailable())
                        .build())
                .build();
    }
}
