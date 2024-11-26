package com.itschool.tableq.network.request;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OwnerRequest {

    private Long id;

    private String email;

    private String password;

    private String name;

    private String phoneNumber;
}
