package com.itschool.tableq.domain;

import com.itschool.tableq.domain.base.AuditableEntity;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.BookmarkRequest;
import com.itschool.tableq.network.request.RestaurantRequest;
import com.itschool.tableq.network.response.BookmarkResponse;
import groovy.lang.DeprecationException;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "restaurants")
public class Restaurant extends AuditableEntity<RestaurantRequest> {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    private String information;

    @Column(nullable = false)
    private String contactNumber;

    @Column(nullable = false)
    private Boolean isAvailable;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", updatable = false)
    private BusinessInformation businessInformation;

    public void update(RestaurantRequest requestEntity) {
        this.name = requestEntity.getName() == null ? this.name : requestEntity.getName();
        this.address = requestEntity.getAddress() == null ? this.address : requestEntity.getAddress();
        this.information = requestEntity.getInformation() == null ? this.information : requestEntity.getInformation();
        this.contactNumber = requestEntity.getContactNumber() == null ? this.contactNumber : requestEntity.getContactNumber();
        this.isAvailable = requestEntity.getIsAvailable() == null ? this.isAvailable : requestEntity.getIsAvailable();
    }
}
