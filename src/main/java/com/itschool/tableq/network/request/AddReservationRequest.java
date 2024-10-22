package com.itschool.tableq.network.request;

import com.itschool.tableq.domain.Store;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AddReservationRequest {
    private Long id;
    private String customerContactNumber;
    private Integer reservation_number;
    private boolean entered;
    private LocalDateTime reserveTime;
    private LocalDateTime enteredTime;
    private Integer people;
    private Store store;
}
