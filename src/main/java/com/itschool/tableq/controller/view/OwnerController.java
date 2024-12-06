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
@RequestMapping("/owner")
public class OwnerController {

    @Autowired
    private UserService userService;

    @GetMapping("/apply")
    public String ownerApply() {
        return "ownerApply";
    }

    @GetMapping("/login")
    public String ownerLogin() {
        return "ownerLogin";
    }

    @GetMapping("/welcome")
    public String getOwnerWelcome() { return "ownerWelcome";  }

    @GetMapping("/owner-auth")
    public String auth() {
        return "owner-auth";
    }

    @GetMapping("/reservation/{restaurantId}")
    public String getOwnerReservation(@PathVariable Long restaurantId, @AuthenticationPrincipal User user, Model model) {

        model.addAttribute("restaurantId", restaurantId);
        model.addAttribute("user", user);

        return "owner-reservation";
    }

    /*@GetMapping("/mypage")
    public String getOwnerMypage(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("user", user);

        return "owner-mypage";
    }*/

    /*@GetMapping("/password")
    public String getOwnerPassword(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("user", user);

        return "owner-password";
    }*/
}
