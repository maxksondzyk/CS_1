package com.ksondzyk.HTTP.dao;

import com.ksondzyk.HTTP.models.DateTime;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;

/* !!! IT'S NOT DAO PATTERN, JUST FOR EXAMPLE !!! */

public class DateTimeDao {
    public static DateTime getCurrent() {new DateTime();
        DateTime currentDateTime = new DateTime();

        LocalDateTime nowLocalDateTime = LocalDateTime.now();

        Date currentDate = Date.valueOf(nowLocalDateTime.toLocalDate());
        currentDateTime.setDate(currentDate);

        Time currentTime = Time.valueOf(nowLocalDateTime.toLocalTime());
        currentDateTime.setTime(currentTime);

        return currentDateTime;
    }
}
