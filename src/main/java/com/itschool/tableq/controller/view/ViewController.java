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
    @GetMapping("/login1")
    public String login1() {
        return "login1";
    }
    @GetMapping("/login2")
    public String login2() { return "login2"; }
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }
}
