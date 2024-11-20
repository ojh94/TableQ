package com.itschool.tableq.controller.view;

import com.itschool.tableq.domain.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/waiting/{id}")
    public String waiting(@PathVariable Long id, @AuthenticationPrincipal User user, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("user", user);
        return "restaurant-waiting";
    }
}
