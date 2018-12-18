/***********************************************************************
 * $Id: IPAddressUtils.java,v1.0 2013-4-2 上午10:11:15 $
 * 
 * @author: lzs
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.util;

/**
 * @author lzs
 * @created @2013-4-2-上午10:11:15
 * 
 */
public class IPAddressUtils {

    /**
     * 全零的IP地址，IPv4
     */
    public static final String IP_ADDR_ALL_ZERO = "0.0.0.0";
    /**
     * 本地回环地址，IPv4
     */
    public static final String IP_ADDR_LOOPBACK = "127.0.0.1";

    /**
     * 本地主机名称 localhost
     */
    public static final String HOST_NAME_LOCALHOST = "localhost";

    /**
     * 判断IP地址是否是全零地址 IPv4
     * 
     * @param ipAddress
     * @return
     */
    public static boolean isAllZeroAddress(String ipAddress) {
        return IP_ADDR_ALL_ZERO.equals(ipAddress);
    }

    /**
     * 判断是否是本地回环地址 IPv4
     * 
     * @param ipAddress
     * @return
     */
    public static boolean isLoopbackAddress(String ipAddress) {
        return IP_ADDR_LOOPBACK.equals(ipAddress);
    }

    /**
     * 判断是否是localhostName
     * 
     * @param hostName
     * @return
     */
    public static boolean isLocalhostName(String hostName) {
        return HOST_NAME_LOCALHOST.equalsIgnoreCase(hostName);
    }

    /**
     * 判断一个字符串是否是本地地址，localhost或者127.0.0.1
     * 
     * @param ipOrName
     * @return
     */
    public static boolean isLocalhost(String ipOrName) {
        return isLoopbackAddress(ipOrName) || isLocalhostName(ipOrName);
    }

    /**
     * 判断一个字符串是否为IP地址
     * 
     * @param ip
     * @return
     */
    public static boolean isIPAddress(String ip) {
        if (ip != null) {
            ip = ip.trim();
            if (ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
                String s[] = ip.split("\\.");
                if ((Integer.parseInt(s[0]) < 255) && (Integer.parseInt(s[1]) < 256) && (Integer.parseInt(s[2]) < 256)
                        && (Integer.parseInt(s[3]) < 255)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断当前操作是否Windows.
     * 
     * @return true---是Windows操作系统
     */
    public static boolean isWindowsOS() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }

    public static void main(String args[]) {
        System.out.println(IPAddressUtils.isIPAddress("10.194.255.94"));
    }

}
