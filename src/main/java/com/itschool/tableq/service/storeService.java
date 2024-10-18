package com.itschool.tableq.service;

import com.itschool.tableq.domain.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.itschool.tableq.repository.StoreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class storeService {
    private final StoreRepository storeRepository;

    public Store findById(String id) {
        return storeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Store not found"));
    }
    public List<Store> findAll() {
        return storeRepository.findAll();
    }
    public Store create(Store store) {
        Store newStore = storeRepository.save(store);
        return newStore;
    }
    public void delete(String  id) {
        Store store = findById(id);
        storeRepository.delete(store);
    }

    public Store update(String id, String body) { // 어떤것을 업데이트 가능하게 할지?
        return null;
    }
}
