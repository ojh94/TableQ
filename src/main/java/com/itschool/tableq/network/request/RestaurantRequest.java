package com.itschool.tableq.network.request;

import lombok.*;

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

    private boolean isAvailable;

    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;

}
