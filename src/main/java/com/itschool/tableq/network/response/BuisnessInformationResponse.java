package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.BuisnessInformation;
import com.itschool.tableq.domain.Owner;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class BuisnessInformationResponse {
    private Long Id;
    private Long ownerId;
    private String buisnessNumber;
    private String buisnessName;
    private String contactNumber;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private Owner owner;

    public BuisnessInformationResponse(BuisnessInformation buisnessInformation) {
        this.Id = buisnessInformation.getId();
        this.buisnessNumber = buisnessInformation.getBuisnessNumber();
        this.buisnessName = buisnessInformation.getBuisnessName();
        this.contactNumber = buisnessInformation.getContactNumber();
        this.createdAt = buisnessInformation.getCreatedAt();
        this.lastModifiedAt = buisnessInformation.getLastModifiedAt();
        this.owner = buisnessInformation.getOwner();
    }

}
