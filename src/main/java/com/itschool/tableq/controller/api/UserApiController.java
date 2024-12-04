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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Tag(name = "사용자", description = "사용자 관련 API") // 문서에서 쉽게 찾도록 한글로 했음
@RestController
@RequestMapping("/api/user")
public class UserApiController extends CrudController<UserRequest, UserResponse, User> {

    // 생성자
    @Autowired
    public UserApiController(UserService baseService) {
        super(baseService);
    }

    @Override
    protected Class<UserRequest> getRequestClass() {
        return UserRequest.class;
    }

    @Override
    @Operation(summary = "일반 사용자 생성", description = "새로운 일반 사용자를 생성")
    @PostMapping("")
    public Header<UserResponse> create(@RequestBody @Valid Header<UserRequest> request) {
        log.info("{}","{}", "create : ", request);
        try {
            if(request.getData() != null)
                return ((UserService)baseService).createUserRole(request);
            throw new IllegalArgumentException("Json 객체 내 data 내 email 값이 null");
        } catch (Exception e) {
            log.error("엔티티 생성 중 오류 발생", e);
            return Header.ERROR(e.getClass().getSimpleName() + " : " + e.getCause());
        }
    }

    @Operation(summary = "점주 회원가입", description = "새로운 점주 생성")
    @PostMapping("/owner-role")
    public Header<UserResponse> createOwner(@RequestBody @Valid Header<UserRequest> request) {
        log.info("{}","{}", "createUser : ", request);
        try {
            if(request.getData() != null)
                return ((UserService)baseService).createOwnerRole(request);
            throw new Exception("Json 객체 내 data 내 email 값이 null");
        } catch (Exception e) {
            log.error("엔티티 생성 중 오류 발생", e);
            return Header.ERROR(e.getClass().getSimpleName() + e.getMessage());
        }
    }

    @Override
    @Operation(summary = "수정", description = "ID로 엔티티 및 세션 업데이트")
    @PutMapping("/{id}")
    public Header<UserResponse> update(@PathVariable(name = "id") Long id,
                              @RequestBody @Valid Header<UserRequest> request) {
        Header<UserResponse> response = super.update(id, request);

        // 사용자 정보를 새로 설정하기 위한 추가 작업
        if (response.getData() != null) {
            UserResponse userResponse = response.getData();

            // 현재 인증된 사용자의 정보를 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // 새로운 사용자 정보로 인증 객체 생성
            UserDetails newUserDetails = User.builder()
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
            authentication = new UsernamePasswordAuthenticationToken(newUserDetails, authentication.getCredentials(), ((User)newUserDetails).getAuthorities());

            // SecurityContext에 새로운 인증 객체 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        return response;
    }

    @Operation(summary = "이메일 중복 확인", description = "가입이 가능할 때 true 반환")
    @PostMapping("/check-email")
    public Header<Boolean> checkEmail(@RequestBody Header<UserRequest> request){
        try {
            if(request.getData().getEmail() != null)
                return Header.OK(((UserService)baseService).checkEmail(request));
            throw new RuntimeException("Json 객체 내 data 내 email 값이 null");
        } catch (Exception e) {
            return Header.ERROR(e.getClass().getSimpleName() + e.getMessage());
        }
    }

    @Operation(summary = "전화번호 중복확인")
    @PostMapping("/check-phonenumber")
    public Header<Boolean> checkPhoneNumber(@RequestBody Header<UserRequest> request){
        try {
            if(request.getData().getPhoneNumber() != null)
                return Header.OK(((UserService)baseService).checkPhoneNumber(request));
            throw new RuntimeException("Json 객체 내 data 내 phoneNumber 값이 null");
        } catch (Exception e) {
            return Header.ERROR(e.getClass().getSimpleName() + e.getMessage());
        }
    }
}