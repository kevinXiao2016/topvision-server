/***********************************************************************
 * $Id: TypeChange.java,v1.0 2011-11-10 下午12:00:14 $
 * 
 * @author: Dosion_Huang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.topvision.framework.constants.Symbol;
import com.topvision.ems.template.service.EntityTypeService;

/**
 * @author Dosion_Huang
 * @created @2011-11-10-下午12:00:14
 * 
 */
public class CmcUtil {
    private static Long UBR7225_UPCHANNEL = 129l;
    private static Long UBR7225_DOWNCHANNEL = 128l;
    private static Long UBR7225_UPLINK = 6l;
    private static Long CASA_UPCHANNEL = 205l;
    private static Long CASA__DOWNCHANNEL = 128l;
    private static Long CASA_UPLINK = 6l;
    private static Long BSR2000_UPCHANNEL = 205l;
    private static Long BSR2000__DOWNCHANNEL = 128l;
    private static Long BSR2000_UPLINK1 = 6l;// 上联口
    private static Long BSR2000_UPLINK2 = 117l;// 上联口

    /**
     * 将long型time指定的数据转化为字符串，格式为**D**H**M**S
     * 
     * @param time
     *            所要转化的时间，单位是second
     * @return
     */
    public static String timeFormat(long time) {
        StringBuilder sb = new StringBuilder();
        return sb.append(time / (24 * 3600)).append("D").append(time % (24 * 3600) / 3600).append("H")
                .append(time % (24 * 3600) % 3600 / 60).append("M").append(time % (24 * 3600) % 3600 % 60).append("S")
                .toString();
    }

    /**
     * 将long型time指定的数据转化为字符串，格式为**天**时**分**秒
     * 
     * @param time
     *            所要转化的时间，单位是second
     * @return
     */
    public static String timeFormatToZh(long time) {
        // 0l表示未采集到时间
        if (time == 0l) {
            return "NoValue";
        }
        return ResourcesUtil.getString("COMMON.runtime", String.valueOf(time / (24 * 3600)),
                String.valueOf(time % (24 * 3600) / 3600), String.valueOf(time % (24 * 3600) % 3600 / 60),
                String.valueOf(time % (24 * 3600) % 3600 % 60));

    }

    /**
     * 转化与byte或bit有关的数据，将其转化为以M结尾或以K结尾的字符串 如1024转化为 “1024 K”
     * 
     * @param length
     *            所要转化的数据
     * @return
     */
    public static String turnToEndWithKOrM(long length) {
        if (length >= 1024 * 1024) {
            return (float) length / (1024 * 1024) + " M";
        } else if (length >= 1024) {
            return (float) length / 1024 + " K";
        } else {
            return length + " ";
        }
    }

    /**
     * 转化与byte或bit有关的数据，将其转化为以M结尾或以K结尾的字符串 如1000转化为 “1 K” 用于转换硬件设备指标，以1000而非1024为单位转换的
     * 
     * @param length
     *            所要转化的数据
     * @return
     */
    public static String turnDevObjToEndWithKOrM(long length) {
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
        if (length >= 1000 * 1000) {
            return format.format((float) length / (1000 * 1000)) + " M";
        } else if (length >= 1000) {
            return format.format((float) length / 1000) + " K";
        } else {
            return length + " ";
        }
    }

    /**
     * 获取速率值，bps-Kbps-Mbps之间使用1000进行换算
     * 
     * @param rate
     * @return
     */
    public static String getIfOctetRateString(float rate) {
        float speed = rate * 8;
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(3);
        format.setMinimumFractionDigits(3);
        if (speed < 1000) {
            return format.format(speed) + "  bps";
        } else if (speed < 1000 * 1000) {
            return format.format(speed / 1000) + " Kbps";
        } else if (speed < 1000 * 1000 * 1000) {
            return format.format(speed / 1000 / 1000) + " Mbps";
        } else {
            return format.format(speed / 1000 / 1000 / 1000) + " Gbps";
        }
    }

    /**
     * 计算两个数的商，并转化为百分比，保留两位小数
     * 
     * @param num1
     *            被除数
     * @param num2
     *            除数
     * @return
     */
    public static String turnToPercent(long num1, long num2) {
        String s = "";
        DecimalFormat df = new DecimalFormat("0.00");
        if (num2 != 0) {
            s = df.format(((double) num1) / num2 * 100) + Symbol.PERCENT;
        } else
            s = "-%";
        return s;
    }

