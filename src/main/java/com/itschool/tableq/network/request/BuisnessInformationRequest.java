package com.itschool.tableq.network.request;

import com.itschool.tableq.domain.Owner;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BuisnessInformationRequest {
    private Long id;

    private String buisnessNumber;

    private String buisnessName;

    private String contactNumber;

    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;

    private Owner owner;
}
