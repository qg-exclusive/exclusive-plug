package com.qg.exclusiveplug.util;


import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author WilderGao
 * time 2018-09-23 11:49
 * motto : everything is no in vain
 * description
 */
@Slf4j
public class DateUtil {
    private static String PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static String DAY_PATTERN = "yyyy-MM-dd";

    public static String currentTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);
        return formatter.format(LocalDateTime.now());
    }

    public static Date getCurrentDate() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(PATTERN);
        return simpleDateFormat.parse(currentTime());
    }


    /**
     * 得到两个时间相差的秒数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 相差的秒数
     */
    public static int diffSecond(Date startTime, Date endTime) {
        return (int) ((endTime.getTime() - startTime.getTime()) / 1000);
    }


    /**
     * 获得该日期前几天的数据
     *
     * @param date 当前日期
     * @param num  前n天的数据
     * @return 前n天日期数组
     */
    public static String[] getDayBefore(Date date, int num) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DAY_PATTERN);
        String[] days = new String[num];
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);

        for (int i = 1; i <= num; i++) {
            LocalDateTime dateTime = localDateTime.minusDays(i);
            days[i - 1] = formatter.format(dateTime);
        }
        return days;
    }

    /**
     * String 转 date
     *
     * @param dateString 字符串表达式
     * @param pattern    转化格式
     * @return 返回结果
     */
    public static Date stringToDate(String dateString, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            log.info("日期转换格式有误");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断日期是否符合格式
     *
     * @param date 日期字符串
     * @return 符合返回true，否则为false
     */
    public static boolean isDate(String date) {
        String rexp = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?" +
                "((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|" +
                "(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?" +
                "((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?" +
                "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468]" +
                "[1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|" +
                "(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])" +
                "|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?" +
                "((0?[1-9])|(1[0-9])|(2[0-8]))))))";
        Pattern pat = Pattern.compile(rexp);
        Matcher mat = pat.matcher(date);
        return mat.matches();
    }

    /**
     * 判断是否符合yyyy-MM-dd HH:mm:ss
     * @param patternString 时间
     * @return 结果
     */
    public static boolean isTimeLegal(String patternString) {

        Pattern a = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}");
        Matcher b = a.matcher(patternString);
        return b.matches();
    }
}
