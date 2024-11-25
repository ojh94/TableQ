package com.itschool.tableq.domain.enumclass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReservationStatus {
    WAITING(null, "wating", "대기 중"),
    COMPLETED(true, "completed", "입장 완료"),
    CANCEL(false, "cancel", "예약 취소(노쇼 또는 사용자가 직접 취소)")
    ;

    private Boolean value;
    private String name;
    private String description;
}
