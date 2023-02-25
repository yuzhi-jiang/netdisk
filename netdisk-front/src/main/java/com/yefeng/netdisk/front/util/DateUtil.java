package com.yefeng.netdisk.front.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * This class is for
 *
 * @author 夜枫
 * @version 2023-02-25 14:51
 */
public class DateUtil {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String DATE_FORMAT1 = "yyyy-MM-dd HH:mm:ss";
    /**
     * 永久时间
     */
    public static final String PERMANENT = "9999-12-31T23:59:59Z";


    public static String getDateFormat(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT1);
        try {
            Date date1 = sdf.parse(date);
            sdf = new SimpleDateFormat(DATE_FORMAT);
            return sdf.format(date1);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    public static String getDateFormat(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
    }

    public static String getExpireDate(Long expireSeconds) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime after5Seconds = now.plusSeconds(expireSeconds);
        return dtf.format(after5Seconds);
    }


    public static void main(String[] args) throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = sdf.parse("");
        System.out.println(getDateFormat("2020-01-1 00:00:1"));
    }


}
