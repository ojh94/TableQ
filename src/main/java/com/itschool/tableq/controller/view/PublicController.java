package com.itschool.tableq.controller.view;

import com.itschool.tableq.domain.User;
import com.itschool.tableq.domain.enumclass.MemberRole;
import com.itschool.tableq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class PublicController {

    @Autowired
    UserService userService;

    // 홈
    @GetMapping("/")
    public String home(@AuthenticationPrincipal User user, Model model) {
        if(user != null && user.getMemberRole().equals(MemberRole.USER)) {
            model.addAttribute("user", user);
            return "index";
        } else if(user != null && user.getMemberRole().equals(MemberRole.OWNER)) {
            model.addAttribute("owner", user);
            return "owner-mypage";
        } else if(user != null && user.getMemberRole().equals(MemberRole.ADMIN)) {
            model.addAttribute("admin", user);
            return "admin";
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
