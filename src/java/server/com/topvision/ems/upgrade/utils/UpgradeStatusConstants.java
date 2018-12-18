/***********************************************************************
 * $Id: UpgradeStatusConstants.java,v1.0 2014年11月4日 上午11:55:53 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.utils;

import com.topvision.ems.upgrade.annotation.UpgradeStatusProperty;

/**
 * @author loyal
 * @created @2014年11月4日-上午11:55:53
 * 
 */
public class UpgradeStatusConstants {
    public final static String RESULT_STATUS_TYPE = "Result_Status";
    public final static String ERROR_STATUS_TYPE = "Error_Status";
    public final static String NOW_STATUS_TYPE = "NOW_STATUS_TYPE";
    @UpgradeStatusProperty(id = "0", displayName = "UPGRADE_NOSTART", statusType = RESULT_STATUS_TYPE)
    public static Integer UPGRADE_NOSTART = 0;
    @UpgradeStatusProperty(id = "1", displayName = "UPGRADE_SUCCESS", statusType = RESULT_STATUS_TYPE)
    public static Integer UPGRADE_SUCCESS = 1;
    @UpgradeStatusProperty(id = "2", displayName = "UPGRADE_FAIL", statusType = RESULT_STATUS_TYPE)
    public static Integer UPGRADE_FAIL = 2;
    @UpgradeStatusProperty(id = "3", displayName = "UPGRADE_NOW", statusType = RESULT_STATUS_TYPE)
    public static Integer UPGRADE_NOW = 3;

    @UpgradeStatusProperty(id = "101", displayName = "CHECKPING_NOW", statusType = NOW_STATUS_TYPE)
    public static Integer CHECKPING_NOW = 101;
    @UpgradeStatusProperty(id = "102", displayName = "CHECKSNMP_NOW", statusType = NOW_STATUS_TYPE)
    public static Integer CHECKSNMP_NOW = 102;
    @UpgradeStatusProperty(id = "103", displayName = "TELNET_NOW", statusType = NOW_STATUS_TYPE)
    public static Integer TELNET_NOW = 103;
    @UpgradeStatusProperty(id = "104", displayName = "CHECKVERSION_NOW", statusType = NOW_STATUS_TYPE)
    public static Integer CHECKVERSION_NOW = 104;
    @UpgradeStatusProperty(id = "105", displayName = "DOWNLOADFILE_NOW", statusType = NOW_STATUS_TYPE)
    public static Integer DOWNLOADFILE_NOW = 105;
    @UpgradeStatusProperty(id = "106", displayName = "CHECKONLINE_NOW", statusType = NOW_STATUS_TYPE)
    public static Integer CHECKONLINE_NOW = 106;
    @UpgradeStatusProperty(id = "107", displayName = "CHECKFTP_NOW", statusType = NOW_STATUS_TYPE)
    public static Integer CHECKFTP_NOW = 107;
    @UpgradeStatusProperty(id = "108", displayName = "CHECHTFTP_NOW", statusType = NOW_STATUS_TYPE)
    public static Integer CHECHTFTP_NOW = 108;
    @UpgradeStatusProperty(id = "109", displayName = "CHECKUBOOT_NOW", statusType = NOW_STATUS_TYPE)
    public static Integer CHECKUBOOT_NOW = 109;
    @UpgradeStatusProperty(id = "110", displayName = "LOADIMAGE_NOW", statusType = NOW_STATUS_TYPE)
    public static Integer LOADIMAGE_NOW = 110;
    @UpgradeStatusProperty(id = "111", displayName = "WIRTECONFIG_NOW", statusType = NOW_STATUS_TYPE)
    public static Integer WIRTECONFIG_NOW = 111;
    @UpgradeStatusProperty(id = "112", displayName = "RESET_NOW", statusType = NOW_STATUS_TYPE)
    public static Integer RESET_NOW = 112;

    @UpgradeStatusProperty(id = "201", displayName = "CHECKPING_ERROR", statusType = ERROR_STATUS_TYPE)
    public static Integer CHECKPING_ERROR = 201;
    @UpgradeStatusProperty(id = "202", displayName = "CHECKSNMP_ERROR", statusType = ERROR_STATUS_TYPE)
    public static Integer CHECKSNMP_ERROR = 202;
    @UpgradeStatusProperty(id = "203", displayName = "TELNET_ERROR", statusType = ERROR_STATUS_TYPE)
    public static Integer TELNET_ERROR = 203;
    @UpgradeStatusProperty(id = "204", displayName = "CHECKVERSION_ERROR", statusType = ERROR_STATUS_TYPE)
    public static Integer CHECKVERSION_ERROR = 204;
    @UpgradeStatusProperty(id = "205", displayName = "DOWNLOADFILE_ERROR", statusType = ERROR_STATUS_TYPE)
    public static Integer DOWNLOADFILE_ERROR = 205;
    @UpgradeStatusProperty(id = "206", displayName = "CHECKONLINE_ERROR", statusType = ERROR_STATUS_TYPE)
    public static Integer CHECKONLINE_ERROR = 206;
    @UpgradeStatusProperty(id = "207", displayName = "CHECKFTP_ERROR", statusType = ERROR_STATUS_TYPE)
    public static Integer CHECKFTP_ERROR = 207;
    @UpgradeStatusProperty(id = "208", displayName = "CHECHTFTP_ERROR", statusType = ERROR_STATUS_TYPE)
    public static Integer CHECHTFTP_ERROR = 208;
    @UpgradeStatusProperty(id = "209", displayName = "CHECKUBOOT_ERROR", statusType = ERROR_STATUS_TYPE)
    public static Integer CHECKUBOOT_ERROR = 209;
    @UpgradeStatusProperty(id = "210", displayName = "LOADIMAGE_ERROR", statusType = ERROR_STATUS_TYPE)
    public static Integer LOADIMAGE_ERROR = 210;
    @UpgradeStatusProperty(id = "211", displayName = "WIRTECONFIG_ERROR", statusType = ERROR_STATUS_TYPE)
    public static Integer WIRTECONFIG_ERROR = 211;
    @UpgradeStatusProperty(id = "212", displayName = "RESET_ERROR", statusType = ERROR_STATUS_TYPE)
    public static Integer RESET_ERROR = 212;

    public static boolean isUpgradeNow(Integer status) {
        return (status > 100 && status < 200) || status == 3;
    }

    public static boolean isUpgradeSuccess(Integer upgradeStatus) {
        return upgradeStatus == UPGRADE_SUCCESS;
    }
}
