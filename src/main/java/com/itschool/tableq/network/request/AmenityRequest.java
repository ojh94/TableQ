package com.itschool.tableq.network.request;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AmenityRequest {

    private Long id;

    private String name;
}
