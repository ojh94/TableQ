package com.itschool.tableq.network.request;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BusinessInformationRequest {

    private Long id;

    private String businessNumber;

    private String businessName;

    private String contactNumber;

    private UserRequest userRequest;
}
