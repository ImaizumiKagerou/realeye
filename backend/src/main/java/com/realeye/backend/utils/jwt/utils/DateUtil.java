package com.realeye.backend.utils.jwt.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 */
public class DateUtil {

    public static final String FULL_TIME_PATTERN = "yyyyMMddHHmmss";

    public static final String FULL_TIME_SPLIT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static String formatFullTime(LocalDateTime localDateTime) {
        return formatFullTime(localDateTime, FULL_TIME_PATTERN);
    }

    public static String formatFullTime(LocalDateTime localDateTime, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(dateTimeFormatter);
    }

    private static String getDateFormat(Date date, String dateFormatType) {
        SimpleDateFormat simformat = new SimpleDateFormat(dateFormatType);
        return simformat.format(date);
    }

    public static String formatCSTTime(String date, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        Date d = sdf.parse(date);
        return DateUtil.getDateFormat(d, format);
    }

    /**
     * 在日期上加小时月，得到新的日期
     * @return
     */
    public static Date addHourToDate(Date date,int hour){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, hour); // 目前时间加小時
        return c.getTime();
    }
    /**
     * 取当前月的毫秒值
     */
    public  static Long  getMonthBegain(){
        try {
            SimpleDateFormat simformat = new SimpleDateFormat("yyyyMM");
            String format = simformat.format(new Date());
            Date parse = simformat.parse(format);
            return  parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            throw  new RuntimeException();
        }
    }
}
