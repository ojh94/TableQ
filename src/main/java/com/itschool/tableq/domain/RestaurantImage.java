package com.itschool.tableq.domain;

import com.itschool.tableq.domain.base.IncludeFileUrl;
import com.itschool.tableq.network.request.RestaurantImageRequestWithFile;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "restaurant_images")
public class RestaurantImage extends IncludeFileUrl<RestaurantImageRequestWithFile> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", updatable = false)
    private Restaurant restaurant;

    @Override
    @Deprecated
    public void updateWithoutFileUrl(RestaurantImageRequestWithFile requestEntity) {}
}
