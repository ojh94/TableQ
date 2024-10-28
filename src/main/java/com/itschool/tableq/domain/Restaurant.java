package com.itschool.tableq.domain;

import com.itschool.tableq.network.request.RestaurantRequest;
import com.itschool.tableq.repository.RestaurantRepository;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

@Table(name = "Restaurant")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Entity
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(nullable = false)
    private Long buisness_id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    private String introduction;

    @Column(nullable = false)
    private String contact_number;

    @Column(nullable = false)
    private boolean is_available;

    @Column(nullable = false)
    private LocalDateTime created_at;

    @Column(nullable = false)
    private LocalDateTime last_modified_at;

    @Builder
    public Restaurant(Long buisness_id, String name, String address, String introduction, String contact_number, boolean is_available, LocalDateTime created_at, LocalDateTime last_modified_at) {
        this.buisness_id = buisness_id;
        this.name = name;
        this.address = address;
        this.introduction = introduction;
        this.contact_number = contact_number;
        this.is_available = is_available;
        this.created_at = created_at;
        this.last_modified_at = last_modified_at;
    }

    public void update(RestaurantRequest restaurantRequest) {
        this.name = restaurantRequest.getName() == null ? this.name : restaurantRequest.getName();
        this.address = restaurantRequest.getAddress() == null ? this.address : restaurantRequest.getAddress();
        this.introduction = restaurantRequest.getIntroduction() == null ? this.introduction : restaurantRequest.getIntroduction();
        this.contact_number = restaurantRequest.getContact_number() == null ? this.contact_number : restaurantRequest.getContact_number();
    }
}
