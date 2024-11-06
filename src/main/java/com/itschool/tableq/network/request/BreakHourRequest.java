package com.itschool.tableq.network.request;

import com.itschool.tableq.domain.Restaurant;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class BreakHourRequest {
    private Long id;
    private LocalTime breakStart;
    private LocalTime breakEnd;
    private String dayOfWeek;
    private Restaurant restaurant;
}
