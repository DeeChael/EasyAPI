package org.ezapi.util;

import org.ezapi.returns.SextupleReturn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtils {

    public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

    public static Date parse(String format, String date) {
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static String now() {
        return format(yyyy_MM_dd_HH_mm_ss, new Date());
    }

    public static String format(String format, Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    private static SextupleReturn<Integer,Integer,Integer,Integer,Integer,Integer> time(Date date) {
        String time = format(yyyy_MM_dd_HH_mm_ss, date);
        int years = Integer.parseInt(time.split(" ")[0].split("-")[0]);
        int months = Integer.parseInt(time.split(" ")[0].split("-")[1]);
        int days = Integer.parseInt(time.split(" ")[0].split("-")[2]);
        int hours = Integer.parseInt(time.split(" ")[1].split(":")[0]);
        int minutes = Integer.parseInt(time.split(" ")[1].split(":")[1]);
        int seconds = Integer.parseInt(time.split(" ")[1].split(":")[2]);
        return new SextupleReturn<>(years, months, days, hours, minutes, seconds);
    }

}
