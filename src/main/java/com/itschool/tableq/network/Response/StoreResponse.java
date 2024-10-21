package com.itschool.tableq.network.Response;

import com.itschool.tableq.domain.Store;
import jakarta.persistence.Column;
import lombok.Getter;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.time.LocalDateTime;

@Getter
public class StoreResponse {
    private Long id;
    private Long buisness_id;
    private String name;
    private String address;
    private String introduction;
    private String contact_number;
    private boolean is_available;
    private LocalDateTime created_at;
    private LocalDateTime last_modified_at;

    public StoreResponse(Long id, Long buisness_id, String name, String address, String introduction, String contact_number, boolean is_available, LocalDateTime created_at, LocalDateTime last_modified_at) {
        this.id = id;
        this.buisness_id = buisness_id;
        this.name = name;
        this.address = address;
        this.introduction = introduction;
        this.contact_number = contact_number;
        this.is_available = is_available;
        this.created_at = created_at;
        this.last_modified_at = last_modified_at;
    }

    public StoreResponse(Store store) {
        this.id = store.getId();
        this.buisness_id = store.getBuisness_id();
        this.name = store.getName();
        this.address = store.getAddress();
        this.introduction = store.getIntroduction();
        this.contact_number = store.getContact_number();
        this.is_available = store.is_available();
        this.created_at = store.getCreated_at();
        this.last_modified_at = store.getLast_modified_at();
    }
}
