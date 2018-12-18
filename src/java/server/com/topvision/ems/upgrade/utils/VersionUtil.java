/***********************************************************************
 * $ VersionUtil.java,v1.0 2013-3-19 19:56:44 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.topvision.ems.upgrade.exception.WrongVersionStringException;


/**
 * @author jay
 * @created @2013-3-19-19:56:44
 */
public class VersionUtil {
    public static int getVersionLong(String version) {
        Pattern p = Pattern.compile("\\d*\\.\\d*\\.\\d*\\.\\d*");
        Matcher m = p.matcher(version);
        boolean b = m.find();
        String v;
        if (!b) {
            Pattern p2 = Pattern.compile("\\d*\\.\\d*\\.\\d*");
            Matcher m2 = p2.matcher(version);
            boolean b2 = m2.find();
            if (!b2) {
                throw new WrongVersionStringException(version);
            } else {
                v = m2.group() + ".0";
            }
        } else {
            v = m.group();
        }
        int l = 0;
        String[] strs = v.split("\\.");
        for (int i = 0; i < strs.length; i++) {
            l = l + Integer.parseInt(strs[i]) * (int) Math.pow(1000, (strs.length - i - 1));
        }
        return l;
    }

    public static String getVersionString(String version) {
        Pattern p = Pattern.compile("V\\d*\\.\\d*\\.\\d*\\.\\d*");
        Matcher m = p.matcher(version);
        boolean b = m.find();
        String v;
        if (!b) {
            Pattern p2 = Pattern.compile("V\\d*\\.\\d*\\.\\d*");
            Matcher m2 = p2.matcher(version);
            boolean b2 = m2.find();
            if (!b2) {
                throw new WrongVersionStringException(version);
            } else {
                v = m2.group() + ".0";
            }
        } else {
            v = m.group();
        }
        String versionAdditional = getVersionAdditional(version);
        if (versionAdditional == null) {
            versionAdditional = "";
        }
        return v + versionAdditional; // To change body of created methods use File | Settings |
                                      // File Templates.
    }

    public static String getVersionAdditional(String version) {
        Pattern p = Pattern.compile("\\d*\\.\\d*\\.\\d*\\.\\d*-.*");
        Matcher m = p.matcher(version);
        boolean b = m.find();
        String v;
        if (!b) {
            Pattern p2 = Pattern.compile("\\d*\\.\\d*\\.\\d*-.*");
            Matcher m2 = p2.matcher(version);
            boolean b2 = m2.find();
            if (!b2) {
                return "";
            } else {
                v = m2.group();
            }
        } else {
            v = m.group();
        }
        Pattern p2 = Pattern.compile("-.*");
        Matcher m2 = p2.matcher(v);
        boolean b2 = m2.find();
        if (!b2) {
            return "";
        }
        return m2.group();
    }

    public static String getVersionString(Integer version) {
        int subversion1 = version % 1000;
        version = version / 1000;
        int subversion2 = version % 1000;
        version = version / 1000;
        int subversion3 = version % 1000;
        int subversion4 = version / 1000;
        return "V" + subversion4 + "." + subversion3 + "." + subversion2 + "." + subversion1;
    }

    public static void main(String[] args) {
        String version =  "V1.3.7.21";
        String deviceVersion =  "V1.3.7.21;";
        long upgradeVersionLong = VersionUtil.getVersionLong(version);
        long deviceVersionLong = VersionUtil.getVersionLong(deviceVersion);
        String upgradeVersionAdditional = VersionUtil.getVersionAdditional(version);
        String deviceVersionAdditional = VersionUtil.getVersionAdditional(deviceVersion);

        System.out.println("upgradeVersionLong = " + upgradeVersionLong);
        System.out.println("upgradeVersionAdditional = " + upgradeVersionAdditional);
        System.out.println("deviceVersionLong = " + deviceVersionLong);
        System.out.println("deviceVersionAdditional = " + deviceVersionAdditional);
        if (upgradeVersionLong != deviceVersionLong || !upgradeVersionAdditional.equals(deviceVersionAdditional)) {
            System.out.println("com.topvision.ems.upgrade.utils.VersionUtil.main 1");
        } else {
            System.out.println("com.topvision.ems.upgrade.utils.VersionUtil.main 2");
        }
    }
}