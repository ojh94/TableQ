package com.itschool.tableq.domain;

import com.itschool.tableq.network.request.RestaurantImageRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.catalina.Store;

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
    private String title;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false)
    private LocalDateTime uploadTime;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", updatable = false)
    private Restaurant restaurant;

    @Builder
    public RestaurantImage(String title, String path, LocalDateTime uploadTime, Restaurant restaurant){
        this.title = title;
        this.path = path;
        this.uploadTime = uploadTime;
        this.restaurant = restaurant;
    }

    public void update(RestaurantImageRequest dto){
        this.title = dto.getTitle();
        this.path = dto.getPath();
        this.uploadTime = dto.getUploadTime();
    }
}
