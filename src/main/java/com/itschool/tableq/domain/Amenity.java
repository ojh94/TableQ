package com.itschool.tableq.domain;

import com.itschool.tableq.domain.base.AuditableEntity;
import com.itschool.tableq.network.request.AmenityRequest;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "amenities")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
public class Amenity extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(nullable = false,unique = true)
    private String name;

    @Builder
    public Amenity(String name) {
        this.name = name;
    }

    public void update(AmenityRequest amenityRequest) {
        this.name = amenityRequest.getName() == null? this.name : amenityRequest.getName();
    }
}
