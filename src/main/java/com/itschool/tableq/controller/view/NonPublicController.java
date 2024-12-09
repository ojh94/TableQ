package com.itschool.tableq.controller.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.User;
import com.itschool.tableq.network.response.RestaurantResponse;
import com.itschool.tableq.service.RestaurantLogicService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/")
public class NonPublicController {

    private final RestaurantLogicService restaurantLogicService;

    private final ObjectMapper objectMapper;

    // (Not public 로그인 필요) 관심매장 페이지
    @GetMapping("/favorites")
    public String favorites(@AuthenticationPrincipal User user, Model model) {
        if(user != null)
            model.addAttribute("user", user);
        return "favorites";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String searchKeyword,
                         @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 6) Pageable pageable,
                         @AuthenticationPrincipal User user,
                         Model model) throws JsonProcessingException {

        if(user != null) {
            model.addAttribute("user", user);
        }

        /*List<RestaurantResponse> restaurantList = restaurantLogicService.searchByName(searchKeyword, pageable).getData();
        ObjectMapper objectMapper = new ObjectMapper();
        String restaurantListJson = objectMapper.writeValueAsString(restaurantList);
        model.addAttribute("restaurantList", restaurantListJson);*/

        model.addAttribute("searchKeyword", searchKeyword);

        return "search";
    }
}
