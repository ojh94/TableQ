package com.itschool.tableq.controller.view;

import com.itschool.tableq.domain.User;
import com.itschool.tableq.domain.enumclass.MemberRole;
import com.itschool.tableq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class PublicController {

    private final UserService userService;

    @Autowired
    public PublicController(UserService userService) {
        this.userService = userService;
    }

    @Value("${app.version:로컬 버전}")
    private String appVersion;

    // 버전 확인용 페이지
    @GetMapping("/version")
    public String getVersion(Model model) {
        model.addAttribute("version", appVersion);
        return "version"; // version.html로 리턴
    }
    
    // 홈
    @GetMapping("/")
    public String home(@AuthenticationPrincipal User user, Model model) {
        if(user == null) {
            // 로그인 안 한 상태
            return "welcome";
        } else {
            model.addAttribute("user", user);

            switch (user.getMemberRole()) {
                case USER:
                    return "index";
                case OWNER:
                    return "owner";
                case ADMIN:
                    return "admin";
            }
            throw new RuntimeException();
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
