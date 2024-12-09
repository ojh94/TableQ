package com.itschool.tableq.controller.view;

import com.itschool.tableq.domain.User;
import com.itschool.tableq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/owner-signup")
    public String signupOwner(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("user", user);

        return "owner-signup";
    }

    // 편의시설 추가
    @GetMapping("/amenity")
    public String getAmenityPage(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("user", user);

        return "amenity-crud";
    }

    @GetMapping("/keyword")
    public String getKeywordPage(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("user", user);

        return "keyword-crud";
    }
}
