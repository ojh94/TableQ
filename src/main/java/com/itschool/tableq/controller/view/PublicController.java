package com.itschool.tableq.controller.view;

import com.itschool.tableq.domain.User;
import com.itschool.tableq.network.response.UserResponse;
import com.itschool.tableq.service.RestaurantService;
import com.itschool.tableq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PublicController {

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
}
