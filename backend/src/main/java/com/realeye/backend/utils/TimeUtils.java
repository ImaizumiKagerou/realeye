package com.realeye.backend.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtils {

    public static Date String2DateAndAddOneDay(String time) throws ParseException {
        String p = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        String r = "2021-03-01T16:00:00.000Z";
        SimpleDateFormat sdf = new SimpleDateFormat(p, Locale.CHINA);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT00:00"));
        long parseTime = sdf.parse(r).getTime() + 1000L * 60 * 60 * 24;
        return new Date(parseTime);
    }

}
