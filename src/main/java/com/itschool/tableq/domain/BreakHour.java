package com.itschool.tableq.domain;

import com.itschool.tableq.domain.base.AuditableEntity;
import com.itschool.tableq.network.request.BreakHourRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
@Table(name = "break_hours")
public class BreakHour extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false,nullable = false)
    private Long id;

    private LocalTime breakStart;

    private LocalTime breakEnd;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="restaurant_id",updatable = false)
    private Restaurant restaurant;

    @Builder
    public BreakHour(LocalTime breakStart, LocalTime breakEnd, DayOfWeek dayOfWeek){
        this.breakStart = breakStart;
        this.breakEnd = breakEnd;
        this.dayOfWeek = dayOfWeek;
    }

    public void update(BreakHourRequest breakHourRequest) {
        this.breakStart = breakHourRequest.getBreakStart() == null ? this.breakStart : breakHourRequest.getBreakStart();
        this.breakEnd = breakHourRequest.getBreakEnd() == null ? this.breakEnd : breakHourRequest.getBreakEnd();
        this.dayOfWeek = breakHourRequest.getDayOfWeek() == null ? this.dayOfWeek : breakHourRequest.getDayOfWeek();
    }
}
