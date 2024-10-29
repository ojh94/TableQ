package com.itschool.tableq.domain;

import com.itschool.tableq.network.request.ReservationRequest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Table(name = "reservations")
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    /*@Column(nullable = false)
    private String contactNumber;*/

    @Column(nullable = false, updatable = false)
    private Integer reservationNumber;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean isEntered;

    @Column(nullable = false, updatable = false)
    private LocalDateTime reserveTime;

    private LocalDateTime enteredTime;

    private Integer people;

    @ManyToOne
    @JoinColumn(name = "store_id",updatable = false)
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    @Builder
    public Reservation(Integer reservationNumber,
                       LocalDateTime reserveTime, Integer people, Restaurant restaurant, User user){
        this.reservationNumber = reservationNumber;
        this.reserveTime = reserveTime;
        this.people = people;
        this.restaurant = restaurant;
        this.user = user;
    }

    public void update(ReservationRequest dto){
        this.reservationNumber = dto.getReservationNumber() == null? this.reservationNumber: dto.getReservationNumber();
        this.isEntered = dto.isEntered();
        this.reserveTime = dto.getReserveTime();
        this.enteredTime = dto.getEnteredTime();
        this.people = dto.getPeople();
        this.restaurant = dto.getRestaurant();
        this.user = dto.getUser() == null? this.user: dto.getUser();
    }
}
