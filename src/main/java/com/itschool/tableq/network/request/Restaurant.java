package com.itschool.tableq.network.request;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.List;

@Entity
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private String location;
    private double rating;
    private int reviews;
    private String description;
    private String phone;
    private String address;
    private String operatingHoursWeekdays;
    private String operatingHoursWeekend;
    private String breakTimeWeekdays;
    private String breakTimeWeekend;
    private String holiday;
    private String facilities;  // 편의시설 리스트 (콤마로 구분된 문자열)
    private String hashtags;    // 해시태그 리스트 (콤마로 구분된 문자열)

    public String getFacilities() {
        return null;
    }

    public String getHashtags() {
        return null;
    }

    // Getters and Setters
}

