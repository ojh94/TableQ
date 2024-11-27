package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.User;
import com.itschool.tableq.domain.enumclass.MemberRole;
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

    private LocalDateTime lastLoginAt;

    private String address;

    @NotBlank
    private String name;

    private String socialType;

    private String socialId;

    @NotBlank
    @Email
    private String email;

    private MemberRole memberRole;

    public static UserResponse of(User user) {
        return UserResponse.builder()
            .id(user.getId())
            .nickname(user.getNickname())
            .phoneNumber(user.getPhoneNumber())
            .lastLoginAt(user.getLastLoginAt())
            .address(user.getAddress())
            .name(user.getName())
            .socialType(user.getSocialType())
            .socialId(user.getSocialId())
            .memberRole(user.getMemberRole())
            .build();
    }
}