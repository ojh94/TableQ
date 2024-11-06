package com.itschool.tableq.domain;

import com.itschool.tableq.network.request.OpeningHourRequest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalTime;

@Table(name = "opening_hours")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
public class OpeningHour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false,nullable = false)
    private Long id;

    private LocalTime openAt;

    private LocalTime closeAt;

    @Column(nullable = false)
    private String dayOfWeek;

    @ManyToOne
    @JoinColumn(name="restaurant_id",updatable = false)
    private Restaurant restaurant;

    @Builder
    public OpeningHour(LocalTime openAt, LocalTime closeAt, String dayOfWeek) {
        this.openAt = openAt;
        this.closeAt = closeAt;
        this.dayOfWeek = dayOfWeek;
    }

    public void update(OpeningHourRequest openingHourRequest) {
        this.openAt = openingHourRequest.getOpenAt() == null ? this.openAt : openingHourRequest.getOpenAt();
        this.closeAt = openingHourRequest.getCloseAt() == null ? this.closeAt : openingHourRequest.getCloseAt();
        this.dayOfWeek = openingHourRequest.getDayOfWeek() == null ? this.dayOfWeek : openingHourRequest.getDayOfWeek();
        this.restaurant = openingHourRequest.getRestaurant() == null ? this.restaurant : openingHourRequest.getRestaurant();
    }
}
