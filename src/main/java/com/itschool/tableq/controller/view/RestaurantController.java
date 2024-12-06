package com.itschool.tableq.controller.view;

import com.itschool.tableq.domain.Reservation;
import com.itschool.tableq.domain.User;
import com.itschool.tableq.domain.enumclass.MemberRole;
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
    public String restaurant(@PathVariable Long id, @AuthenticationPrincipal User user, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("user", user);
        return "restaurant";
    }

    @GetMapping("/modify/{id}")
    public String restaurantModify(@PathVariable Long id, @AuthenticationPrincipal User user, Model model) {
        if(user.getMemberRole() == MemberRole.OWNER) {
            model.addAttribute("id", id);
            model.addAttribute("user", user);

            return "restaurant-modify";

        } else {
            // 점주 로그인이 아닐 때
            return "ownerWelcome";
        }
    }

    @GetMapping("/reservation/apply/{restaurantId}")
    public String reservationApply(@PathVariable Long restaurantId, @AuthenticationPrincipal User user, Model model) {
        model.addAttribute("restaurantId", restaurantId);
        model.addAttribute("user", user);
        return "restaurant-reservation-apply";
    }

    @GetMapping("/reservation/detail/{reservationId}")
    public String reservationDetail(@PathVariable Long reservationId, @AuthenticationPrincipal User user, Model model) {
        model.addAttribute("reservationId", reservationId);
        model.addAttribute("user", user);
        return "restaurant-reservation-detail";
    }
}
