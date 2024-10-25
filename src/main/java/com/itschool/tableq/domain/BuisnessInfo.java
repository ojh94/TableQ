package com.itschool.tableq.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class BuisnessInfo {
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
    public BuisnessInfo(String buisnessNumber, String buisnessName, String contactNumber, Owner owner){
        this.buisnessNumber = buisnessNumber;
        this.buisnessName = buisnessName;
        this.contactNumber = contactNumber;
        this.owner = owner;
    }
}
