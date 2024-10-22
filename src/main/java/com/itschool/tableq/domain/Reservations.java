package com.itschool.tableq.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "reservations")
@NoArgsConstructor
@Getter
@Entity
public class Reservations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "customer_contact_number",nullable = false)
    private String customerContactNumber;

    @Column(name = "reservation_number",nullable = false)
    private Integer reservationNumber;

    @Column(name = "entered", nullable = false)
    private boolean entered;

    @Column(name = "reserve_time",nullable = false)
    private LocalDateTime reserveTime;

    @Column(name = "entered_time")
    private LocalDateTime enteredTime;

    @Column(name = "people")
    private Integer people;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @Builder
    public Reservations(String customerContactNumber, Integer reservationNumber,
                        LocalDateTime reserveTime, Integer people, Store store){
        this.customerContactNumber = customerContactNumber;
        this.reservationNumber = reservationNumber;
        this.reserveTime = reserveTime;
        this.people = people;
        this.store = store;
    }
}
