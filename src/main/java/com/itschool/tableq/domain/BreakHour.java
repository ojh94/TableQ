package com.itschool.tableq.domain;

import com.itschool.tableq.domain.base.AuditableEntity;
import com.itschool.tableq.network.request.BreakHourRequest;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "break_hours")
public class BreakHour extends AuditableEntity {

    private LocalTime breakStart;

    private LocalTime breakEnd;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="restaurant_id",updatable = false)
    private Restaurant restaurant;

    public void update(BreakHourRequest breakHourRequest) {
        this.breakStart = breakHourRequest.getBreakStart() == null ? this.breakStart : breakHourRequest.getBreakStart();
        this.breakEnd = breakHourRequest.getBreakEnd() == null ? this.breakEnd : breakHourRequest.getBreakEnd();
        this.dayOfWeek = breakHourRequest.getDayOfWeek() == null ? this.dayOfWeek : breakHourRequest.getDayOfWeek();
    }
}
