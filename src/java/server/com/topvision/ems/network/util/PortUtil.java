/***********************************************************************
 * $Id: PortUtil.java,v 1.1 2009-10-5 下午01:00:54 kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.util;

import java.text.NumberFormat;

/**
 * @Create Date 2009-10-5 下午01:00:54
 * 
 * @author kelers
 * 
 */
public class PortUtil{
	private static NumberFormat format = NumberFormat.getNumberInstance();
    static {
        format.setMaximumFractionDigits(3);
    }

    /**
     * @return the ifAdminStatus
     */
    public static String getIfAdminStatusString(byte ifAdminStatus) {
        switch (ifAdminStatus) {
        case 1:
            return "Up";
        case 2:
            return "Down";
        case 3:
            return "Testing";
        default:
            return String.valueOf(ifAdminStatus);
        }
    }

    /**
     * 
     * @param ifInOctetsRate
     * @return
     */
    public static String getIfOctetsRateString(double ifOctetsRate) {
        if (ifOctetsRate < 1000) {
            return format.format(ifOctetsRate) + "  bps";
        } else if (ifOctetsRate < 1000 * 1000) {
            return format.format(ifOctetsRate / 1000.0) + " Kbps";
        } else if (ifOctetsRate < 1000 * 1000 * 1000) {
            return format.format(ifOctetsRate / 1000.0 / 1000.0) + " Mbps";
        } else {
            return format.format(ifOctetsRate / 1000.0 / 1000.0 / 1000.0) + " Gbps";
        }
    }

    /**
     * 
     * @param ifInOctetsRate
     * @return
     */
    public static String getIfRateString(double ifOctetsRate) {
        if (ifOctetsRate < 1000) {
            return format.format(ifOctetsRate) + "  bps";
        } else if (ifOctetsRate < 1000 * 1000) {
            return format.format(ifOctetsRate / 1024.0) + " Kbps";
        } else if (ifOctetsRate < 1000 * 1000 * 1000) {
            return format.format(ifOctetsRate / 1000.0 / 1024.0) + " Mbps";
        } else {
            return format.format(ifOctetsRate / 1000.0 / 1024.0 / 1000.0) + " Gbps";
        }
    }

    /**
     * @return the ifInOctets
     */
    public static String getIfOctetsString(double ifOctets) {
        if (ifOctets < 1024) {
            return format.format(ifOctets) + " Byte";
        } else if (ifOctets < 1024 * 1024) {
            return format.format(ifOctets / 1024.0) + " KByte";
        } else if (ifOctets < 1024 * 1024 * 1024) {
            return format.format(ifOctets / 1024.0 / 1024.0) + " MByte";
        } else {
            return format.format(ifOctets / 1024.0 / 1024.0 / 1024.0) + " GByte";
        }
    }

    /**
     * @return the ifOperStatus
     */
    public static String getIfOperStatusString(byte ifOperStatus) {
        switch (ifOperStatus) {
        case 1:
            return "Up";
        case 2:
            return "Down";
        case 3:
            return "Testing";
        case 4:
            return "Unknown";
        case 5:
            return "Dormant";
        case 6:
            return "NotPresent";
        default:
            return getIfAdminStatusString(ifOperStatus);
        }
    }

    /**
     * @return the ifSpeed
     */
    public static String getIfSpeedString(double ifSpeed) {
        if (ifSpeed < 1000) {
            return new StringBuilder().append(format.format(ifSpeed)).append("  Bps").toString();
        } else if (ifSpeed < 1000000) {
            if (ifSpeed % 1000 == 0) {
                return new StringBuilder().append(format.format(ifSpeed / 1000.0)).append(" KBps").toString();
            } else {
                return new StringBuilder().append(format.format(ifSpeed / 1024.0)).append(" KBps").toString();
            }
        } else if (ifSpeed < 1000000000) {
            if (ifSpeed % 1000 == 0) {
                return new StringBuilder().append(format.format(ifSpeed / 1000000.0)).append(" MBps").toString();
            } else {
                return new StringBuilder().append(format.format(ifSpeed / 1048576.0)).append(" MBps").toString();
            }
        } else {
            if (ifSpeed % 1000 == 0) {
                return new StringBuilder().append(format.format(ifSpeed / 1000000000.0)).append(" GBps").toString();
            } else {
                return new StringBuilder().append(format.format(ifSpeed / 1073741824.0)).append(" GBps").toString();
            }
        }
    }

    /**
     * @return the ifType name
     */
    public static String getIfTypeString(int ifType) {
        switch (ifType) {
        case 1:
            return "Other";
        case 6:
            return "EthernetCsmacd";
        case 7:
            return "Iso88023Csmacd";
        case 23:
            return "Ppp";
        case 24:
            return "SoftwareLoopback";
        case 56:
            return "FibreChannel";
        case 62:
            return"FastEther";
        case 71:
            return "Wifi";
        case 117:
            return "GigabitEthernet";
        case 135:
            return "l2Vlan";
        case 136:
            return "l3IpVlan";
        case 209:
            return "209";
        default:
            return String.valueOf(ifType);
        }
    }

    /**
     * 格式: 79 days, 14:25:12.00
     * 
     * @param str
     * @return
     */
    public static long getLastChangeTime(String str) {
        if (str == null || "".equals(str)) {
            return 0;
        }
        long sum = 0;
        String[] arr = str.split(",");
        if (arr.length == 1) {
            String[] times = arr[0].trim().split(":");
            sum = sum + 3600 * Long.parseLong(times[0]) + 60 * Long.parseLong(times[1])
                    + Long.parseLong(times[2].substring(0, 2));
        } else if (arr.length == 2) {
            sum = sum + 24 * 3600 * Integer.parseInt(arr[0].replace("days", "").replace("day", "").trim());
            String[] times = arr[1].trim().split(":");
            sum = sum + 3600 * Long.parseLong(times[0]) + 60 * Long.parseLong(times[1])
                    + Long.parseLong(times[2].substring(0, 2));
        } else {
        }
        if (sum == 0) {
            return 0;
        }
        sum = System.currentTimeMillis() - sum * 1000;
        return sum;
    }

}
