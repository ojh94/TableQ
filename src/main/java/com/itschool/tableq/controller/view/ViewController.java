package com.itschool.tableq.controller.view;

import com.itschool.tableq.network.request.Restaurant;
import com.itschool.tableq.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @Autowired
    private RestaurantRepository restaurantRepository;


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
