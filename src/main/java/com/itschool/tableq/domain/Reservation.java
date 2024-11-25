package com.itschool.tableq.domain;

import com.itschool.tableq.domain.base.AuditableEntity;
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
public class Reservation extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    /*@Column(nullable = false)
    private String contactNumber;*/

    @Column(nullable = false, updatable = false)
    private Integer reservationNumber;

    private Boolean isEntered;

    private Integer people;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id",updatable = false)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    @Builder
    public Reservation(Integer reservationNumber, Integer people, Restaurant restaurant, User user){
        this.reservationNumber = reservationNumber;
        this.people = people;
        this.restaurant = restaurant;
        this.user = user;
    }

    public void update(ReservationRequest dto){
        this.isEntered = dto.getIsEntered();
    }
}
