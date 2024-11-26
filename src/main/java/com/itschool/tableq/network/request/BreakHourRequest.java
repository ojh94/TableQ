package com.itschool.tableq.network.request;

import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BreakHourRequest {

    private Long id;

    private LocalTime breakStart;

    private LocalTime breakEnd;

    private DayOfWeek dayOfWeek;

    private RestaurantRequest restaurant;
}
