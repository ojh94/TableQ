package com.itschool.tableq.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/owner")
public class OwnerController {

    @GetMapping("/apply")
    public String ownerApply() {
        return "ownerApply";
    }

    @GetMapping("/login")
    public String ownerLogin() {
        return "ownerLogin";
    }

    @GetMapping("/mypage")
    public String ownerMypage() {
        return "owner-mypage";
    }

    @GetMapping("/welcome")
    public String getOwnerWelcome() { return "ownerWelcome";  }

    @GetMapping("/owner-auth")
    public String auth() {
        return "";
    }
}
