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

    // 레스토랑 id로 레스토랑 예약 현황 조회
    @GetMapping("/reservation/{restaurantId}")
    public String getOwnerReservation(@PathVariable Long restaurantId, @AuthenticationPrincipal User user, Model model) {

        model.addAttribute("restaurantId", restaurantId);
        model.addAttribute("user", user);

        return "owner-reservation";
    }

    // 예약 상황
    @GetMapping("/reservation")
    public String getReservation(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("user", user);

        return "reservation-crud";
    }

    // 사업자정보 설정 페이지
    @GetMapping("/business-info")
    public String getBusinessInfo(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("user", user);

        return "businessInfo-crud";
    }

    // 점포 설정 페이지
    @GetMapping("/restaurant")
    public String getRestaurant(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("user", user);

        return "restaurant-crud";
    }

    // 점포 이미지 설정 페이지
    @GetMapping("/restaurant-image")
    public String getRestaurantImage(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("user", user);

        return "restaurant-image-crud";
    }

    // 운영시간 설정 페이지
    @GetMapping("/openinghour")
    public String getOpeninghour(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("user", user);

        return "openinghour-crud";
    }

    // 휴무시간 설정 페이지
    @GetMapping("/breakhour")
    public String getBreakhour(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("user", user);

        return "breakhour-crud";
    }

    // 메뉴 설정 페이지
    @GetMapping("/menu-item")
    public String getMenuItem(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("user", user);

        return "menu-item-crud";
    }

    // 점포 편의시설 설정 페이지
    @GetMapping("/restaurant-amenity")
    public String getRestaurantAmenity(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("user", user);

        return "restaurant-amenity-crud";
    }

    // 점포 키워드 설정 페이지
    @GetMapping("/restaurant-keyword")
    public String getRestaurantKeyword(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("user", user);

        return "restaurant-keyword-crud";
    }
}
