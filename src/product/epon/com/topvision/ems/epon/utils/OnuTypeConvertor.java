/***********************************************************************
 * $Id: OnuTypeConvertor.java,v1.0 2015年6月17日 上午9:23:18 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.utils;

/**
 * @author Bravin
 * @created @2015年6月17日-上午9:23:18
 *
 */
public class OnuTypeConvertor {

    /**
     * typeid大于255的按照ASCII码的方式解析出来
     * @param type
     * @return
     */
    public static String convertTypeName(Integer type) {
        String s = Integer.toHexString(type);
        int l = s.length();
        if (l % 2 != 0) {
            return s;
        }
        StringBuilder sb = new StringBuilder();
        for (int cur = 0; cur * 2 < l; cur++) {
            String $sub = s.substring(cur * 2, (cur + 1) * 2);
            int $value = Integer.parseInt($sub, 16);
            sb.append((char) $value);
        }
        return sb.toString();
    }


}
