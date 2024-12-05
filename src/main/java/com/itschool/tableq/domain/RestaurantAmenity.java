package com.itschool.tableq.domain;

import com.itschool.tableq.domain.base.AuditableEntity;
import com.itschool.tableq.network.request.RestaurantAmenityRequest;
import com.itschool.tableq.network.request.RestaurantRequest;
import groovy.lang.DeprecationException;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "restaurant_amenities",
    uniqueConstraints = @UniqueConstraint(columnNames = {"restaurant_id", "amenity_id"}))
public class RestaurantAmenity extends AuditableEntity<RestaurantRequest> {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id", updatable = false)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "amenity_id",updatable = false)
    private Amenity amenity;

    @Override
    public void update(RestaurantRequest requestEntity) throws DeprecationException {
        throw new DeprecationException("연결 테이블로 update 사용처 없음");
    }
}
