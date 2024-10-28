package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserResponse {

    private Long id;

    private String nickname;

    @NotBlank
    @Pattern(regexp = "^\\+?\\d{10,15}$", message = "유효한 핸드폰 번호를 입력하세요.") // 핸드폰 번호 정규 표현식
    private String phoneNumber;

    private LocalDateTime createdAt;

    private LocalDateTime lastLoginAt;

    private String address;

    @NotBlank
    private String name;

    private String socialType;

    private String socialId;

    @NotBlank
    @Email
    private String email;

    public UserResponse(User user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.phoneNumber = user.getPhoneNumber();
        this.createdAt = user.getCreatedAt();
        this.lastLoginAt = user.getLastLoginAt();
        this.address = user.getAddress();
        this.name = user.getName();
        this.socialType = user.getSocialType();
        this.socialId = user.getSocialId();
    }
}