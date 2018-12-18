/***********************************************************************
 * $Id: DateUtils.java,v 1.1 Jun 14, 2008 4:37:56 PM Victor Exp $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.common;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author Victor
 * @created @2011-9-26-下午03:02:31 comment by @bravin:
 *          SimpleDateFormat不是线程安全的，所有线程共有一个实例不安全，建议采用DateUtils.getDateFormat()得到实例，只支持 yyyy-MM-dd
 *          HH:mm:ss
 * 
 */
public class DateUtils {
    /**
     * 如果这里不设置为 1000L而是 1000的话，大数将装不下：by @Bravin
     */
    public static final long MINUTE_MILLS = 60 * 1000L;

    public static final long HOUR_MILLS = 60 * MINUTE_MILLS;

    public static final long DAY_MILLS = 24 * HOUR_MILLS;

    public static final long WEEK_MILLS = 7 * DAY_MILLS;

    public static final long WEEKDAY_MILLS = 5 * DAY_MILLS;

    public static final long MONTH_MILLS = 30 * DAY_MILLS;

    public static final long YEAR_MILLS = 365 * DAY_MILLS;

    public static final SimpleDateFormat FILENAME_FORMAT = new SimpleDateFormat("yyMMdd");

    public static final SimpleDateFormat DAY_MONTH_YEAR_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static final SimpleDateFormat FULL_FILENAME_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd");

    public static final SimpleDateFormat FULL_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static final SimpleDateFormat MINUTE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static final SimpleDateFormat FULL_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final SimpleDateFormat FULL_S_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

    public static final SimpleDateFormat FULL_Z_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S,Z");

    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

    public static final SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat("HH");

    private static final long GMT_1970 = 0;

    public static String buildFileNameByTime() {
        return FILENAME_FORMAT.format(new Date());
    }

    public static String format(Date d) {
        return d == null ? "" : FULL_FORMAT.format(d);
    }

    public static String format(long l) {
        return FULL_FORMAT.format(new Date(l));
    }

    public static String format(long l, SimpleDateFormat format) {
        return format.format(new Date(l));
    }

    public static String hourFormat(long l) {
        return HOUR_FORMAT.format(new Date(l));
    }

    public static String getCurrentTime() {
        return FULL_FORMAT.format(new Date());
    }

    // 1-59秒前，1-59分钟前，1-23小时前，1-6天前，1-4周前，1-11月前，1-N年前。
    public static String getTimeDesInObscure(long time, String lang) {
        ResourceManager rm = ResourceManager.getResourceManager("com.topvision.framework.resources", lang);
        long obscure = 0;
        String timeFlag;
        long length = time > 0 ? time : -time;
        if (length > YEAR_MILLS) {
            obscure = length / YEAR_MILLS;
            timeFlag = rm.getNotNullString("Date.YEAR");
        } else if (length > MONTH_MILLS) {
            obscure = length / MONTH_MILLS;
            timeFlag = rm.getNotNullString("Date.MONTH");
        } else if (length > WEEK_MILLS) {
            obscure = length / WEEK_MILLS;
            timeFlag = rm.getNotNullString("Date.WEEK");
        } else if (length > DAY_MILLS) {
            obscure = length / DAY_MILLS;
            timeFlag = rm.getNotNullString("Date.DAY");
        } else if (length > HOUR_MILLS) {
            obscure = length / HOUR_MILLS;
            timeFlag = rm.getNotNullString("Date.HOUR");
        } else if (length > MINUTE_MILLS) {
            obscure = length / MINUTE_MILLS;
            timeFlag = rm.getNotNullString("Date.MINUTE");
        } else {
            obscure = length / 1000;
            timeFlag = rm.getNotNullString("Date.SECOND");
        }
        StringBuilder sb = new StringBuilder();
        sb.append(rm.getNotNullString("Date.Obscure"));
        if (lang.equals("en_US")) {
            sb.append(" ");
        }
        sb.append(obscure);
        if (lang.equals("en_US")) {
            sb.append(" ");
        }
        sb.append(timeFlag);
        if (lang.equals("en_US")) {
            sb.append(" ");
        }
        if (time >= 0) {
            sb.append(rm.getNotNullString("Date.Ago"));
        } else {
            sb.append(rm.getNotNullString("Date.Later"));
        }
        return sb.toString();
    }

    // 1-59秒前，1-59分钟前，1-23小时前，1-6天前，1-4周前，1-11月前，1-N年前。
    public static String getTimeDesInObscure(long time) {
        long length = time > 0 ? time : -time;
        StringBuilder sb = new StringBuilder();
        sb.append("About ");
        if (length > YEAR_MILLS) {
            sb.append(length / YEAR_MILLS).append("Y");
        } else if (length > MONTH_MILLS) {
            sb.append(length / MONTH_MILLS).append("M");
        } else if (length > WEEK_MILLS) {
            sb.append(length / WEEK_MILLS).append("W");
        } else if (length > DAY_MILLS) {
            sb.append(length / DAY_MILLS).append("D");
        } else if (length > HOUR_MILLS) {
            sb.append(length / HOUR_MILLS).append("H");
        } else if (length > MINUTE_MILLS) {
            sb.append(length / MINUTE_MILLS).append("m");
        } else {
            sb.append(length / 1000).append("S");
        }
        if (time >= 0) {
            sb.append(" Ago");
        } else {
            sb.append(" Later");
        }
        return sb.toString();
    }

