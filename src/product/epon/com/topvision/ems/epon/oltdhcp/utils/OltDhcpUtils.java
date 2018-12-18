/***********************************************************************
 * $Id: OltDhcpUtils.java,v1.0 2017年11月27日 下午1:29:04 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.utils;

/**
 * @author haojie
 * @created @2017年11月27日-下午1:29:04
 *
 */
public class OltDhcpUtils {

    /**
     * RELAY规则的OPTION60字符串转化为mib可用的点分字符串
     * 
     * @param opt60
     * @return
     */
    public static String convertOpt60StrToMibStr(String opt60Str) {
        Integer length = opt60Str.length();
        char[] chars = opt60Str.toCharArray();
        StringBuffer hex = new StringBuffer();
        //hex.append(Integer.toHexString(length));
        hex.append(length);
        for (int i = 0; i < chars.length; i++) {
            hex.append(".").append((int) chars[i]);
        }
        return hex.toString();
    }

    /**
     * OPTION60的mib点分字符串转换为可见字符串
     * 
     * @param ascii
     * @return
     */
    public static String convertMibStrToOpt60Str(String mibStr) {
        StringBuilder sb = new StringBuilder();
        String[] opt60 = mibStr.split("\\.");
        if (opt60.length == 1 && opt60[0].equals("0")) {
            sb.append("");
        }
        for (int i = 1; i < opt60.length; i++) {
            int decimal = Integer.parseInt(opt60[i]);
            sb.append((char) decimal);
        }
        return sb.toString();
    }

    /**
     * 服务器组IP MIB字符串转化为服务器组列表
     * 
     * @param groupStr
     * @return
     */
    public static String convertMibStrToGroupStr(String mibStr) {
        StringBuffer groupStr = new StringBuffer();
        String[] temp = mibStr.split(":");
        for (int i = 0; i < temp.length; i++) {
            Integer f = Integer.parseInt(temp[i], 16);
            if (i != 0 && i % 4 == 0) {
                groupStr.append(",");
            } else if (i != 0 && i % 4 != 0) {
                groupStr.append(".");
            }
            groupStr.append(f);
        }
        return groupStr.toString();
    }

    /**
     * 服务器组列表转化为mib可用的服务器组字符串
     * 
     * @param groupList
     * @return
     */
    public static String convertGroupStrToMibStr(String groupStr) {
        StringBuffer mibStr = new StringBuffer();
        String[] group = groupStr.split(",");
        for (int i = 0; i < group.length; i++) {
            String serverIp = group[i];
            String[] ips = serverIp.split("\\.");
            for (int j = 0; j < ips.length; j++) {
                String hexStr = Integer.toHexString(Integer.parseInt(ips[j])).toUpperCase();
                if (mibStr.length() != 0) {
                    mibStr.append(":");
                }
                if (hexStr.length() < 2) {
                    mibStr.append("0" + hexStr);
                } else {
                    mibStr.append(hexStr);
                }
            }
        }
        return mibStr.toString();
    }

    public static void main(String[] args) {
        // List<String> ips = new ArrayList<String>();
        String groupStr = "01.01.01.01.02.02.02.02.AC.17.24.20";
        // System.out.println(IpUtils.transferHexIp("04:38:3F:02"));
        System.out.println(convertMibStrToGroupStr(groupStr));

    }

}
