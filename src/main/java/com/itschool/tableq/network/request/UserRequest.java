package com.itschool.tableq.network.request;

import com.itschool.tableq.domain.enumclass.MemberRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRequest {

    private Long id;

    @NotBlank
    @Size(min=8)
    private String password;

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

    private MemberRole memberRole;
}
