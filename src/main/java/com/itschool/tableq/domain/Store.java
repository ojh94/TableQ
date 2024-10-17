package com.itschool.tableq.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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
    private Long owner_key;

    @Column(nullable = false)
    private String store_name;

    @Column(nullable = false)
    private String store_address;

    @Column
    private String store_intro;

    @Column
    private String store_img;

    @Column(nullable = false)
    private String store_number;

    @Column(nullable = false)
    private BigDecimal store_status;
}
