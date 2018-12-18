/***********************************************************************
 * $Id: MacUtils.java,v1.0 2011-6-29 下午07:12:02 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Victor
 * @created @2011-6-29-下午07:12:02
 * 
 */
public class MacUtils implements Serializable {
    private static final long serialVersionUID = -3166107635279197475L;
    private static final Logger logger = LoggerFactory.getLogger(MacUtils.class);
    public static String LINE = "-";
    public static String KONGGE = " ";
    public static String MAOHAO = ":";
    public static String DIAN = ".";
    public static String WJG = "";
    private static String DEFAULT = MAOHAO;
    private List<List<String>> macs = new ArrayList<List<String>>();

    public MacUtils(byte[] bs) {
        try {
            if (bs.length == 6) {
                List<String> al = new ArrayList<String>();
                al.add(getHex(Long.toHexString(bs[0] & 0x0000000000FFL)));
                al.add(getHex(Long.toHexString(bs[1] & 0x0000000000FFL)));
                al.add(getHex(Long.toHexString(bs[2] & 0x0000000000FFL)));
                al.add(getHex(Long.toHexString(bs[3] & 0x0000000000FFL)));
                al.add(getHex(Long.toHexString(bs[4] & 0x0000000000FFL)));
                al.add(getHex(Long.toHexString(bs[5] & 0x0000000000FFL)));
                macs.add(al);
            }
        } catch (Exception e) {
            logger.debug("", e);
        }
    }

    public MacUtils(long l) {
        long a0 = (l & 0xFF0000000000L) >> 40;
        long a1 = (l & 0x00FF00000000L) >> 32;
        long a2 = (l & 0x0000FF000000L) >> 24;
        long a3 = (l & 0x000000FF0000L) >> 16;
        long a4 = (l & 0x00000000FF00L) >> 8;
        long a5 = (l & 0x0000000000FFL);

        List<String> al = new ArrayList<String>();
        al.add(getHex(Long.toHexString(a0)));
        al.add(getHex(Long.toHexString(a1)));
        al.add(getHex(Long.toHexString(a2)));
        al.add(getHex(Long.toHexString(a3)));
        al.add(getHex(Long.toHexString(a4)));
        al.add(getHex(Long.toHexString(a5)));
        macs.add(al);
    }

    public MacUtils(String mac) {
        try {
            if (mac != null && (!mac.equals("")) && (!mac.equals(" "))) {
                parseMac(macs, mac.toUpperCase().trim());
            } else {
                List<String> al = new ArrayList<String>();
                macs.add(al);
            }
        } catch (Exception e) {
            logger.debug("", e);
        }
    }

    public String toString() {
        if (macs.size() > 1) {
            StringBuffer sb = new StringBuffer();
            for (Iterator<List<String>> iter = macs.iterator(); iter.hasNext(); sb.append(";")) {
                List<String> al = iter.next();
                sb.append(getMac(al, DEFAULT));
            }
            return sb.toString().toUpperCase();
        } else if (macs.size() == 1) {
            return getMac(macs.get(0), DEFAULT);
        } else {
            return "";
        }
    }

    public String toString(String type) {
        if (macs.size() >= 1) {
            return getMac(macs.get(0), type);
        } else {
            return "";
        }
    }

    public long longValue() {
        List<String> al = macs.get(0);
        if (al.size() == 6) {
            try {
                Object[] ms = al.toArray();
                long a0 = Long.parseLong((String) ms[0], 16) << 40;
                long a1 = Long.parseLong((String) ms[1], 16) << 32;
                long a2 = Long.parseLong((String) ms[2], 16) << 24;
                long a3 = Long.parseLong((String) ms[3], 16) << 16;
                long a4 = Long.parseLong((String) ms[4], 16) << 8;
                long a5 = Long.parseLong((String) ms[5], 16);
                return a0 + a1 + a2 + a3 + a4 + a5;
            } catch (NumberFormatException e) {
                return -2;
            }
        } else {
            return -1;
        }
    }

