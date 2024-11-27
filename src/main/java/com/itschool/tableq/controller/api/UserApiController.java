package com.itschool.tableq.controller.api;

import com.itschool.tableq.controller.CrudController;
import com.itschool.tableq.domain.User;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.response.UserResponse;
import com.itschool.tableq.network.request.UserRequest;
import com.itschool.tableq.service.UserService;
import groovy.util.logging.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "사용자", description = "사용자 관련 API") // 문서에서 쉽게 찾도록 한글로 했음
@RestController
@RequestMapping("/api/user")
public class UserApiController extends CrudController<UserRequest, UserResponse, User> {

    @Override
    protected Class<UserRequest> getRequestClass() {
        return UserRequest.class;
    }

    @Override
    @Operation(summary = "수정", description = "ID로 엔티티 및 세션 업데이트")
    @PutMapping("{id}")
    public Header<UserResponse> update(@PathVariable(name = "id") Long id,
                              @RequestBody Header<UserRequest> request) {
        Header<UserResponse> response = super.update(id, request);

        // 사용자 정보를 새로 설정하기 위한 추가 작업
        if (response.getData() != null) {
            UserResponse userResponse = response.getData();

            // 현재 인증된 사용자의 정보를 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // 새로운 사용자 정보로 인증 객체 생성
            UserDetails newUserDetails = User.builder()
                    .id(userResponse.getId())
                    .email(userResponse.getEmail())
                    .nickname(userResponse.getNickname())
                    .phoneNumber(userResponse.getPhoneNumber())
                    .lastLoginAt(userResponse.getLastLoginAt())
                    .address(userResponse.getAddress())
                    .name(userResponse.getName())
                    .socialType(userResponse.getSocialType())
                    .socialId(userResponse.getSocialId())
                    .build();

            // 새로운 인증 객체 설정
            authentication = new UsernamePasswordAuthenticationToken(newUserDetails, authentication.getCredentials(), newUserDetails.getAuthorities());

            // SecurityContext에 새로운 인증 객체 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        return response;
    }

    @Operation(summary = "이메일 중복 확인", description = "가입이 가능할 때 true 반환")
    @GetMapping("/check-email")
    public Header<Boolean> checkEamil(@RequestParam(name = "email") String email){
        return Header.OK(((UserService)baseService).checkEmail(email));
    }

    @Operation(summary = "전화번호 중복확인")
    @GetMapping("/check-phonenumber")
    public Header<Boolean> checkPhoneNumber(@RequestParam(name = "phoneNumber") String phoneNumber){
        return Header.OK(((UserService)baseService).checkPhoneNumber(phoneNumber));
    }
}
