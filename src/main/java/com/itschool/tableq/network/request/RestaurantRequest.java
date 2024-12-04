package com.itschool.tableq.network.request;

import com.itschool.tableq.network.request.base.SingleKeyRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class RestaurantRequest extends SingleKeyRequest {

    private String name;

    private String address;

    private String information;

    private String contact_number;

    private boolean isAvailable;

    private BusinessInformationRequest businessInformation;
}
