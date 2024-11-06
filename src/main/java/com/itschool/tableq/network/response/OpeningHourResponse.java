package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.OpeningHour;
import com.itschool.tableq.domain.Restaurant;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@NoArgsConstructor
@Getter
public class OpeningHourResponse {
    private Long id;
    private LocalTime openAt;
    private LocalTime closeAt;
    private String dayOfWeek;
    private Restaurant restaurant;

    public OpeningHourResponse(OpeningHour openingHour) {
        this.id = openingHour.getId();
        this.openAt = openingHour.getOpenAt();
        this.closeAt = openingHour.getCloseAt();
        this.dayOfWeek = openingHour.getDayOfWeek();
        this.restaurant = openingHour.getRestaurant();
    }
}
