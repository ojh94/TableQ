package com.itschool.tableq.network.request;

import com.itschool.tableq.domain.Owner;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BuisnessInformationRequest {
    private Long Id;
    private String buisnessNumber;
    private String buisnessName;
    private String contactNumber;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private Owner owner;
}
