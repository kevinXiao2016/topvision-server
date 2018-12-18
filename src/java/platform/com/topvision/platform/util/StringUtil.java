package com.topvision.platform.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.topvision.platform.ResourceManager;

public class StringUtil {
    private static final String STRIP_NAME = "[^a-zA-Z\\d\u4e00-\u9fa5-_\\[\\]()\\/\\.:]";

    public static Boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static String stripSpecialChar(String name) {
        return name.replaceAll(STRIP_NAME, "");
    }

    /**
     * 
     * 
     * @param time
     * @return
     */
    public static StringBuilder getTimeString(Long time) {
        int day = time.intValue() / (24 * 3600);
        int hour = (time.intValue() % (24 * 3600)) / 3600;
        int minute = (time.intValue() % (24 * 3600) % 3600) / 60;
        int second = (time.intValue() % (24 * 3600) % 3600) % 60;
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(day));
        sb.append(ResourceManager.getResourceManager("com.topvision.platform.resources").getNotNullString("label.day"));
        sb.append(hour);
        sb.append(ResourceManager.getResourceManager("com.topvision.platform.resources").getNotNullString("label.h"));
        sb.append(minute);
        sb.append(ResourceManager.getResourceManager("com.topvision.platform.resources").getNotNullString("label.m"));
        sb.append(second);
        sb.append(ResourceManager.getResourceManager("com.topvision.platform.resources").getNotNullString("label.s"));
        return sb;
    }

    /**
     * 时间格式化 1. 年不同时 ===> yyyy/MM/dd HH:mm 2. 月不同时 ===> MM月dd日HH时mm分 3. 日不同时 ===> dd日HH时mm分 4. 小时不同时
     * ===> HH时mm分
     * 
     * @param startTime
     * @param endTime
     * @return
     */
    public static SimpleDateFormat getTimeString(long startTime, long endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        Calendar sc = Calendar.getInstance();
        Calendar ec = Calendar.getInstance();
        sc.setTimeInMillis(startTime);
        ec.setTimeInMillis(endTime);
        if (sc.get(1) != ec.get(1))
            sdf.applyPattern(ResourceManager.getResourceManager("com.topvision.platform.resources").getNotNullString(
                    "label.ymdhm"));
        else if (sc.get(2) != ec.get(2))
            sdf.applyPattern(ResourceManager.getResourceManager("com.topvision.platform.resources").getNotNullString(
                    "label.mdhm"));
        else if (sc.get(5) != ec.get(5))
            sdf.applyPattern(ResourceManager.getResourceManager("com.topvision.platform.resources").getNotNullString(
                    "label.dhm"));
        else
            sdf.applyPattern(ResourceManager.getResourceManager("com.topvision.platform.resources").getNotNullString(
                    "label.hm"));
        return sdf;
    }

    /**
     * 将时间格式化为："yyyy-MM-dd HH:mm:ss"
     * 
     * @param o
     * @return
     */
    public static String getTimeString(Object o) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(o);
    }

    /**
     * 获得时间点
     * 
     * @param o
     * @return
     */
    public static String getTimeSubDayString(Object o) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dayStr = sdf.format(o).substring(8, 10)
                + ResourceManager.getResourceManager("com.topvision.platform.resources").getNotNullString("label.d");
        String dayh = sdf.format(o).substring(11, 13)
                + ResourceManager.getResourceManager("com.topvision.platform.resources").getNotNullString("label.h");
        String subTime = dayStr + dayh;
        return subTime;
    }

    /**
     * 获得时间点
     * 
     * @param o
     * @return
     */
    public static String getTimeSubString(Object o) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String subTime = sdf.format(o).substring(11, 16);
        return subTime;
    }

    /**
     * 与javascript一致的format.还不可用
     * @param template
     * @param replace
     * @return
     */
    public static String format(String template, Object... replace) {
        String string = template;
        for (int i = 0; i < replace.length; i++) {
            if (replace[i] != null) {
                String replaceSequence = replace[i].toString().replace("\\", "\\\\");
                string = string.replaceAll("\\{" + i + "\\}", replaceSequence);
            }
        }
        return string;
    }

}
