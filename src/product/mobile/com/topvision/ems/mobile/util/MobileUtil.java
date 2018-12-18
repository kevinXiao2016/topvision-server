/***********************************************************************
 * $Id: MobileUtil.java,v1.0 2016年7月23日 下午2:19:33 $
 * 
 * @author: lzt
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.mobile.util;

/**
 * @author lzt
 * @created @2016年7月23日-下午2:19:33
 *
 */
public class MobileUtil {
    public static String convert2Bit(String text) {
        Integer value = Integer.parseInt(text);
        String v = Integer.toHexString(value).toUpperCase();
        Integer length = v.length();
        for (int i = 0; i < 4 - length; i++) {
            v = '0' + v;
        }
        return "0X" + v;
    }

    public static String convertSysUpTime(Long sysUpTime) {
        if (sysUpTime == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        long hour = sysUpTime / 3600;
        long min = sysUpTime / 60 - hour * 60;
        long sec = sysUpTime % 60;
        sb.append(hour);
        sb.append("h");
        sb.append(min);
        sb.append("m");
        sb.append(sec);
        sb.append("s");
        return sb.toString();
    }

    public static String convertQueryContext(String queryContext) {
        return queryContext.replaceAll("[:\\-\\_]", "");
    }

}
