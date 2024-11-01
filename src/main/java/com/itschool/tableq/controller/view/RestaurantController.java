package com.itschool.tableq.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/restaurant")
public class RestaurantController {

    @GetMapping("/{id}")
    public String restaurant(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);
        return "restaurant";
    }

    @GetMapping("/modify/{id}")
    public String restaurantModify(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);
        return "restaurant-modify";
    }
}
