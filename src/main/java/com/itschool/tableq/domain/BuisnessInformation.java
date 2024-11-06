package com.itschool.tableq.domain;

import com.itschool.tableq.domain.base.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "buisness_informations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Entity
public class BuisnessInformation extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long Id;

    @Column(nullable = false, unique = true)
    private String buisnessNumber;

    @Column(nullable = false)
    private String buisnessName;

    @Column(unique = true)
    private String contactNumber;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime lastModifiedAt;

    @ManyToOne
    @JoinColumn(name="owner_id",updatable = false)
    private Owner owner;

    @Builder
    public BuisnessInformation(String buisnessNumber, String buisnessName, String contactNumber, Owner owner){
        this.buisnessNumber = buisnessNumber;
        this.buisnessName = buisnessName;
        this.contactNumber = contactNumber;
        this.owner = owner;
    }

    public void update(BuisnessInformation buisnessInformation) {
        this.buisnessNumber = buisnessInformation.getBuisnessNumber() == null? this.buisnessNumber : buisnessInformation.getBuisnessNumber();
        this.buisnessName = buisnessInformation.getBuisnessName() == null? this.buisnessName : buisnessInformation.getBuisnessName();
        this.contactNumber = buisnessInformation.getContactNumber() == null? this.contactNumber : buisnessInformation.getContactNumber();
    }
}
