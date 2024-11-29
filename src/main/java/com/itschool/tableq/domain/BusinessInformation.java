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
public class BusinessInformation extends AuditableEntity<BusinessInformationRequest> {

    @Column(nullable = false, unique = true)
    private String businessNumber;

    @Column(nullable = false)
    private String businessName;

    @Column(unique = true)
    private String contactNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",updatable = false)
    private User user;

    public void update(BusinessInformationRequest requestEntity) {
        this.businessNumber = requestEntity.getBusinessNumber() == null? this.businessNumber : requestEntity.getBusinessNumber();
        this.businessName = requestEntity.getBusinessName() == null? this.businessName : requestEntity.getBusinessName();
        this.contactNumber = requestEntity.getContactNumber() == null? this.contactNumber : requestEntity.getContactNumber();
    }
}
