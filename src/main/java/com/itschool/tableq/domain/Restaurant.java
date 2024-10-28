package com.itschool.tableq.domain;

import com.itschool.tableq.network.request.RestaurantRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "Restaurant")
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
    private Long buisnessId;

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

    @Builder
    public Restaurant(Long buisnessId, String name, String address, String introduction, String contactNumber, boolean isAvailable, LocalDateTime createdAt, LocalDateTime lastModifiedAt) {
        this.buisnessId = buisnessId;
        this.name = name;
        this.address = address;
        this.introduction = introduction;
        this.contactNumber = contactNumber;
        this.isAvailable = isAvailable;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
    }

    public void update(RestaurantRequest restaurantRequest) {
        this.name = restaurantRequest.getName() == null ? this.name : restaurantRequest.getName();
        this.address = restaurantRequest.getAddress() == null ? this.address : restaurantRequest.getAddress();
        this.introduction = restaurantRequest.getIntroduction() == null ? this.introduction : restaurantRequest.getIntroduction();
        this.contactNumber = restaurantRequest.getContact_number() == null ? this.contactNumber : restaurantRequest.getContact_number();
    }
}
