package com.itschool.tableq.domain;

import com.itschool.tableq.domain.base.AuditableEntity;
import com.itschool.tableq.network.request.BusinessInformationRequest;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "business_informations")
public class BusinessInformation extends AuditableEntity {

    @Column(nullable = false, unique = true)
    private String businessNumber;

    @Column(nullable = false)
    private String businessName;

    @Column(unique = true)
    private String contactNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",updatable = false)
    private User user;

    public void update(BusinessInformationRequest businessInformationRequest) {
        this.businessNumber = businessInformationRequest.getBusinessNumber() == null? this.businessNumber : businessInformationRequest.getBusinessNumber();
        this.businessName = businessInformationRequest.getBusinessName() == null? this.businessName : businessInformationRequest.getBusinessName();
        this.contactNumber = businessInformationRequest.getContactNumber() == null? this.contactNumber : businessInformationRequest.getContactNumber();
    }
}
