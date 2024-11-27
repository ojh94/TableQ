package com.itschool.tableq.domain.enumclass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRole {

    USER("USER", "일반 사용자"),
    OWNER("OWNER", "음식점 점주"),
    ADMIN( "ADMIN", "시스템 관리자")
    ;

    private final String name;
    private final String description;
}
