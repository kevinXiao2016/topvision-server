/***********************************************************************
 * $Id: Telnet.java,v 1.1 2009-11-21 上午12:04:55 kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.telnet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.epon.utils.EponUtil;

/**
 * @Create Date 2009-11-21 上午12:04:55
 * 
 * @author kelers
 * 
 */
public class Telnet {
    protected static final Logger logger = LoggerFactory.getLogger(Telnet.class);
    public static final SimpleDateFormat FULL_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public static void main(String[] args) {
        TelnetParam param = new TelnetParam();
        param.setIpAddress("172.17.2.152");
        //param.setIpAddress("119.34.136.2");//V1.6.9.9
        //param.setIpAddress("119.32.50.2 ");//V1.6.9.5-P5 
        param.setPassword("topvision");
        param.setEnablePasswd("topvision");
        param.setSuperPasswd(String.valueOf(EponUtil.pwdGetByDate()));
        System.out.println("Super password>>>>>>" + param.getSuperPasswd());
        try {
            TelnetVty telnet = new TelnetVty();
            telnet.connect(param.getIpAddress(), 23);
            String r = telnet.sendLine(param.getPassword());
            System.out.println(r);
            if (r.trim().endsWith("assword:")) {
                r = telnet.sendLine(param.getSuperPasswd());
                System.out.println(r);
            }
            r = telnet.sendLine("enable");
            System.out.println(r);
            r = telnet.sendLine(param.getEnablePasswd());
            System.out.println(r);
            if (r.trim().endsWith("assword:")) {
                r = telnet.sendLine(param.getSuperPasswd());
                System.out.println(r);
            }
            r = telnet.sendLine("super");
            System.out.println(r);
            r = telnet.sendLine(param.getSuperPasswd());
            System.out.println(r);
            r = telnet.sendLine("test");
            System.out.println(r);
            //String time = telnet.sendLine("show ccmts 00:24:68:50:02:79 system-time");
            String time = telnet.sendLine("show ccmts 0024.6850.025c system-time");
            System.out.println("=====================");
            System.out.println(getTime(time));
            System.out.println("=====================");
            telnet.disconnect();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private static String getTime(String sTime) {
        try {
            int index1 = sTime.indexOf("date:");
            int index2 = sTime.indexOf("time:");
            if (index1 == -1 || index2 == -1) {
                return null;
            }
            System.out.println(sTime.substring(index1 + 6, index1 + 16) + " "
                    + sTime.substring(index2 + 6, index2 + 14));
            Calendar c = Calendar.getInstance();
            c.setTime(FULL_FORMAT.parse(sTime.substring(index1 + 6, index1 + 16) + " "
                    + sTime.substring(index2 + 6, index2 + 14)));
            return String.valueOf(c.getTimeInMillis() + (c.get(Calendar.ZONE_OFFSET) + c.get(Calendar.DST_OFFSET)));
        } catch (ParseException e) {
            logger.debug("", e);
        }
        return null;
    }
}
