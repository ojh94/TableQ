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
public class BreakHour extends AuditableEntity<BreakHourRequest> {

    private LocalTime breakStart;

    private LocalTime breakEnd;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="restaurant_id",updatable = false)
    private Restaurant restaurant;

    public void update(BreakHourRequest requestEntity) {
        this.breakStart = requestEntity.getBreakStart() == null ? this.breakStart : requestEntity.getBreakStart();
        this.breakEnd = requestEntity.getBreakEnd() == null ? this.breakEnd : requestEntity.getBreakEnd();
        this.dayOfWeek = requestEntity.getDayOfWeek() == null ? this.dayOfWeek : requestEntity.getDayOfWeek();
    }
}
