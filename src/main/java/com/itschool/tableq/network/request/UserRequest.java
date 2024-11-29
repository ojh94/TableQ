package com.itschool.tableq.network.request;

import com.itschool.tableq.domain.enumclass.MemberRole;
import com.itschool.tableq.network.request.base.SingleKeyRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserRequest extends SingleKeyRequest {

    @NotBlank
    @Size(min = 8, max = 16)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,16}$", message = "비밀번호는 최소 8자 이상, 숫자, 대소문자, 특수문자를 포함해야 합니다.")
    private String password;

    @NotBlank
    @Email(message = "유효한 이메일 주소를 입력하세요.")
    private String email;

    @NotBlank
    @Pattern(regexp = "^[가-힣]+$", message = "이름은 한글만 입력 가능합니다.")
    private String name; // 이름

    private String nickname;

    @NotBlank
    @Pattern(regexp = "^(01[0-9])-(\\d{3,4})-(\\d{4})$", message = "유효한 한국 핸드폰 번호를 입력하세요.")
    private String phoneNumber;

    private String address;

    /*private String socialType;

    private String socialId;*/

    private MemberRole memberRole;
}
