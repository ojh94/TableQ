package com.itschool.tableq.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    @GetMapping("/")
    public String main() {
        return "welcome";
    }
    @GetMapping("/index")
    public String index() {
        return "index";
    }
    @GetMapping("/auth")
    public String auth() {
        return "auth";
    }
    @GetMapping("/login")
    public String login() { return "login"; }
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }
}
