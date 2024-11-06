package com.itschool.tableq.domain;

import com.itschool.tableq.domain.base.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalTime;

@Table(name = "break_hours")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class BreakHour extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false,nullable = false)
    private Long id;

    private LocalTime breakStart;

    private LocalTime breakEnd;

    @Column(nullable = false)
    private String dayOfWeek;

    @ManyToOne
    @JoinColumn(name="restaurant_id",updatable = false)
    private Restaurant restaurant;

    @Builder
    public BreakHour(LocalTime breakStart, LocalTime breakEnd, String dayOfWeek, Restaurant restaurant){
        this.breakStart = breakStart;
        this.breakEnd = breakEnd;
        this.dayOfWeek = dayOfWeek;
        this.restaurant = restaurant;
    }
}
