package com.itschool.tableq.controller.api;

import com.itschool.tableq.controller.CrudController;
import com.itschool.tableq.domain.User;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.Response.UserResponse;
import com.itschool.tableq.network.request.UserRequest;
import com.itschool.tableq.service.UserService;
import groovy.util.logging.Slf4j;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자", description = "사용자 관련 API") // 문서에서 쉽게 찾도록 한글로 했음
@RestController
@RequestMapping("/api/user")
public class UserApiController extends CrudController<UserRequest, UserResponse, User> {
    @GetMapping("/check-email")
    public Header<Boolean> isDuplicated(@RequestParam String email){
        return Header.OK(((UserService)baseService).checkEmail(email));
    }
}
