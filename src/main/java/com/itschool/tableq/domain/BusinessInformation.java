package com.itschool.tableq.domain;

import com.itschool.tableq.domain.base.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "buisness_informations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Entity
public class BusinessInformation extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long Id;

    @Column(nullable = false, unique = true)
    private String businessNumber;

    @Column(nullable = false)
    private String businessName;

    @Column(unique = true)
    private String contactNumber;

    @ManyToOne
    @JoinColumn(name="owner_id",updatable = false)
    private Owner owner;

    @Builder
    public BusinessInformation(String businessNumber, String businessName, String contactNumber, Owner owner){
        this.businessNumber = businessNumber;
        this.businessName = businessName;
        this.contactNumber = contactNumber;
        this.owner = owner;
    }

    public void update(BusinessInformation businessInformation) {
        this.businessNumber = businessInformation.getBusinessNumber() == null? this.businessNumber : businessInformation.getBusinessNumber();
        this.businessName = businessInformation.getBusinessName() == null? this.businessName : businessInformation.getBusinessName();
        this.contactNumber = businessInformation.getContactNumber() == null? this.contactNumber : businessInformation.getContactNumber();
    }
}
