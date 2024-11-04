package com.itschool.tableq.domain;

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
public class RestaurantImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false)
    private LocalDateTime uploadTime;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", updatable = false)
    private Restaurant restaurant;

    @Builder
    public RestaurantImage(String filename, String path, LocalDateTime uploadTime, Restaurant restaurant){
        this.filename = filename;
        this.path = path;
        this.uploadTime = uploadTime;
        this.restaurant = restaurant;
    }

    public void update(RestaurantImageRequest dto){
        this.filename = dto.getFilename();
        this.path = dto.getPath();
        this.uploadTime = dto.getUploadTime();
    }
}