    /**
     * 计算设备启动时长等时间段的表示
     * 
     * @param time
     * @param lang
     * @return
     */
    public static String getTimePeriod(long time, String lang) {
        ResourceManager rm = ResourceManager.getResourceManager("com.topvision.framework.resources", lang);
        if (time == GMT_1970) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if (time > MONTH_MILLS) {
            sb.append(time / MONTH_MILLS);
            sb.append(" ");
            sb.append(rm.getNotNullString("Date.MONTH"));
            sb.append(" ");
            time = time % MONTH_MILLS;
        }
        if (time > DAY_MILLS) {
            sb.append(time / DAY_MILLS);
            sb.append(" ");
            sb.append(rm.getNotNullString("Date.DAY"));
            sb.append(" ");
            time = time % DAY_MILLS;
        }
        if (time > HOUR_MILLS) {
            sb.append(time / HOUR_MILLS);
            sb.append(" ");
            sb.append(rm.getNotNullString("Date.HOUR"));
            sb.append(" ");
            time = time % HOUR_MILLS;
        }
        if (time > MINUTE_MILLS) {
            sb.append(time / MINUTE_MILLS);
            sb.append(" ");
            sb.append(rm.getNotNullString("Date.MINUTE"));
            sb.append(" ");
            time = time % MINUTE_MILLS;
        }
        return sb.toString();
    }

    /**
     * 计算设备启动时长等时间段的表示
     * 
     * @param time
     * @param lang
     * @return
     */
    public static String getTimePeriod(long time) {
        if (time == GMT_1970) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if (time > MONTH_MILLS) {
            sb.append(time / MONTH_MILLS);
            sb.append("M");
            time = time % MONTH_MILLS;
        }
        if (time > DAY_MILLS) {
            sb.append(time / DAY_MILLS);
            sb.append("D");
            time = time % DAY_MILLS;
        }
        if (time > HOUR_MILLS) {
            sb.append(time / HOUR_MILLS);
            sb.append(":");
            time = time % HOUR_MILLS;
        } else {
            sb.append("0:");
        }

        if (time > MINUTE_MILLS) {
            sb.append(time / MINUTE_MILLS);
            sb.append(":");
            time = time % MINUTE_MILLS;
        } else {
            sb.append("0:");
        }
        if (time > 1000) {
            sb.append(time / 1000);
            sb.append(".");
            time = time % 1000;
        } else {
            sb.append("0.");
        }
        sb.append(time);
        sb.append("S");
        return sb.toString();
    }

    /**
     * 适用ThreadLocal来为每个线程创建一个实例，以保证线程安全 备注：SimpleDateFormat不是线程安全的
     */
    private static ThreadLocal<?> threadLocal = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected synchronized SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    /**
     * 得到 SimpleDateFormat 实例
     * 
     * @return
     */
    public static DateFormat getDateFormat() {
        return (DateFormat) threadLocal.get();
    }

    /**
     * 解析一个日期字符串为日期
     * 
     * @param text
     * @return
     * @throws ParseException
     */
    public static Date parse(String text) throws ParseException {
        return getDateFormat().parse(text);
    }

    /**
     * 解析一个日期字符串为长正整形
     * 
     * @param text
     * @return
     * @throws ParseException
     */
    public static Long parseLong(String text) throws ParseException {
        return parse(text).getTime();
    }

    /**
     * 解析一个日期字符串为数据库适用的Timestamp类型
     * 
     * @param text
     * @return
     * @throws ParseException
     */
    public static Timestamp parseToTimestamp(String text) throws ParseException {
        return Timestamp.valueOf(text);
        // return new Timestamp(parseLong(text));
    }

    /**
     * 提供通用方法，方便统一管理，也方便以后版本升级后更改为不同的实现
     * 
     * @param date
     * @return
     * @throws ParseException
     */
    public static Timestamp parseToTimestamp(Date date) throws ParseException {
        return new Timestamp(date.getTime());
    }

    /**
     * java.sql.Timestamp的toString方法会留下 ms级别的小数位，所以使用这个
     * 
     * @param timestamp
     * @return
     * @throws ParseException
     */
    public static String parseToString(Timestamp timestamp) throws ParseException {
        return getDateFormat().format(timestamp);
    }

    /**
     * 得到当前的Timestamp
     * 
     * @return
     * @throws ParseException
     */
    public static Timestamp getCurrentTimestamp() throws ParseException {
        return parseToTimestamp(new Date());
    }
}
