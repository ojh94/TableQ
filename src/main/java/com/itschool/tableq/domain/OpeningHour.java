package com.itschool.tableq.domain;

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
    public OpeningHour(String dayOfWeek, Restaurant restaurant){
        this.dayOfWeek = dayOfWeek;
        this.restaurant = restaurant;
    }
}
