package com.itschool.tableq.controller.api;

import com.itschool.tableq.network.request.Restaurant;
import com.itschool.tableq.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public class RestaurantController {
    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping("/restaurant/{id}")
    public String getRestaurantDetails(@PathVariable Long id, Model model) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid restaurant Id:" + id));

        // 시설과 해시태그는 콤마로 구분된 문자열로 처리된다고 가정합니다.
        String[] facilitiesArray = restaurant.getFacilities().split(",");
        String[] hashtagsArray = restaurant.getHashtags().split(",");

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("facilities", facilitiesArray);
        model.addAttribute("hashtags", hashtagsArray);

        return "restaurant-detail";  // Thymeleaf 템플릿 파일 이름
    }
}
