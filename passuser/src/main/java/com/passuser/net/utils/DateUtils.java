package com.passuser.net.utils;

import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 工具类-日期处理
 *
 * @author xx
 * @version 1.0
 * @since 2014年1月28日
 */
public class DateUtils {

    /**
     * 获得当前日期
     *
     * @return
     */
    public static Date getNow() {
        Calendar cal = Calendar.getInstance();
        Date currDate = cal.getTime();
        return currDate;
    }

    /**
     * 日期转换为字符串 格式自定义
     *
     * @param date
     * @param f
     * @return
     */
    public static String dateStr(Date date, String f) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(f);
        String str = format.format(date);
        return str;
    }

    /**
     * 日期转换为字符串 MM月dd日 hh:mm
     *
     * @param date
     * @return
     */
    public static String dateStr(Date date) {
        return dateStr(date, "MM月dd日 hh:mm");
    }

    /**
     * 日期转换为字符串 yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String dateStr2(Date date) {
        return dateStr(date, "yyyy-MM-dd");
    }

    /**
     * yyyy年MM月dd日HH时mm分ss秒
     *
     * @param date
     * @return
     */
    public static String dateStr5(Date date) {
        return dateStr(date, "yyyy年MM月dd日 HH时mm分ss秒");
    }

    /**
     * yyyyMMddHHmmss
     *
     * @param date
     * @return
     */
    public static String dateStr3(Date date) {
        return dateStr(date, "yyyyMMddHHmmss");
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String dateStr4(Date date) {
        return dateStr(date, "yyyy-MM-dd HH:mm:ss");

    }

    /**
     * yyyy年MM月dd日
     *
     * @param date
     * @return
     */
    public static String dateStr6(Date date) {
        return dateStr(date, "yyyy年MM月dd日");
    }

    /**
     * yyyyMMdd
     *
     * @param date
     * @return
     */
    public static String dateStr7(Date date) {
        return dateStr(date, "yyyyMMdd");
    }

    /**
     * MM-dd
     *
     * @param date
     * @return
     */
    public static String dateStr8(Date date) {
        return dateStr(date, "MM-dd");
    }

    /**
     * HH:mm
     *
     * @param date
     * @return
     */
    public static String dateStr9(Date date) {
        return dateStr(date, "HH:mm");
    }

    /**
     * 将时间戳转换为Date
     *
     * @param times
     * @return
     */
    public static Date getDate(String times) {
        long time = Long.parseLong(times);
        return new Date(time * 1000);
    }

    public static String dateStr(String times) {
        return dateStr(getDate(times));
    }

    public static String dateStr2(String times) {
        return dateStr2(getDate(times));
    }

    public static String dateStr3(String times) {
        return dateStr3(getDate(times));
    }

    public static String dateStr4(String times) {
        return dateStr4(getDate(times));
    }

    public static String dateStr5(String times) {
        return dateStr5(getDate(times));
    }

    /**
     * 将Date转换为时间戳
     *
     * @param date
     * @return
     */
    public static long getTime(Date date) {
        return date.getTime() / 1000;
    }

    public static int getDay(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * s - 表示 "yyyy-mm-dd" 形式的日期的 String 对象
     *
     * @param
     * @return
     */
    public static Date valueOf(String s) {
        final int YEAR_LENGTH = 4;
        final int MONTH_LENGTH = 2;
        final int DAY_LENGTH = 2;
        final int MAX_MONTH = 12;
        final int MAX_DAY = 31;
        int firstDash;
        int secondDash;
        int threeDash = 0;
        int fourDash = 0;
        Date d = null;

        if (s == null) {
            throw new IllegalArgumentException();
        }

        firstDash = s.indexOf('-');
        secondDash = s.indexOf('-', firstDash + 1);
        if (s.contains(":")) {
            threeDash = s.indexOf(':');
            fourDash = s.indexOf(':', threeDash + 1);
        }
        if ((firstDash > 0) && (secondDash > 0) && (secondDash < s.length() - 1)) {
            String yyyy = s.substring(0, firstDash);
            String mm = s.substring(firstDash + 1, secondDash);
            String dd = "";
            String hh = "";
            String MM = "";
            String ss = "";
            if (s.contains(":")) {
                dd = s.substring(secondDash + 1, threeDash - 3);
                hh = s.substring(threeDash - 2, threeDash);
                MM = s.substring(threeDash + 1, fourDash);
                ss = s.substring(fourDash + 1);
            } else {
                dd = s.substring(secondDash + 1);
            }
            if (yyyy.length() == YEAR_LENGTH && mm.length() == MONTH_LENGTH && dd.length() == DAY_LENGTH) {
                int year = Integer.parseInt(yyyy);
                int month = Integer.parseInt(mm);
                int day = Integer.parseInt(dd);
                int hour = 0;
                int minute = 0;
                int second = 0;
                if (s.contains(":")) {
                    hour = Integer.parseInt(hh);
                    minute = Integer.parseInt(MM);
                    second = Integer.parseInt(ss);
                }
                if (month >= 1 && month <= MAX_MONTH) {
                    int maxDays = MAX_DAY;
                    switch (month) {
                        // February determine if a leap year or not
                        case 2:
                            if ((year % 4 == 0 && !(year % 100 == 0)) || (year % 400 == 0)) {
                                maxDays = MAX_DAY - 2; // leap year so 29 days in
                                // February
                            } else {
                                maxDays = MAX_DAY - 3; // not a leap year so 28 days
                                // in February
                            }
                            break;
                        // April, June, Sept, Nov 30 day months
                        case 4:
                        case 6:
                        case 9:
                        case 11:
                            maxDays = MAX_DAY - 1;
                            break;
                    }
                    if (day >= 1 && day <= maxDays) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(year, month - 1, day, hour, minute, second);
                        cal.set(Calendar.MILLISECOND, 0);
                        d = cal.getTime();
                    }
                }
            }
        }
        if (d == null) {
            throw new IllegalArgumentException();
        }
        return d;
    }

    /**
     * 获取指定日期星期几
     *
     * @param dt
     * @return
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }


    /**
     * 获取指定日期星期几(int)
     *
     * @param dt
     * @return
     */
    public static int getWeekOfInt(Date dt) {
        int[] weekDays = {7, 1, 2, 3, 4, 5, 6};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }


    /**
     * 前/后?分钟
     *
     * @param d
     * @param minute
     * @return
     */
    public static Date rollMinute(Date d, int minute) {
        return new Date(d.getTime() + minute * 60 * 1000);
    }

    /**
     * 前/后?天
     *
     * @param d
     * @param day
     * @return
     */
    public static Date rollDay(Date d, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }

    /**
     * 前/后?月
     *
     * @param d
     * @param mon
     * @return
     */
    public static Date rollMon(Date d, int mon) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MONTH, mon);
        return cal.getTime();
    }

    /**
     * 前/后?年
     *
     * @param d
     * @param year
     * @return
     */
    public static Date rollYear(Date d, int year) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.YEAR, year);
        return cal.getTime();
    }

    public static Date rollDate(Date d, int year, int mon, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.YEAR, year);
        cal.add(Calendar.MONTH, mon);
        cal.add(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }

    /**
     * 获取当前时间-时间戳字符串
     *
     * @return
     */
    public static String getNowTimeStr() {
        String str = Long.toString(System.currentTimeMillis() / 1000);
        return str;
    }

    /**
     * 获取当前时间-时间戳
     *
     * @return
     */
    public static long getNowTime() {
        return System.currentTimeMillis();
    }

    /**
     * 将Date转换为时间戳
     *
     * @param time
     * @return
     */
    public static String getTimeStr(Date time) {
        long date = time.getTime();
        String str = Long.toString(date / 1000);
        return str;
    }

    public static String getTimeStr(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date;
        try {
            date = sdf.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        String str = DateUtils.getTimeStr(date);
        return str;
    }


    public static Date getIntegralTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取当天23:59:59
     *
     * @return
     */
    public static Date getLastIntegralTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 设置时间格式为 23:59:59
     *
     * @return
     */
    public static Date getLastSecIntegralTime(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(d.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取本周日的日期
     */
    public static String getCurrentWeekday() {
        int weeks = 0;
        int mondayPlus = DateUtils.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
        Date monday = currentDate.getTime();

        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    /**
     * 获取时间戳
     *
     * @param format 日期时间
     * @return
     */
    public static long getTime(String format) {
        long t = 0;
        if (StringUtils.isEmpty(format)) {
            return t;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = sdf.parse(format);
            t = date.getTime() / 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 获得当前日期与本周日相差的天数
     */
    private static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1;
        // 因为按中国礼拜一作为第一天所以这里减1
        if (dayOfWeek == 1) {
            return 0;
        } else {
            return 1 - dayOfWeek;
        }
    }


    /**
     * 获得本周一的日期
     *
     * @return
     */
    public static String getMondayOFWeek() {
        int weeks = 0;
        int mondayPlus = DateUtils.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        Date monday = currentDate.getTime();

        DateFormat df = DateFormat.getDateInstance();
        String preMonday = df.format(monday);
        return preMonday;
    }

    /**
     * 获取当前月第一天：
     */
    public static String getFirstDayOfMonth(String first) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        // 设置为1号,当前日期既为本月第一天
        c.set(Calendar.DAY_OF_MONTH, 1);
        first = format.format(c.getTime());
        return first;
    }

    /**
     * 获取当月最后一天
     */
    public static String getLastDayOfMonth(String last) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        last = format.format(ca.getTime());
        return last;
    }

    /**
     * 获取当月最后一天 天数
     */
    public static Integer getLastDayOfMonthInt(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        return ca.get(Calendar.DATE);
    }

    /**
     * 日期月份处理
     *
     * @param d     时间
     * @param month 相加的月份，正数则加，负数则减
     * @return
     */
    public static Date timeMonthManage(Date d, int month) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(d);
        rightNow.add(Calendar.MONTH, month);
        return rightNow.getTime();
    }

    /**
     * 获取指定年月的最后一天
     *
     * @param year_time  指定年
     * @param month_time 指定月
     * @return
     */
    public static Date monthLastDay(int year_time, int month_time) {
        Calendar cal = Calendar.getInstance();
        cal.set(year_time, month_time, 0, 23, 59, 59);
        return cal.getTime();
    }

    /**
     * 获取指定年月的第一天
     *
     * @param year_time  指定年
     * @param month_time 指定月
     * @return
     */
    public static Date monthFirstDay(int year_time, int month_time) {
        Calendar cal = Calendar.getInstance();
        cal.set(year_time, month_time - 1, 1, 0, 0, 0);
        return cal.getTime();
    }

    /**
     * 获取指定时间月的第一天
     *
     * @param d 指定时间，为空代表当前时间
     * @return
     */
    public static Date currMonthFirstDay(Date d) {
        Calendar cal = Calendar.getInstance();
        if (d != null) {
            cal.setTime(d);
        }
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);
        return cal.getTime();
    }

    /**
     * 获取指定时间月的最后一天
     *
     * @param d 指定时间，为空代表当前时间
     * @return
     */
    public static Date currMonthLastDay(Date d) {
        Calendar cal = Calendar.getInstance();
        if (d != null)
            cal.setTime(d);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 23, 59, 59);
        return cal.getTime();
    }

    /**
     * 获取指定时间的年
     *
     * @param date 指定时间
     * @return
     */
    public static int getTimeYear(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获取指定时间的月
     *
     * @param date 指定时间
     * @return
     */
    public static int getTimeMonth(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取指定时间的天
     *
     * @param date 指定时间
     * @return
     */
    public static int getTimeDay(Date date) {
        if (date == null)
            date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DATE);
    }

    public static Date getFirstSecIntegralTime(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(d.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.DATE, 0);
        return cal.getTime();
    }

    /**
     * 获取指定时间天的结束时间
     *
     * @param d
     * @return
     */
    public static Date getDayEndTime(long d) {
        Date day = null;
        if (d <= 0) {
            day = new Date();
        } else {
            day = new Date(d * 1000);
        }
        Calendar cal = Calendar.getInstance();
        if (day != null) {
            cal.setTimeInMillis(day.getTime());
        }
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 23, 59, 59);
        return cal.getTime();
    }

    /**
     * 获取指定时间天的开始时间
     *
     * @param d
     * @return
     */
    public static Date getDayStartTime(long d) {
        Date day = null;
        if (d <= 0) {
            day = new Date();
        } else {
            day = new Date(d * 1000);
        }
        Calendar cal = Calendar.getInstance();
        if (day != null) {
            cal.setTimeInMillis(day.getTime());
        }
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);
        return cal.getTime();
    }

    /**
     * 获取19位的格式时间
     *
     * @param dateStr
     * @return
     * @throws
     */
    public static Date getDateByFullDateStr(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取16位的格式时间 yyyy-MM-dd HH:mm
     *
     * @param dateStr
     * @return
     * @throws Exception
     */
    public static Date getDateByStr(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return sdf.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取yyyy-MM-dd的格式时间
     *
     * @param dateStr
     * @return
     * @throws
     */
    public static Date getDateByDateStr(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取两个日期相差的月数
     *
     * @param d1 较大的日期
     * @param d2 较小的日期
     * @return 如果d1>d2返回 月数差 否则返回0
     */
    public static int monthsBetween(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        if (c1.getTimeInMillis() < c2.getTimeInMillis()) {
            return 0;
        }
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH);
        int month2 = c2.get(Calendar.MONTH);
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        // 获取年的差值 假设 d1 = 2015-8-16  d2 = 2011-9-30
        int yearInterval = year1 - year2;
        // 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数
        if (month1 < month2 || month1 == month2 && day1 < day2) {
            yearInterval--;
        }
        // 获取月数差值
        int monthInterval = (month1 + 12) - month2;
        if (day1 < day2) {
            monthInterval--;
        }
        monthInterval %= 12;
        return yearInterval * 12 + monthInterval;
    }

    /**
     * 计算date2 - date1之间相差的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int daysBetween(Date date1, Date date2) {
        DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        try {
            Date d1 = sdf.parse(DateUtils.dateStr7(date1));
            Date d2 = sdf.parse(DateUtils.dateStr7(date2));
            cal.setTime(d1);
            long time1 = cal.getTimeInMillis();
            cal.setTime(d2);
            long time2 = cal.getTimeInMillis();
            return Integer.parseInt(String.valueOf((time2 - time1) / 86400000L));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 计算date2 - date1之间相差的分钟
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int minutesBetween(Date date1, Date date2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        if (time2 - time1 <= 0) {
            return 0;
        } else {
            return Integer.parseInt(String.valueOf((time2 - time1) / 60000L)) + 1;
        }

    }

    /**
     * 计算date2 - date1之间相差的秒
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int secondBetween(Date date1, Date date2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        if (time2 - time1 <= 0) {
            return 0;
        } else {
            return Integer.parseInt(String.valueOf((time2 - time1) / 1000L)) + 1;
        }
    }

    /**
     * 计算date2 - date1之间相差的毫秒
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int millisecondBetween(Date date1, Date date2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        if (time2 - time1 <= 0) {
            return 0;
        } else {
            return Integer.parseInt(String.valueOf((time2 - time1)));
        }
    }

    /**
     * 获取当月开始时间
     */
    public static Date getMonthStartTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar cal = Calendar.getInstance();
        Date nowdate = cal.getTime();
        String date1 = sdf.format(nowdate);
        date1 += "-01 00:00:00";
        try {
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf2.parse(date1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取当月开始时间Str
     */
    public static String getMonthStartTimeStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar cal = Calendar.getInstance();
        Date nowdate = cal.getTime();
        String date = sdf.format(nowdate);
        date += "-01 00:00:00";
        return date;
    }


    /**
     * 获取当月结束时间
     */

    public static Date getMonthEndTime() {
        Calendar calendar = Calendar.getInstance();
        // 设置日期为本月最大日期
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        // 打印
        DateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        calendar.getTime();
        String date1 = sdf1.format(calendar.getTime());
        date1 += " 23:59:59";
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf2.parse(date1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据当前日期计算今年剩余天数
     *
     * @return
     */
    public static int getLeftDaysOfCurrYear() {
        return daysBetween(new Date(), getCurrYearLast());
    }

    /**
     * 获取当年的最后一天
     *
     * @return
     */
    public static Date getCurrYearLast() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearLast(currentYear);
    }

    /**
     * 获取某年最后一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getYearLast(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();

        return currYearLast;
    }

    /**
     * 根据出生日期计算年龄
     *
     * @param birthday 出生日期
     * @return
     */
    public static int getAge(Date birthday) {
        int age = 0;
        Calendar born = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        if (birthday != null) {
            now.setTime(new Date());
            born.setTime(birthday);
            if (born.after(now)) {
                throw new IllegalArgumentException("年龄不能超过当前日期");
            }
            age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
            int nowDayOfYear = now.get(Calendar.DAY_OF_YEAR);
            int bornDayOfYear = born.get(Calendar.DAY_OF_YEAR);
            if (nowDayOfYear < bornDayOfYear) {
                age -= 1;
            }
        }
        return age;
    }


    /**
     * 判断当前时间是否是否 3:00过后  如果当前时间是交易时间段
     *
     * @return
     */
    public static boolean checkedTransactionTime() {
        Date da = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String now = sdf.format(da);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(sdf.parse(now));
            c2.setTime(sdf.parse("15:00:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if ((c1.compareTo(c2) > 0)) {
            return false;
        }
        return true;
    }


    /**
     * 判断当前时间是否是否 2:30前
     *
     * @return
     */
    public static boolean checkedTransactionTowTime() {
        Date da = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String now = sdf.format(da);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(sdf.parse(now));
            c2.setTime(sdf.parse("14:30:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if ((c1.compareTo(c2) > 0)) {
            return false;
        }
        return true;
    }

    /**
     * 日期转换为字符串 格式自定义
     *
     * @param date
     * @param f
     * @return
     */
    public static Date dateFormat(Date date, String f) {
        Date date1 = new Date();
        if (date == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(f);
        String str = format.format(date);
        try {
            date1 = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }


}
