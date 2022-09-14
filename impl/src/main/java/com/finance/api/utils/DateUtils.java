package com.finance.api.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static final SimpleDateFormat FORMAT1 =
            new SimpleDateFormat("dd:MM:yyyy");

    public static final SimpleDateFormat FORMAT2 =
            new SimpleDateFormat("yyyy-MM-dd");

    public static final SimpleDateFormat FORMAT3 =
            new SimpleDateFormat("MM-dd-yyyy");

    public static final SimpleDateFormat FORMAT4 =
            new SimpleDateFormat("MM/dd/yyyy");

    public static boolean isValidString(String dateStr, SimpleDateFormat format){
        boolean flag = false;
        try {
            format.parse(dateStr);
            flag = true;
        } catch (ParseException e) {
        }
        return flag;
    }

    public static String fromDate(Date date, SimpleDateFormat format){
        return format.format(date);
    }

    public static Date fromString(String date, String format) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.parse(date);
    }

    public static Date fromString(String date, SimpleDateFormat format) throws ParseException {
        return fromString(date, format.toPattern());
    }

    public static String changeFormat(String date, SimpleDateFormat oldFormat, SimpleDateFormat newFormat) {
        String result;
        try {
            Date d = oldFormat.parse(date);
            result = newFormat.format(d);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}
