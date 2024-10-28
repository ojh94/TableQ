package com.itschool.tableq.domain;

import com.itschool.tableq.network.request.RestaurantRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "restaurants")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Entity
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    private String introduction;

    @Column(nullable = false)
    private String contactNumber;

    @Column(nullable = false)
    private boolean isAvailable;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime lastModifiedAt;

    @ManyToOne
    @JoinColumn(name = "buisness_id", updatable = false)
    private BuisnessInformation buisnessInformation;

    @Builder
    public Restaurant(String name, String address, String introduction, String contactNumber, boolean isAvailable,
                      LocalDateTime createdAt, LocalDateTime lastModifiedAt, BuisnessInformation buisnessInformation) {
        this.name = name;
        this.address = address;
        this.introduction = introduction;
        this.contactNumber = contactNumber;
        this.isAvailable = isAvailable;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.buisnessInformation = buisnessInformation;
    }

    public void update(RestaurantRequest restaurantRequest) {
        this.name = restaurantRequest.getName() == null ? this.name : restaurantRequest.getName();
        this.address = restaurantRequest.getAddress() == null ? this.address : restaurantRequest.getAddress();
        this.introduction = restaurantRequest.getIntroduction() == null ? this.introduction : restaurantRequest.getIntroduction();
        this.contactNumber = restaurantRequest.getContact_number() == null ? this.contactNumber : restaurantRequest.getContact_number();
    }
}
