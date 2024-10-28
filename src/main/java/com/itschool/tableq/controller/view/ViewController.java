package com.itschool.tableq.controller.view;

import com.itschool.tableq.domain.User;
import com.itschool.tableq.network.Response.RestaurantResponse;
import com.itschool.tableq.network.Response.UserResponse;
import com.itschool.tableq.service.RestaurantService;
import com.itschool.tableq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ViewController {

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantService;

    // 홈
    @GetMapping("/")
    public String home(@AuthenticationPrincipal User user, Model model) {
        if(user != null) { // 로그인 한 상태
            model.addAttribute("user", user);
            return "index";
        } else { // 로그인 안 한 상태
            return "welcome";
        }
    }

    // Oauth 연동 페이지
    @GetMapping("/auth")
    public String auth() {
        return "auth";
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String login() { return "login"; }

    // 회원 가입 페이지
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/ownerApply")
    public String ownerApply() {
        return "ownerApply";
    }

    @GetMapping("/ownerLogin")
    public String ownerLogin() {
        return "ownerLogin";
    }

    @GetMapping("/ownerParticular")
    public String ownerParticular() {
        return "ownerParticular";
    }

    @GetMapping("/ownerParticularModify")
    public String ownerParticularModify() {
        return "ownerParticularModify";
    }

    // 관심매장 페이지
    @GetMapping("/favorites")
    public String favorites(@AuthenticationPrincipal User user, Model model) {
        if(user != null)
            model.addAttribute("user", user);
        return "favorites";
    }

    // 관심매장 페이지
    @GetMapping("/mypage")
    public String myPage(@AuthenticationPrincipal User user, Model model) {
        UserResponse userResponse = userService.read(user.getId()).getData();

        if(user != null)
            model.addAttribute("user", userResponse);
        return "mypage";
    }

    @GetMapping("/restaurant/{id}")
    public String getRestaurantDetails(@PathVariable Long id, Model model) { // 데이터베이스 연결 시 괄호 안에 넣기

        RestaurantResponse response = restaurantService.read(id).getData();

        model.addAttribute("restaurant", response);

        // restaurant-detail.html 파일을 반환 (템플릿 이름)
        return "restaurant-detail";
    }
}
