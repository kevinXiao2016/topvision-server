/***********************************************************************
 * $Id: Validator.java,v1.0 2014年1月4日 下午5:20:55 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.zetaframework.util;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Bravin
 * @created @2014年1月4日-下午5:20:55
 *
 */
public class Validator {
    private static final String IP_REG = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
    //匹配以冒号、横线和空格隔开的形式
    private static final String MAC_REG_1 = "^([A-Fa-f\\d]{2}[\\s:-]){5}[A-Fa-f\\d]{2}$";
    //匹配0000.0000.0000形式
    private static final String MAC_REG_2 = "^([A-Fa-f\\d]{4}\\.){2}[A-Fa-f\\d]{4}$";
    //匹配0000-0000-0000形式
    private static final String MAC_REG_3 = "^([A-Fa-f\\d]{4}-){2}[A-Fa-f\\d]{4}$";
    //匹配000000000000形式
    private static final String MAC_REG_4 = "^[A-Fa-f\\d]{12}$";
    //MAC地址最小单位(两位)
    private static final String MAC_REG_5 = "[A-Fa-f\\d]{2}";

    public static boolean isTrue(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof java.lang.String) {
            String $ = (String) obj;
            if ("".equals($)) {
                return false;
            }
        }
        if (obj instanceof java.lang.Boolean) {
            Boolean $ = (Boolean) obj;
            if ($ != true) {
                return false;
            }
        }
        if (obj instanceof Collection) {
            Collection<Object> $ = (Collection) obj;
            if ($.size() == 0) {
                return false;
            }
        }

        if (obj instanceof java.lang.Integer) {
            if (obj.equals(1)) {
                return true;
            } else {
                return false;
            }
        }

        return true;
    }

    /**
     * 判断一个字符串是否是IP地址
     * @param ipAddress
     * @return
     */
    public static boolean isIpV4(String ipAddress) {
        Pattern pattern = Pattern.compile(IP_REG);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }

    /** 
     *  判断一个字符串是否是MAC地址
     * @param macAddress
     * @return
     */
    public static boolean isMac(String macAddress) {
        if (macAddress.matches(MAC_REG_1) || macAddress.matches(MAC_REG_2) || macAddress.matches(MAC_REG_3)
                || macAddress.matches(MAC_REG_5) || macAddress.matches(MAC_REG_4)) {
            return true;
        }
        return false;
    }

    public static boolean isMulticast(String ip) {
        String[] ips = ip.split(".");
        Integer value = Integer.parseInt(ips[0], 10);
        if (value >= 224 && value <= 239) {
            return true;
        }
        return false;
    }
}
