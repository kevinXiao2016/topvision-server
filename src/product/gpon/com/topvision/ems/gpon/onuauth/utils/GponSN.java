/***********************************************************************
 * $Id: GponSnFormat.java,v1.0 2017年1月3日 下午4:28:17 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onuauth.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Bravin
 * @created @2017年1月3日-下午4:28:17
 *
 */
public class GponSN {

    public static String format(String sn) {
        sn = sn.replaceAll(":", "");
        Pattern pattern1 = Pattern.compile("([A-Fa-f\\d]{2})");
        Matcher matcher1 = pattern1.matcher(sn);
        String sb = "";
        while (matcher1.find()) {
            sb += matcher1.group() + ":";
        }
        // 除去最后一个：
        sb = sb.substring(0, sb.length() - 1);
        return sb.toUpperCase();
    }

}
