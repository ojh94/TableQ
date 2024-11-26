package com.itschool.tableq.domain;

import com.itschool.tableq.domain.base.AuditableEntity;
import com.itschool.tableq.network.request.RestaurantRequest;
import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
@Table(name = "restaurants")
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
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buisness_id", updatable = false)
    private BusinessInformation businessInformation;

    public void update(RestaurantRequest restaurantRequest) {
        this.name = restaurantRequest.getName() == null ? this.name : restaurantRequest.getName();
        this.address = restaurantRequest.getAddress() == null ? this.address : restaurantRequest.getAddress();
        this.information = restaurantRequest.getInformation() == null ? this.information : restaurantRequest.getInformation();
        this.contactNumber = restaurantRequest.getContact_number() == null ? this.contactNumber : restaurantRequest.getContact_number();
    }
}
