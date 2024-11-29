package com.itschool.tableq.domain;

import com.itschool.tableq.domain.base.AuditableEntity;
import com.itschool.tableq.network.request.ReservationRequest;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "reservations")
public class Reservation extends AuditableEntity<ReservationRequest> {

    /*@Column(nullable = false)
    private String contactNumber;*/

    @Column(nullable = false, updatable = false)
    private Long reservationNumber;

    private Boolean isEntered;

    private Integer people;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id",updatable = false)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    public void update(ReservationRequest requestEntity){
        this.isEntered = requestEntity.getIsEntered();
    }
}
