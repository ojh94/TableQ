package com.itschool.tableq.controller.view;

import com.itschool.tableq.domain.User;
import com.itschool.tableq.domain.enumclass.MemberRole;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.OwnerRequest;
import com.itschool.tableq.network.request.UserRequest;
import com.itschool.tableq.network.response.OwnerResponse;
import com.itschool.tableq.network.response.UserResponse;
import com.itschool.tableq.service.OwnerService;
import com.itschool.tableq.service.RestaurantService;
import com.itschool.tableq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class PublicController {

    @Autowired
    UserService userService;

    @Autowired
    OwnerService ownerService;

    // 홈
    @GetMapping("/")
    public String home(@AuthenticationPrincipal User user, Model model) {
        if(user != null && user.getMemberRole().equals(MemberRole.USER.getName())) { // 로그인 한 상태
            model.addAttribute("user", user);
            return "index";
        }else if(user != null && user.getMemberRole().equals(MemberRole.OWNER.getName())) { // 로그인 한 상태
            model.addAttribute("owner", user);
            return "owner-mypage";
        }
        else { // 로그인 안 한 상태
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

    @PostMapping("/user")
    public String signup(@ModelAttribute UserRequest userRequest) { // BindingResult bindingResult
        Header<UserResponse> userResponse = userService.create(Header.OK(userRequest));
        
        if(userResponse != null) {
            return "redirect:/login";   
        }
        
        throw new NullPointerException("생성된 유저가 없음");
    }
    @PostMapping("/owner")
    public String signup(@ModelAttribute OwnerRequest ownerRequest) { // BindingResult bindingResult
        Header<OwnerResponse> ownerResponse = ownerService.create(Header.OK(ownerRequest));

        if(ownerResponse != null) {
            return "redirect:/owner/login";
        }

        throw new NullPointerException("생성된 유저가 없음");
    }
}
