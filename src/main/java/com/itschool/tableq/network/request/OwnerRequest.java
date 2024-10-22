package com.itschool.tableq.network.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OwnerRequest {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;

}
