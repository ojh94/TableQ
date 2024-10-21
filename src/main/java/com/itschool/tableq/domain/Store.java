package com.itschool.tableq.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.time.LocalDateTime;

@Table(name = "Store")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Store {

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

    @Column
    private String introduction;

    @Column(nullable = false)
    private String contact_number;

    @Column(nullable = false)
    private boolean is_available;

    @Column(nullable = false)
    private LocalDateTime created_at;

    @Column(nullable = false)
    private LocalDateTime last_modified_at;

}
