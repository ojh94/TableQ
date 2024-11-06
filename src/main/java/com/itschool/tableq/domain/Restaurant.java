package com.itschool.tableq.domain;

import com.itschool.tableq.domain.base.AuditableEntity;
import com.itschool.tableq.network.request.RestaurantRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "restaurants")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Entity
public class Restaurant extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    private String information;

    @Column(nullable = false)
    private String contactNumber;

    @Column(nullable = false)
    private boolean isAvailable;

    @ManyToOne
    @JoinColumn(name = "buisness_id", updatable = false)
    private BuisnessInformation buisnessInformation;

    @Builder
    public Restaurant(String name, String address, String information, String contactNumber, boolean isAvailable, BuisnessInformation buisnessInformation) {
        this.name = name;
        this.address = address;
        this.information = information;
        this.contactNumber = contactNumber;
        this.isAvailable = isAvailable;
        this.buisnessInformation = buisnessInformation;
    }

    public void update(RestaurantRequest restaurantRequest) {
        this.name = restaurantRequest.getName() == null ? this.name : restaurantRequest.getName();
        this.address = restaurantRequest.getAddress() == null ? this.address : restaurantRequest.getAddress();
        this.information = restaurantRequest.getIntroduction() == null ? this.information : restaurantRequest.getIntroduction();
        this.contactNumber = restaurantRequest.getContact_number() == null ? this.contactNumber : restaurantRequest.getContact_number();
    }
}