    /**
     * 将一个十进制数转成标准格式的十六进制（每个标准格式均为两个字节）
     * 
     * @param i
     * @return
     */
    public static String getByteHexStringFromNum(Integer i) {
        String result = "";
        if (Integer.toHexString(i).length() == 1) {
            result = String.format("0%s", Integer.toHexString(i));
        } else if (Integer.toHexString(i).length() == 2) {
            result = String.format("%s", Integer.toHexString(i));
        }
        return result;
    }

    /**
     * 将指定mac字符串中0-9,A-F 提取出来，并按照两个字符间使用：间隔
     * 
     * @param mac
     *            要转化的字符串
     * @return 返回指定格式的字符串“AA：AA”
     */
    public static String turnToMacType(String mac) {
        StringBuilder sb = new StringBuilder();
        String upper;
        upper = mac.toUpperCase();
        // 设置初始值
        int j = 0;
        // 转换字符
        for (int i = 0; i < upper.length(); i++) {
            if (upper.charAt(i) >= '0' && upper.charAt(i) <= '9' || upper.charAt(i) >= 'A' && upper.charAt(i) <= 'F') {
                if (j < 1) {
                    // 当为第一个要输出的字符时直接输出，并且让标记j++
                    sb.append(upper.charAt(i));
                    j++;
                } else {
                    // 当为第二个要输出的字符时输出该字符，并在其后面输出‘：’，置j=0
                    sb.append(upper.charAt(i)).append(':');
                    j = 0;
                }
            }
        }
        if (sb.length() >= 1 && sb.charAt(sb.length() - 1) == ':') {
            return sb.substring(0, sb.length() - 1);
        }
        return sb.toString();
    }

    public static long turnMacToLong(String mac) {
        String upper;
        upper = mac.toUpperCase();
        long longMac = 0L;
        if (mac.length() == 17) {
            for (int i = 0; i < 17; i++) {
                if (upper.charAt(i) >= '0' && upper.charAt(i) <= '9') {
                    longMac = longMac * 16 + (mac.charAt(i) - '0');
                } else if (upper.charAt(i) >= 'A' && upper.charAt(i) <= 'F') {
                    longMac = longMac * 16 + (upper.charAt(i) - 'A') + 10;
                }
            }
        } else
            return -1;
        return longMac;
    }

    /**
     * 对mib中的bits类型数据进行转换
     * 
     * @param value
     *            从mib中取到的值
     * @param type
     *            bits中不同位所代表的意义
     * @return
     */
    public static String turnBitsToString(String value, String[] type) {
        String result = "";
        StringBuilder sb = new StringBuilder();
        for (String s : value.split(Symbol.COLON)) {
            int i = Integer.parseInt(s, 16);
            int str2 = Integer.parseInt(Integer.toBinaryString(i));
            result += String.format("%08d", str2);
        }
        int i = 0;
        for (String s : type) {
            if (result.charAt(i) == '1') {
                sb.append(s).append("|");
            }
            i++;
        }
        if (sb.toString().length() == 0) {
            return "-";
        }
        return sb.toString();
    }

    /**
     * 通过mib中的十进制mac转换为系统可用的十六进制mac
     * 
     * @param mibMac
     * @return
     */
    public static String getMacFromMibMac(String mibMac) {
        String[] mibMacStrings = mibMac.split("\\.");
        StringBuilder result = new StringBuilder();
        for (String mac : mibMacStrings) {
            if (Integer.toHexString(Integer.parseInt(mac)).length() == 1) {
                result.append(String.format("0%s", (Integer.toHexString(Integer.parseInt(mac))))).append(Symbol.COLON);
            } else {
                result.append(String.format("%s", (Integer.toHexString(Integer.parseInt(mac))))).append(Symbol.COLON);
            }
        }
        return result.substring(0, result.length() - 1);

    }

    public static String turnHexMacToMibMacIndex(String hexMac) {
        String[] hexMacArray = hexMac.split(":");
        if (hexMacArray.length > 0) {
            StringBuilder mibMac = new StringBuilder();
            mibMac.append(Integer.parseInt(hexMacArray[0], 16));
            for (int i = 1; i < hexMacArray.length; i++) {
                mibMac.append(".").append(Integer.parseInt(hexMacArray[i], 16));
            }
            return mibMac.toString();
        } else {
            return hexMac;
        }

    }

    /**
     * 截取不超过长度255的字符串
     */
    public static String cutStringIn255(String str) {
        if (str != null && str.length() > 255) {
            str = str.substring(0, 254);
        }
        return str;
    }

