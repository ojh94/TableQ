package com.itschool.tableq.network.request;

import lombok.Getter;
import lombok.Setter;

import java.security.Timestamp;

@Getter
@Setter
public class AddOwnerRequest {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String contact_number;
    private Timestamp created_at;
    private Timestamp last_modified_at;

}
