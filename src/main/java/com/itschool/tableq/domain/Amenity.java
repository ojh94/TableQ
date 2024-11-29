package com.itschool.tableq.domain;

import com.itschool.tableq.domain.base.AuditableEntity;
import com.itschool.tableq.network.request.AmenityRequest;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "amenities")
public class Amenity extends AuditableEntity {

    @Column(nullable = false,unique = true)
    private String name;

    public void update(AmenityRequest amenityRequest) {
        this.name = amenityRequest.getName() == null? this.name : amenityRequest.getName();
    }

    public void setName(String name) {
        this.name = name;
    }
}
