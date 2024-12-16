package com.itschool.tableq.controller.view;

import com.itschool.tableq.domain.User;
import com.itschool.tableq.service.ReservationService;
import com.itschool.tableq.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final ReservationService reservationService;

    @GetMapping("/{id}")
    public String restaurant(@PathVariable Long id, @AuthenticationPrincipal User user, Model model) {
        if (reservationService.isExistByRestaurantId(user, id)) { // 해당 레스토랑에 기존 예약이 있을때
            model.addAttribute("isExist", true);
        } else { // 예약이 없을때
            model.addAttribute("isExist", false);
        }
        model.addAttribute("id", id);
        model.addAttribute("user", user);
        return "restaurant";
    }

    @GetMapping("/modify/{id}")
    public String restaurantModify(@PathVariable Long id, @AuthenticationPrincipal User user, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("user", user);

        return "restaurant-modify";
    }

    @GetMapping("/reservation/apply/{restaurantId}")
    public String reservationApply(@PathVariable Long restaurantId, @AuthenticationPrincipal User user, Model model) {
        if (reservationService.isExistByRestaurantId(user, restaurantId)) { // 해당 레스토랑에 기존 예약이 있을때
            return "redirect:/restaurant/" + restaurantId;
        } else { // 예약이 없을때
            model.addAttribute("restaurantId", restaurantId);
            model.addAttribute("user", user);
            return "restaurant-reservation-apply";
        }
    }

    @GetMapping("/reservation/detail/{reservationId}")
    public String reservationDetail(@PathVariable Long reservationId, @AuthenticationPrincipal User user, Model model) {
        model.addAttribute("reservationId", reservationId);
        model.addAttribute("user", user);
        return "restaurant-reservation-detail";
    }
}
