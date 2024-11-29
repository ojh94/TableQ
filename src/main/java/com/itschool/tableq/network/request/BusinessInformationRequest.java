package com.itschool.tableq.network.request;

import com.itschool.tableq.network.request.base.SingleKeyRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BusinessInformationRequest extends SingleKeyRequest {

    private String businessNumber;

    private String businessName;

    private String contactNumber;

    private UserRequest userRequest;
}
