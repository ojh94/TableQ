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
    private String userName;

    @NotBlank
    private String name;

    @NotBlank
    private String phoneNumber;

    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;

    public OwnerResponse(Owner owner) {
        this.id = owner.getId();
        this.userName = owner.getUsername();
        this.name = owner.getName();
        this.phoneNumber = owner.getPhoneNumber();
        this.createdAt = owner.getCreatedAt();
        this.lastLoginAt = LocalDateTime.now();
    }
}
