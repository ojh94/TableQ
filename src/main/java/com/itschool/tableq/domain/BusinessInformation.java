package com.itschool.tableq.domain;

import com.itschool.tableq.domain.base.AuditableEntity;
import com.itschool.tableq.network.request.BusinessInformationRequest;
import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
@Table(name = "business_informations")
public class BusinessInformation extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String businessNumber;

    @Column(nullable = false)
    private String businessName;

    @Column(unique = true)
    private String contactNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="owner_id",updatable = false)
    private Owner owner;

    public void update(BusinessInformationRequest businessInformationRequest) {
        this.businessNumber = businessInformationRequest.getBusinessNumber() == null? this.businessNumber : businessInformationRequest.getBusinessNumber();
        this.businessName = businessInformationRequest.getBusinessName() == null? this.businessName : businessInformationRequest.getBusinessName();
        this.contactNumber = businessInformationRequest.getContactNumber() == null? this.contactNumber : businessInformationRequest.getContactNumber();
    }
}
