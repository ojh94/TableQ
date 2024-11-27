package com.itschool.tableq.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static LocalDateTime get3DaysAgo() {
        LocalDateTime startDay = DateUtil.getStartOfDay().minusDays(3);
        return startDay;
    }

    public static LocalDateTime getStartOfDay() {
        return LocalDate.now().atStartOfDay(); // 오늘 자정
    }

    public static LocalDateTime getEndOfDay() {
        return LocalDate.now().atTime(23, 59, 59, 999_999_999);
    }
}
