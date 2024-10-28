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

    @Column(nullable = false)
    private LocalTime openAt;

    @Column(nullable = false)
    private LocalTime closeAt;

    private String dayOff;

    private LocalTime breakStart;

    private LocalTime breakEnd;

    @ManyToOne
    @JoinColumn(name="restaurant_id",updatable = false)
    private Restaurant restaurant;

    @Builder
    public OpeningHour(LocalTime openAt, LocalTime closeAt, Restaurant restaurant){
        this.openAt = openAt;
        this.closeAt = closeAt;
        this.restaurant = restaurant;
    }

    @Builder
    public OpeningHour(LocalTime openAt, LocalTime closeAt, String dayOff, Restaurant restaurant){
        this.openAt = openAt;
        this.closeAt = closeAt;
        this.dayOff = dayOff;
        this.restaurant = restaurant;
    }

    @Builder
    public OpeningHour(LocalTime openAt, LocalTime closeAt, LocalTime breakStart, LocalTime breakEnd, Restaurant restaurant){
        this.openAt = openAt;
        this.closeAt = closeAt;
        this.breakStart = breakStart;
        this.breakEnd = breakEnd;
        this.restaurant = restaurant;
    }
}
