package com.itschool.tableq.domain.enumclass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRole {

    USER("ROLE_USER", "일반 사용자"),
    OWNER("ROLE_OWNER", "음식점 점주"),
    ADMIN( "ROLE_ADMIN", "시스템 관리자")
    ;

    private final String role;
    private final String description;
}
