package com.itschool.tableq.controller.api;

import com.itschool.tableq.network.request.AddOwnerRequest;
import com.itschool.tableq.service.OwnerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class OwnerApiController {

    private final OwnerService ownerService;

    @PostMapping("/owner")
    public String addOwner(@ModelAttribute AddOwnerRequest request, BindingResult bindingResult) {
        ownerService.save(request);
        return "redirect:/ownerlogin";
    }

    @GetMapping("/ownerlogout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/ownerlogin";
    }

    @GetMapping("/ownerlogin")
    public String ownerlogin(HttpServletRequest request, HttpServletResponse response) {
        return "ownerLogin";
    }
}
