package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.Owner;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    private LocalDateTime lastLoginAt;

    public OwnerResponse(Owner owner) {
        this.id = owner.getId();
        this.email = owner.getEmail();
        this.name = owner.getName();
        this.phoneNumber = owner.getPhoneNumber();
        this.lastLoginAt = LocalDateTime.now();
    }
}
