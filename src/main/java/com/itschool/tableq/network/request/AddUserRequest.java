package com.itschool.tableq.network.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.security.Timestamp;

@Getter
@Setter
public class AddUserRequest {

    private Long id;

    @NotBlank
    @Size(min=8)
    private String password;

    private String nickname;

    @NotBlank
    @Pattern(regexp = "^\\+?\\d{10,15}$", message = "유효한 핸드폰 번호를 입력하세요.") // 핸드폰 번호 정규 표현식
    private String phoneNumber;

    private Timestamp createdAt;

    private Timestamp lastLoginAt;

    private String address;

    @NotBlank
    private String name;

    private String socialType;

    private String socialId;

    @NotBlank
    @Email
    private String email;
}