    /**
     * 將16進制字符串轉成文本
     */
    public static String toStringHex(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
        }
        s = new String(baKeyword);
        return s;
    }

    /**
     * Add by Rod
     * 
     * @param event_text
     * @return
     */
    public static String getChannelIdFromTrapText(String event_text) {
        event_text = event_text.toLowerCase();
        String tmp = "";
        String channelId = "";
        // Add by Rod
        if (event_text.split(":").length > 10) {
            String[] array = event_text.split(Symbol.COLON);
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < array.length; j++) {
                sb.append(array[j]);
            }
            String text = sb.toString();
            event_text = CmcUtil.toStringHex(text);
        }
        if (event_text.indexOf("us ") != -1) {
            tmp = "[US";
            int position = event_text.indexOf("us ");
            channelId = event_text.substring(position + 3, position + 5);
        } else if (event_text.indexOf("ds ") != -1) {
            tmp = "[DS";
            int position = event_text.indexOf("ds ");
            channelId = event_text.substring(position + 3, position + 5);
        }
        if (channelId.endsWith(" ")) {
            return tmp + channelId.substring(0, 1) + "]";
        }
        return tmp + channelId + "]";
    }

    /**
     * Modify By Rod
     * 
     * 告警报文的text节点下截取CCMTS的mac地址
     */
    public static String getCCMTSMACFromTrapText(String event_text) {
        String ccmts_mac = "";
        String[] array = event_text.split(Symbol.COLON);
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < array.length; j++) {
            sb.append(array[j]);
        }
        String text = sb.toString();
        event_text = CmcUtil.toStringHex(text);
        if (event_text.indexOf("CMTSMAC=") != -1) {
            int position = event_text.indexOf("CMTSMAC=");
            ccmts_mac = event_text.substring(position + 8, position + 25);
        } else if (event_text.indexOf("CMC=") != -1) {
            int position = event_text.indexOf("CMC=");
            ccmts_mac = event_text.substring(position + 4, position + 21);
        } else if (event_text.indexOf("CmcMac=") != -1) {
            int position = event_text.indexOf("CmcMac=");
            ccmts_mac = event_text.substring(position + 7, position + 24);
        } else if (event_text.indexOf("CMTS-MAC=") != -1) {
            int position = event_text.indexOf("CMTS-MAC=");
            String style = event_text.substring(position + 9, position + 14);
            if (style.indexOf(".") != -1) {
                ccmts_mac = changeStyle(event_text.substring(position + 9, position + 23));
            } else if (style.indexOf(":") != -1) {
                ccmts_mac = event_text.substring(position + 9, position + 26);
            }
        }
        return ccmts_mac.toUpperCase();
    }

    public static String changeStyle(String address) {
        String[] tmp = address.split("\\.");
        StringBuilder sb = new StringBuilder();
        sb.append(tmp[0].substring(0, 2)).append(":").append(tmp[0].substring(2, 4)).append(":");
        sb.append(tmp[1].substring(0, 2)).append(":").append(tmp[1].substring(2, 4)).append(":");
        sb.append(tmp[2].substring(0, 2)).append(":").append(tmp[2].substring(2, 4));
        return sb.toString();
    }

    /**
     * 复制文件
     * 
     * @param src
     * @param dst
     * @return String
     */
    public static String writeFile(File src, File dst) {
        String result = null;
        try {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = new BufferedInputStream(new FileInputStream(src));
                out = new BufferedOutputStream(new FileOutputStream(dst));
                byte[] buffer = new byte[512];
                // 开始写入
                int length = 0;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
                result = "putSuccess";
            } finally {
                if (null != in) {
                    in.close();
                }
                if (null != out) {
                    out.close();
                }
            }
        } catch (Exception e) {
            result = "putError";
        }
        return result;
    }

    /**
     * 对数值进行判断，只返回0-100的数。超过100的值返回100
     * 
     * @param percent
     * @return
     */
    public static Integer hundredPercentLimit(Integer percent) {
        if (percent > 100) {
            return 100;
        } else {
            return percent;
        }
    }

    /**
     * 读取文件信息的时候判断数组是否有可用对象
     * 
     * @param objArray
     *            数组列表
     * @return String
     */
    public static boolean isNonEmpty(Object[] objArray) {
        boolean result = false;
        for (int index = 0; index < objArray.length && !result; index++) {
            if (objArray[index] != null) {
                result = true;
            }
        }
        return result;
    }

    /**
     * 获得可用的索引值
     * 
     * @param start
     * @param end
     * @param uList
     * @return
     */
    public static Integer getAviableIndex(int start, int end, List<Integer> uList) {
        List<Integer> avList = new ArrayList<Integer>();
        for (int i = start; i <= end; i++) {
            avList.add(i);
        }
        avList.removeAll(uList);
        // 如果无可用索引则返回null
        return avList.size() == 0 ? null : avList.get(0);
    }

    /**
     * 获取上行信道索引(x/x/x)
     * 
     * @param ifDescr
     * @param typeId
     * @return
     */
    public static String getCmtsUpChannelIndex(Long typeId, String ifDescr, EntityTypeService entityTypeService) {
        if (ifDescr != null && entityTypeService.isUbrCmts(typeId)) {
            return "US" + ifDescr.substring(ifDescr.indexOf("/") - 1, ifDescr.lastIndexOf("/") + 2) + "/"
                    + ifDescr.substring(ifDescr.length() - 1, ifDescr.length());
        } else if (ifDescr != null && entityTypeService.isCasaCmts(typeId)) {
            return "US" + ifDescr.substring(ifDescr.indexOf("/") - 1, ifDescr.lastIndexOf("/") + 2).replace(".", "/");
        } else if (ifDescr != null && entityTypeService.isBsrCmts(typeId)) {
            return "US" + ifDescr.substring(ifDescr.indexOf("/") - 1, ifDescr.lastIndexOf("/") + 2);
        } else if (ifDescr != null) {
            // modify by loyal 暂时使用casa设备类型作为默认类型，在设备类型重构中需要修改为根据不同厂商对信道进行解析
            return "US" + ifDescr.substring(ifDescr.indexOf("/") - 1, ifDescr.lastIndexOf("/") + 2).replace(".", "/");
            // return ifDescr;
        } else {
            return "";
        }
    }

    /**
     * 获取cmts端口类型(0:upchannle 1:downchannel 2:upLinkPort)
     * 
     * @param typeId
     * @param ifType
     * @return 0:up 1:down 2:upLinkPort
     */
    public static int getCmtsChannelType(Long typeId, Long ifType, EntityTypeService entityTypeService) {
        if (ifType != null && entityTypeService.isUbrCmts(typeId)) {
            if (UBR7225_UPCHANNEL.equals(ifType)) {
                return 0;
            } else if (UBR7225_DOWNCHANNEL.equals(ifType)) {
                return 1;
            } else if (UBR7225_UPLINK.equals(ifType)) {
                return 2;
            }
        } else if (ifType != null && entityTypeService.isCasaCmts(typeId)) {
            if (CASA_UPCHANNEL.equals(ifType)) {
                return 0;
            } else if (CASA__DOWNCHANNEL.equals(ifType)) {
                return 1;
            } else if (CASA_UPLINK.equals(ifType)) {
                return 2;
            }
        } else if (ifType != null && entityTypeService.isBsrCmts(typeId)) {
            if (BSR2000_UPCHANNEL.equals(ifType)) {
                return 0;
            } else if (BSR2000__DOWNCHANNEL.equals(ifType)) {
                return 1;
            } else if (BSR2000_UPLINK1.equals(ifType) || BSR2000_UPLINK2.equals(ifType)) {
                return 2;
            }
        }
        // modify by loyal 暂时使用casa设备类型作为默认类型，在设备类型重构中需要修改为根据不同厂商对信道进行解析
        else if (ifType != null) {
            if (CASA_UPCHANNEL.equals(ifType)) {
                return 0;
            } else if (CASA__DOWNCHANNEL.equals(ifType)) {
                return 1;
            } else if (CASA_UPLINK.equals(ifType)) {
                return 2;
            }
        }
        return -1;
    }

    /**
     * 获取下行信道索引(x/x/x)
     * 
     * @param ifDescr
     * @return
     */
    public static String getCmtsDownChannelIndex(Long typeId, String ifDescr, EntityTypeService entityTypeService) {
        if (ifDescr != null && entityTypeService.isUbrCmts(typeId)) {
            return "DS" + ifDescr.substring(ifDescr.indexOf("/") - 1, ifDescr.length()).replace(":", "/");
        } else if (ifDescr != null && entityTypeService.isCasaCmts(typeId)) {
            return "DS" + ifDescr.substring(ifDescr.indexOf("/") - 1, ifDescr.lastIndexOf("/") + 2);
        } else if (ifDescr != null && entityTypeService.isBsrCmts(typeId)) {
            return "DS" + ifDescr.substring(ifDescr.indexOf("/") - 1, ifDescr.lastIndexOf("/") + 2);
        } else if (ifDescr != null) {
            // modify by loyal 暂时使用casa设备类型作为默认类型，在设备类型重构中需要修改为根据不同厂商对信道进行解析
            return "DS" + ifDescr.substring(ifDescr.indexOf("/") - 1, ifDescr.lastIndexOf("/") + 2);
            // return ifDescr;
        } else {
            return "";
        }
    }
}
