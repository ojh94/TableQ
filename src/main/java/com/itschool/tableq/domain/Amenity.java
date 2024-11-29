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
public class Amenity extends AuditableEntity<AmenityRequest> {

    @Column(nullable = false,unique = true)
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public void update(AmenityRequest requestEntity) {
        this.name = requestEntity.getName() == null? this.name : requestEntity.getName();
    }
}
