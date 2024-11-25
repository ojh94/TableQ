package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.Owner;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OwnerResponse {
    private Long id;

    @NotBlank
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String phoneNumber;

    public static OwnerResponse of(Owner owner) {
        return OwnerResponse.builder()
                .id(owner.getId())
                .email(owner.getEmail())
                .name(owner.getName())
                .phoneNumber(owner.getPhoneNumber())
                .build();
    }
}
