package com.itschool.tableq.network.request;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RestaurantRequest {

    private Long id;

    private String name;

    private String address;

    private String information;

    private String contact_number;

    private boolean isAvailable;

    private BusinessInformationRequest businessInformation;
}
