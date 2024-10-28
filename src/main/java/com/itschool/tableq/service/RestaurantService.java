package com.itschool.tableq.service;

import com.itschool.tableq.domain.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.itschool.tableq.repository.StoreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RestaurantService {
    private final StoreRepository storeRepository;

    public Restaurant findById(Long id) {
        return storeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Store not found"));
    }
    public List<Restaurant> findAll() {
        return storeRepository.findAll();
    }
    public Restaurant create(Restaurant restaurant) {
        Restaurant newRestaurant = storeRepository.save(restaurant);
        return newRestaurant;
    }
    public void delete(Long id) {
        Restaurant restaurant = findById(id);
        storeRepository.delete(restaurant);
    }

    public Restaurant update(Long id, String body) { // 어떤것을 업데이트 가능하게 할지?
        return null;
    }
}
