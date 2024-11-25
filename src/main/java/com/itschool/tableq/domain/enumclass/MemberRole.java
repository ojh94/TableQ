package com.itschool.tableq.domain.enumclass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRole {

    USER(0, "common", "일반 사용자"),
    OWNER(1, "owner", "음식점 점주"),
    ADMIN(2, "admin", "시스템 관리자")
    ;

    private Integer id;
    private String name;
    private String description;
}
