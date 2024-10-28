package com.itschool.tableq.network.request;

import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RestaurantRequest {
    private Long id;

    private Long buisness_id;

    private String name;

    private String address;

    private String introduction;

    private String contact_number;

    private boolean is_available;

    private LocalDateTime created_at;

    private LocalDateTime last_modified_at;

}