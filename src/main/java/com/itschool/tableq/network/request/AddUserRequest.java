package com.itschool.tableq.network.request;

import lombok.Getter;
import lombok.Setter;

import java.security.Timestamp;

@Getter
@Setter
public class AddUserRequest {
    private Long id;
    private String password;
    private String nickname;
    private String phone_number;
    private Timestamp created_at;
    private Timestamp last_login_at;
    private String address;
    private String name;
    private String social_type;
    private String social_id;
    private String email;
}
