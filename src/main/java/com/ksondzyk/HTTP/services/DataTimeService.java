package com.ksondzyk.HTTP.services;

import com.ksondzyk.HTTP.dao.DateTimeDao;
import com.ksondzyk.HTTP.models.DateTime;

import java.sql.Time;
import java.time.LocalTime;

public class DataTimeService {
    public static DateTime getCurrentDateTimeMinusThreeHours() {
        DateTime currentDateTime = DateTimeDao.getCurrent();

        Time time = currentDateTime.getTime();
        LocalTime localTime = time.toLocalTime();

        LocalTime localTimeWithoutOffset = localTime.minusHours(3);

        Time updatedTime = Time.valueOf(localTimeWithoutOffset.toString());

        currentDateTime.setTime(updatedTime);

        return currentDateTime;
    }
}
