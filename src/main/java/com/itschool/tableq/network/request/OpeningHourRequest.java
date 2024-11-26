package com.itschool.tableq.network.request;

import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OpeningHourRequest {

    private Long id;

    private LocalTime openAt;

    private LocalTime closeAt;

    private DayOfWeek dayOfWeek;

    private RestaurantRequest restaurant;
}
