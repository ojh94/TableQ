package com.itschool.tableq.network.request;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class StoreRequest {
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
