package com.finance.api.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static final SimpleDateFormat FORMAT1 =
            new SimpleDateFormat("dd:MM:yyyy");

    public static final SimpleDateFormat FORMAT2 =
            new SimpleDateFormat("yyyy-MM-dd");

    public static boolean isValidString(String dateStr){
        boolean flag = false;
        try {
            FORMAT1.parse(dateStr);
            flag = true;
        } catch (ParseException e) {
        }
        return flag;
    }

    public static String fromDate(Date date){
        return FORMAT1.format(date);
    }

    public static Date fromString(String date) throws ParseException {
        return FORMAT1.parse(date);
    }

    public static Date fromString(String date, String format) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.parse(date);
    }

}
