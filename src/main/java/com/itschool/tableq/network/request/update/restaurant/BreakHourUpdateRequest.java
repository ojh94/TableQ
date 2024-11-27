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
public class BreakHourUpdateRequest {

    private Long id;

    private LocalTime breakStart;

    private LocalTime breakEnd;

    private DayOfWeek dayOfWeek;
}
