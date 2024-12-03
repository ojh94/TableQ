package com.itschool.tableq.controller.view;

import com.itschool.tableq.domain.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class NonPublicController {
    // (Not public 로그인 필요) 관심매장 페이지
    @GetMapping("/favorites")
    public String favorites(@AuthenticationPrincipal User user, Model model) {
        if(user != null)
            model.addAttribute("user", user);
        return "favorites";
    }

    @GetMapping("/search")
    public String search(@AuthenticationPrincipal User user, Model model) {
        if(user != null)
            model.addAttribute("user", user);
        return  "search";
    }
}