    public byte[] getBytes() {
        List<String> al = macs.get(0);
        if (logger.isDebugEnabled()) {
            logger.debug("macs = " + macs);
        }
        if (al.size() == 6) {
            try {
                Object[] ms = al.toArray();
                byte[] bs = new byte[6];
                bs[0] = (byte) Integer.parseInt((String) ms[0], 16);
                bs[1] = (byte) Integer.parseInt((String) ms[1], 16);
                bs[2] = (byte) Integer.parseInt((String) ms[2], 16);
                bs[3] = (byte) Integer.parseInt((String) ms[3], 16);
                bs[4] = (byte) Integer.parseInt((String) ms[4], 16);
                bs[5] = (byte) Integer.parseInt((String) ms[5], 16);
                return bs;
            } catch (NumberFormatException e) {
                logger.debug("", e);
            }
        }
        byte[] bs = { (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0 };
        return bs;
    }

    public String getHex(String atmp) {
        if (atmp.length() == 1) {
            atmp = "0" + atmp;
        }
        return atmp;
    }

    public long getKey() {
        return longValue() & 0xFFFFFF000000L;
    }

    public long getKeyByMedian(long l) {
        return longValue() & l;
    }

    public boolean equals(Object o) {
        MacUtils mac = (MacUtils) o;
        if (mac.longValue() == longValue()) {
            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return 0;
    }

    /**
     * 比较2个MAC地址的大小： 0 相等 1 target > origin -1 target < origin
     * 
     * @param targetMac
     * @param originMac
     * @return
     */
    public static int compare(String targetMac, String originMac) {
        MacUtils target = new MacUtils(targetMac);
        MacUtils origin = new MacUtils(originMac);
        long targetLong = target.longValue();
        long originLong = origin.longValue();
        if (targetLong == originLong) {
            return 0;
        } else if (targetLong > originLong) {
            return 1;
        } else {
            return -1;
        }
    }

    private void parseMac(List<List<String>> all, String mac) {
        if (mac.indexOf(";") != -1) {
            String[] tmps = mac.split(";");
            for (int i = 0; i < tmps.length; i++) {
                parseMac(all, tmps[i]);
            }
        } else if (mac.indexOf("=") != -1) {
            String[] tmps = mac.split("=");
            for (int i = 0; i < tmps.length; i++) {
                parseMac(all, tmps[i]);
            }
        } else if (mac.indexOf(".") != -1) {
            List<String> al = new ArrayList<String>();
            mac = mac.replace('.', '-');
            String[] ss = mac.split("-");
            for (int i = 0; i < ss.length; i++) {
                al.add(ss[i].substring(0, 2));
                al.add(ss[i].substring(2));
            }
            all.add(al);
        } else if (mac.indexOf("-") != -1) {
            List<String> al = new ArrayList<String>();
            String[] ss = mac.split("-");
            for (int i = 0; i < ss.length; i++) {
                al.add(ss[i].substring(0, 2));
                al.add(ss[i].substring(2));
            }
            all.add(al);
        } else if (mac.indexOf(":") != -1) {
            List<String> al = new ArrayList<String>();
            String[] ss = mac.split(":");
            for (int i = 0; i < ss.length; i++) {
                al.add(ss[i]);
            }
            all.add(al);
        } else if (mac.indexOf(" ") != -1) {
            List<String> al = new ArrayList<String>();
            String[] ss = mac.split(" ");
            for (int i = 0; i < ss.length; i++) {
                al.add(ss[i]);
            }
            all.add(al);
        } else if (mac.length() == 12) {
            List<String> al = new ArrayList<String>();
            al.add(mac.substring(0, 2));
            al.add(mac.substring(2, 4));
            al.add(mac.substring(4, 6));
            al.add(mac.substring(6, 8));
            al.add(mac.substring(8, 10));
            al.add(mac.substring(10, 12));
            all.add(al);
        } else {
            List<String> al = new ArrayList<String>();
            all.add(al);
        }

    }

    public String getMac(List<String> al, String type) {
        if (al.size() == 6) {
            Object[] ms = al.toArray();
            StringBuilder m = new StringBuilder();
            int i = 0;
            m.append(ms[i++]);
            for (; i < 6; i++) {
                m.append(type).append(ms[i]);
            }
            return m.toString().toUpperCase();
        } else {
            return "";
        }
    }

    public static String getMacStringFromNoISOControl(String noISOControlString) {
        Pattern hexStringPattern = Pattern.compile("^([0-9a-fA-F]{2})([-:][0-9a-fA-F]{2})+$");
        if (!hexStringPattern.matcher(noISOControlString).matches()) {
            String result = "";
            char[] charArray = noISOControlString.toCharArray();
            for (char c : charArray) {
                result += Integer.toHexString(c) + ":";
            }
            return result.substring(0, result.length() - 1);
        }
        return noISOControlString;
    }

    /**
     * 根据传入的合法的MAC地址模糊查询条件，生成有意义的查询范围并返回(传入的MAC地址条件必须为以：分割的)
     * 
     * @param mac
     * @return
     */
    public static String getFormatFuzzyMac(String mac) {
        String formatFuzzyMac = "";
        // 判断传入的MAC地址条件是否为以：分割的
        if (isFuzzyMacAddress(mac)) {
            return null;
        }

        return formatFuzzyMac;
    }

    /**
     * 判断输入MAC地址是否合法
     * 
     * @param mac
     * @return
     */
    public static boolean isMac(String mac) {
        // 匹配以冒号、横线和空格隔开的形式
        Pattern pattern1 = Pattern.compile("^([A-Fa-f\\d]{2}[\\s:-]){5}[A-Fa-f\\d]{2}$");
        // 匹配0000.0000.0000形式
        Pattern pattern2 = Pattern.compile("^([A-Fa-f\\d]{4}\\.){2}[A-Fa-f\\d]{4}$");
        // 匹配000000000000形式
        Pattern pattern3 = Pattern.compile("^[A-Fa-f\\d]{12}$");
        // 匹配0000-0000-0000形式
        Pattern pattern4 = Pattern.compile("^([A-Fa-f\\d]{4}\\-){2}[A-Fa-f\\d]{4}$");

        Matcher matcher1 = pattern1.matcher(mac);
        Matcher matcher2 = pattern2.matcher(mac);
        Matcher matcher3 = pattern3.matcher(mac);
        Matcher matcher4 = pattern4.matcher(mac);

        if (!matcher1.find() && !matcher2.find() && !matcher3.find() && !matcher4.find()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 将正确的MAC地址转换成系统默认的格式，以：分割，字母为大写
     * 
     * @param mac
     * @return
     */
    public static String formatMac(String mac) {
        Pattern pattern1 = Pattern.compile("([A-Fa-f\\d]{2})");
        Matcher matcher1 = pattern1.matcher(mac);
        String formatMac = "";
        while (matcher1.find()) {
            formatMac = formatMac + matcher1.group() + ":";
        }
        // 除去最后一个：
        formatMac = formatMac.substring(0, formatMac.length() - 1);
        return formatMac.toUpperCase();
    }

    /**
     * 验证输入的MAC地址是否为合法的模糊查询规则
     */
    public static boolean isFuzzyMacAddress(String mac) {
        // 模糊查询的MAC地址支持所有的合法的MAC地址(以：断点)的从第一位开始截取的任意部分
        Pattern pattern1 = Pattern.compile("^[A-Fa-f\\d]{1,2}$");
        Pattern pattern2 = Pattern.compile("^([A-Fa-f\\d]{1,2}:){1,5}$");
        Pattern pattern3 = Pattern.compile("^([A-Fa-f\\d]{1,2}:){1,5}[A-Fa-f\\d]{1,2}$");

        Matcher matcher1 = pattern1.matcher(mac);
        Matcher matcher2 = pattern2.matcher(mac);
        Matcher matcher3 = pattern3.matcher(mac);

        if (!matcher1.find() && !matcher2.find() && !matcher3.find()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 将MAC地址转成无间隔的字符串,用于在数据库中的存储
     * 
     * @param mac
     * @return
     */
    private static String convertMacToStorageFormat(String mac) {
        if (mac == null) {
            return mac;
        }
        // 如果字符串长度为6，则认为是采集端传过来的格式，进行转换
        if (mac.length() == 6) {
            StringBuffer sbBuffer = new StringBuffer();
            char[] charArray = mac.toCharArray();
            for (char c : charArray) {
                sbBuffer.append(Integer.toHexString(c));
            }
            return sbBuffer.toString().toUpperCase();
        } else {
            // 将输入mac地址中的间隔符及不合法字符均去掉,只保留0-9A-Fa-f
            String formatedMac = mac.replaceAll("[^\\dA-Fa-f]", "");
            return formatedMac.toUpperCase();
        }
    }

    /**
     * 格式化查询MAC字符串，处理逻辑为将所有系统识别的分隔符替换为：
     * 
     * 请保证先通过前端的验证后，再调用该方法
     * 
     * @param mac
     * @return
     */
    public static String formatQueryMac(String mac) {
        if (mac == null) {
            return mac;
        }
        // 将查询字符串中的所有间隔符转换成：
        mac = mac.replaceAll("[-\\.\\s]", ":").toUpperCase();
        // 考虑到有三段式的情况，如果查询字符串中间有间隔符，需要判断各间隔符之间的位置,并转换为六段式的情况来进行匹配
        ArrayList<Integer> splitIndexs = new ArrayList<Integer>();
        for (int i = 0; i < mac.length(); i++) {
            if (":".equals(String.valueOf(mac.charAt(i)))) {
                splitIndexs.add(i);
            }
        }
        if (splitIndexs.size() == 1) {
            // 如果只有一个间隔符，则判断其左右字符的长度，是否需要加上间隔符
            if (mac.length() - splitIndexs.get(0) > 3) {
                mac = mac.substring(0, splitIndexs.get(0) + 3) + ":"
                        + mac.substring(splitIndexs.get(0) + 3, mac.length());
            }
            if (splitIndexs.get(0) > 2) {
                mac = mac.substring(0, splitIndexs.get(0) - 2) + ":"
                        + mac.substring(splitIndexs.get(0) - 2, mac.length());
            }
        } else if (splitIndexs.size() == 2) {
            // 如果有两个间隔符，则判断其是否为三段式，以及是否需要在指定位置加上间隔符
            if (splitIndexs.get(1) - splitIndexs.get(0) == 5) {
                // 如果只有一个间隔符，则判断其左右字符的长度，是否需要加上间隔符
                if (mac.length() - splitIndexs.get(1) > 2) {
                    mac = mac.substring(0, splitIndexs.get(1) + 3) + ":"
                            + mac.substring(splitIndexs.get(1) + 3, mac.length());
                }
                mac = mac.substring(0, splitIndexs.get(0) + 3) + ":"
                        + mac.substring(splitIndexs.get(0) + 3, mac.length());
                if (splitIndexs.get(0) > 2) {
                    mac = mac.substring(0, splitIndexs.get(0) - 2) + ":"
                            + mac.substring(splitIndexs.get(0) - 2, mac.length());
                }
            }
        }
        // 如果间隔符大于2个，则肯定为六段式，不需做处理
        return mac;
    }

    public static String convertToMaohaoFormat(String mac) {
        String formatedMac = convertMacToStorageFormat(mac);
        if (formatedMac == null || formatedMac.length() != 12) {
            // 格式不正确,不做处理,直接返回原字符串
            return mac;
        } else {
            String finalMac = "";
            for (int i = 0; i < 6; i++) {
                finalMac += formatedMac.substring(2 * i, 2 * (i + 1)) + ":";
            }
            // 去掉最后一个间隔符
            finalMac = finalMac.substring(0, finalMac.length() - 1);
            return finalMac;
        }
    }

    public static String convertSnToMaohaoFormat(String sn) {
        if (sn == null || sn.length() != 16) {
            // 格式不正确,不做处理,直接返回原字符串
            return sn;
        } else {
            String finalSn = "";
            for (int i = 0; i < 8; i++) {
                finalSn += sn.substring(2 * i, 2 * (i + 1)) + ":";
            }
            // 去掉最后一个间隔符
            finalSn = finalSn.substring(0, finalSn.length() - 1);
            return finalSn;
        }
    }

    /**
     * 将MAC地址转成统一的显示格式
     * 
     * @param mac
     * @param displayRule
     *            (6#M#U之类的格式)
     * @return
     */
    public static String convertMacToDisplayFormat(String mac, String displayRule) {
        if (mac == null) {
            return mac;
        }
        // 验证displayRule是否符合规范，若不符合规范，直接返回原字符串
        Pattern pattern = Pattern.compile("^[136]#[MHDWK]#[UD]$");
        Matcher matcher = pattern.matcher(displayRule);
        if (!matcher.matches()) {
            return mac;
        }
        // 先格式化mac成无间隔的存储格式
        String formatedMac = convertMacToStorageFormat(mac);
        if (formatedMac.length() != 12) {
            // 格式不正确,不做处理,直接返回原字符串
            return mac;
        } else {
            String[] ruleParts = displayRule.split("#");
            Integer section = Integer.valueOf(ruleParts[0]);
            String delimiter = ruleParts[1];
            switch (delimiter) {
            case "M":
                delimiter = ":";
                break;
            case "H":
                delimiter = "-";
                break;
            case "D":
                delimiter = ".";
                break;
            case "K":
                delimiter = " ";
                break;
            case "W":
                delimiter = "";
                break;
            }
            String letterCase = ruleParts[2];
            // 格式化字母大小写
            if (letterCase.equals("U")) {
                formatedMac = formatedMac.toUpperCase();
            } else if (letterCase.equals("D")) {
                formatedMac = formatedMac.toLowerCase();
            }
            String finalMac = "";
            Integer sectionLength = 12 / section;
            for (int i = 0; i < section; i++) {
                finalMac += formatedMac.substring(sectionLength * i, sectionLength * (i + 1)) + delimiter;
            }
            // 去掉最后一个间隔符
            if (!delimiter.equals("")) {
                finalMac = finalMac.substring(0, finalMac.length() - 1);
            }
            return finalMac;
        }
    }

    public static long getKey(long mac) {
        return mac & 0xFFFFFF000000L;
    }

    public static long getKeyByMedian(long mac, long l) {
        return mac & l;
    }

}
