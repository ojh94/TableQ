package com.itschool.tableq.network.request.update.restaurant;

import com.itschool.tableq.network.request.RestaurantRequest;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OpeningHourUpdateRequest {

    private Long id;

    private LocalTime openAt;

    private LocalTime closeAt;

    private DayOfWeek dayOfWeek;

}