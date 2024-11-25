package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.BusinessInformation;
import com.itschool.tableq.domain.Owner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BusinessInformationResponse {
    private Long id;

    private String businessNumber;

    private String businessName;

    private String contactNumber;

    private OwnerResponse owner;

    public static BusinessInformationResponse of (BusinessInformation businessInformation) {
        Owner owner = businessInformation.getOwner();

        return BusinessInformationResponse.builder()
                .id(businessInformation.getId())
                .businessNumber(businessInformation.getBusinessNumber())
                .businessName(businessInformation.getBusinessName())
                .contactNumber(businessInformation.getContactNumber())
                .owner(OwnerResponse.builder()
                        .id(owner.getId())
                        .build())
                .build();
    }
}
