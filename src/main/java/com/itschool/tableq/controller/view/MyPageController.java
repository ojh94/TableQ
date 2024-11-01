package com.itschool.tableq.controller.view;

import com.itschool.tableq.domain.User;
import com.itschool.tableq.network.response.UserResponse;
import com.itschool.tableq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage")
public class MyPageController {

    @Autowired
    private UserService userService;

    // 마이 페이지
    @GetMapping("/")
    public String myPage(@AuthenticationPrincipal User user, Model model) {
        UserResponse userResponse = userService.read(user.getId()).getData();

        if(user != null)
            model.addAttribute("user", userResponse);
        return "mypage";
    }
}
