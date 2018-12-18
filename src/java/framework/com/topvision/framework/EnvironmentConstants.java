/***********************************************************************
 * $Id: EnvironmentConstants.java,v1.0 2011-4-1 上午10:38:31 Victor Exp $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2010-2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 
 * @author Victor
 * 
 */
public class EnvironmentConstants {
    public static String START_TIME = "nm3000.start.time";
    public static String ENGINE_HOME = "nm3000.engine.home";
    public static String DRIVER_HOME = "nm3000.driver.home";
    public static String MIB_HOME = "nm3000.mibs.home";
    public static String DLL_HOME = "nm3000.dll.home";
    public static String ENGINE_PORT = "nm3000.engine.port";
    public static String HOSTNAME = "java.rmi.server.hostname";

    public static String getEnv(String key) {
        return System.getProperty(key, "");
    }

    public static String getEnv(String key, String defaultValue) {
        return System.getProperty(key, defaultValue);
    }

    public static void putEnv(String key, String value) {
        System.setProperty(key, value);
    }

    public static void setEnv(String key, String value) {
        System.setProperty(key, value);
    }

    public static String showEnvs() {
        return System.getProperties().toString();
    }

    /**
     * @return 获取本机一个可用IP（对于多IP服务器可能存在问题，解决办法就是配置文件中配置java.rmi.server.hostname）
     */
    public static String getHostAddress() {
        try {
            String hostname = System.getProperty("java.rmi.server.hostname");
            if (hostname != null && hostname.trim().length() > 0) {
                return hostname;
            }
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (nis.hasMoreElements()) {
                NetworkInterface netInterface = nis.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address && !ip.isLoopbackAddress()) {
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * @return 获取本机所有IP
     */
    public static List<String> getAllAddress() {
        List<String> ips = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (nis.hasMoreElements()) {
                NetworkInterface netInterface = nis.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address && !ip.isLoopbackAddress()) {
                        ips.add(ip.getHostAddress());
                    }
                }
            }
        } catch (Exception e) {
        }
        return ips;
    }
}
