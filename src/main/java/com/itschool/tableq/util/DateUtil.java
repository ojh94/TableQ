package com.itschool.tableq.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateUtil {
    public static LocalDateTime getStartOfDay() {
        return LocalDate.now().atStartOfDay(); // 오늘 자정
    }

    public static LocalDateTime getEndOfDay() {
        return LocalDate.now().atTime(23, 59, 59, 999_999_999);
    }
}
