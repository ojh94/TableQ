package com.itschool.tableq.network.request;

import com.itschool.tableq.domain.Restaurant;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class OpeningHourRequest {
    private Long id;
    private LocalTime openAt;
    private LocalTime closeAt;
    private String dayOfWeek;
    private Restaurant restaurant;
}
