package com.itschool.tableq.controller.api;

import com.itschool.tableq.repository.RestaurantRepository;
import com.itschool.tableq.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public class RestaurantController {
    @Autowired
    private RestaurantRepository restaurantRepository;

/*
    @GetMapping("/restaurant/{id}")
    public String getRestaurantDetails(@PathVariable Long id, Model model) {

    }
*/
}
