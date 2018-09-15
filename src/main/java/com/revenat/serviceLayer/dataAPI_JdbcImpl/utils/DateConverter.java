package com.revenat.serviceLayer.dataAPI_JdbcImpl.utils;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class DateConverter {

    public static LocalDate fromSlqDate(Date date) {
        return date.toLocalDate();
    }

    public static Date toSqlDate(LocalDate localDate) {
        return Date.valueOf(localDate);
    }
}
