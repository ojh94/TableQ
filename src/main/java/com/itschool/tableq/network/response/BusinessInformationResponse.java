package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.BusinessInformation;
import com.itschool.tableq.domain.Owner;
import com.itschool.tableq.domain.User;
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

    private UserResponse userResponse;

    public static BusinessInformationResponse of (BusinessInformation businessInformation) {
        User user = businessInformation.getUser();

        return BusinessInformationResponse.builder()
                .id(businessInformation.getId())
                .businessNumber(businessInformation.getBusinessNumber())
                .businessName(businessInformation.getBusinessName())
                .contactNumber(businessInformation.getContactNumber())
                .userResponse(UserResponse.builder()
                        .id(user.getId())
                        .build())
                .build();
    }
}
