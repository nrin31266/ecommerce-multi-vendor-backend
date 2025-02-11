package com.vanrin05.utils;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


@Component
public class DateUtil {
    public static Date parseToDate(String dateString, String dateFormat) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat (dateFormat);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.parse(dateString);
    }

    public static String formatDate(Date date, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat (dateFormat);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(date);
    }
}
