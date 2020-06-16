package com.ksondzyk.HTTP.models;

import lombok.Data;

import java.sql.Time;
import java.util.Date;

@Data
public class DateTime {
    private Time time;
    private Date date;
    private String author = "trosha_b";
    private Integer bestMark = 100;
}
