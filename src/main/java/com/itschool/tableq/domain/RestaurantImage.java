package com.itschool.tableq.domain;

import com.itschool.tableq.domain.base.AuditableEntity;
import com.itschool.tableq.domain.base.IncludeFileUrl;
import com.itschool.tableq.network.request.RestaurantImageRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "restaurant_images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RestaurantImage extends IncludeFileUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", updatable = false)
    private Restaurant restaurant;

    @Builder
    public RestaurantImage(String fileUrl, Restaurant restaurant){
        this.fileUrl = fileUrl;
        this.restaurant = restaurant;
    }

    public void update(String fileUrl){
        this.fileUrl = fileUrl;
    }
}
