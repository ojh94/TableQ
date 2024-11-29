package com.itschool.tableq.domain;

import com.itschool.tableq.domain.base.AuditableEntity;
import com.itschool.tableq.network.request.OpeningHourRequest;
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
@Table(name = "opening_hours")
public class OpeningHour extends AuditableEntity {

    private LocalTime openAt;

    private LocalTime closeAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="restaurant_id",updatable = false)
    private Restaurant restaurant;

    public void update(OpeningHourRequest openingHourRequest) {
        this.openAt = openingHourRequest.getOpenAt() == null ? this.openAt : openingHourRequest.getOpenAt();
        this.closeAt = openingHourRequest.getCloseAt() == null ? this.closeAt : openingHourRequest.getCloseAt();
        this.dayOfWeek = openingHourRequest.getDayOfWeek() == null ? this.dayOfWeek : openingHourRequest.getDayOfWeek();
    }
}
