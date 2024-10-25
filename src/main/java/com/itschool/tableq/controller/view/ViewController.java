package com.itschool.tableq.controller.view;

import com.itschool.tableq.domain.User;
import com.itschool.tableq.network.response.UserResponse;
import com.itschool.tableq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @Autowired
    private UserService userService;

    /*@Autowired
    private Restaurant restaurantRepository;*/

    // 홈
    @GetMapping("/")
    public String home(@AuthenticationPrincipal User user, Model model) {
        if(user != null) { // 로그인 한 상태
            model.addAttribute("user", user);
            return "index";
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

    // 관심매장 페이지
    @GetMapping("/favorites")
    public String favorites(@AuthenticationPrincipal User user, Model model) {
        if(user != null)
            model.addAttribute("user", user);
        return "favorites";
    }

    // 관심매장 페이지
    @GetMapping("/mypage")
    public String myPage(@AuthenticationPrincipal User user, Model model) {
        UserResponse userResponse = userService.read(user.getId()).getData();

        if(user != null)
            model.addAttribute("user", userResponse);
        return "mypage";
    }


    @GetMapping("/restaurant/{id}")
    public String getRestaurantDetails() { // 데이터베이스 연결 시 괄호 안에 넣기 @PathVariable Long id, Model model

//        // 레스토랑 ID에 해당하는 정보를 데이터베이스에서 조회
//        Restaurant restaurant = restaurantRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid restaurant Id:" + id));
//
//        // 편의시설과 해시태그를 배열로 변환하여 템플릿에 전달
//        String[] facilitiesArray = restaurant.getFacilities().split(",");
//        String[] hashtagsArray = restaurant.getHashtags().split(",");
//
//        // 모델에 데이터 추가
//        model.addAttribute("restaurant", restaurant);
//        model.addAttribute("facilities", facilitiesArray);
//        model.addAttribute("hashtags", hashtagsArray);

        // restaurant-detail.html 파일을 반환 (템플릿 이름)
        return "restaurant-detail";
    }
}
