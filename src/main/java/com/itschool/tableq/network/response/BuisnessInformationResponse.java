package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.BuisnessInformation;
import com.itschool.tableq.domain.Owner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BuisnessInformationResponse {
    private Long id;

    private String buisnessNumber;

    private String buisnessName;

    private String contactNumber;

    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;

    private Long ownerId;

    public BuisnessInformationResponse(BuisnessInformation buisnessInformation) {
        this.id = buisnessInformation.getId();
        this.buisnessNumber = buisnessInformation.getBuisnessNumber();
        this.buisnessName = buisnessInformation.getBuisnessName();
        this.contactNumber = buisnessInformation.getContactNumber();
        this.createdAt = buisnessInformation.getCreatedAt();
        this.lastModifiedAt = buisnessInformation.getLastModifiedAt();
        this.ownerId = buisnessInformation.getOwner().getId();
    }
}
