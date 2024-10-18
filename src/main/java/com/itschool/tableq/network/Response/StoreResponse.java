package com.itschool.tableq.network.Response;

import com.itschool.tableq.domain.Store;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class StoreResponse {
    private Long id;
    private Long owner_key;
    private String store_name;
    private String store_address;
    private String store_intro;
    private String store_number;
    private BigDecimal store_status;

    public StoreResponse(Long id, Long owner_key, String store_name, String store_address, String store_intro, String store_number, BigDecimal store_status) {
        this.id = id;
        this.owner_key = owner_key;
        this.store_name = store_name;
        this.store_address = store_address;
        this.store_intro = store_intro;
        this.store_number = store_number;
        this.store_status = store_status;
    }

    public StoreResponse(Store store) {
        this.id = store.getId();
        this.owner_key = store.getOwner_key();
        this.store_name = store.getStore_name();
        this.store_address = store.getStore_address();
        this.store_intro = store.getStore_intro();
        this.store_number = store.getStore_number();
        this.store_status = store.getStore_status();
    }
}
