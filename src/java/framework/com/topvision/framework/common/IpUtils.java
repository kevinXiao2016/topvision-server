/***********************************************************************
 * $Id: IpUtils.java,v 1.1 Jul 25, 2008 5:42:39 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.framework.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.topvision.framework.exception.FormatException;

/**
 * @Create Date Jul 25, 2008 5:42:39 PM
 * 
 * @author kelers
 * 
 */
public class IpUtils implements Serializable, Comparable<IpUtils> {
    private static final long serialVersionUID = 3397980582189364257L;
    public static final long MAX_IP = 0xFFFFFFFFL;
    public static final int MAX_NUMBER = 0xFF;
    public static final int OFFSET = 8;
    public static final int PART_COUNT = 4;
    public static final String DOT = ".";
    public static final String STOP_TOKENIZER = "0";
    public final static String A_CLASS = "A";
    public final static String B_CLASS = "B";
    public final static String C_CLASS = "C";
    public final static String D_CLASS = "D";
    public final static String E_CLASS = "E";
    public final static long A_MAX = 2147483647L;
    public final static long B_MAX = 3221225471L;
    public final static long C_MAX = 3758096383L;
    public final static long D_MAX = 4026531839L;
    public final static long E_MAX = 4294967295L;
    public final static Pattern IP_PATTERN = Pattern
            .compile("^(1\\d{2}|2[0-4 ]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$");
    public final static Pattern IP_V4_PATTERN = Pattern
            .compile("^(1\\d{2}|2[0-4 ]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$");
    public final static Pattern IP_V6_PATTERN = Pattern
            .compile("^([\\da-fA-F]{1,4}:){7}[\\da-fA-F]{1,4}$|^:((:[\\da-fA-F]{1,4}){1,6}|:)$|^[\\da-fA-F]{1,4}:((:[\\da-fA-F]{1,4}){1,5}|:)$|^([\\da-fA-F]{1,4}:){2}((:[\\da-fA-F]{1,4}){1,4}|:)$|^([\\da-fA-F]{1,4}:){3}((:[\\da-fA-F]{1,4}){1,3}|:)$|^([\\da-fA-F]{1,4}:){4}((:[\\da-fA-F]{1,4}){1,2}|:)$|^([\\da-fA-F]{1,4}:){5}:([\\da-fA-F]{1,4})?$|^([\\da-fA-F]{1,4}:){6}:$");
    public final static Pattern IP_V4_HEX_PATTERN = Pattern.compile("^([\\da-fA-F]{2}:){3}[\\da-fA-F]{2}$");
    public final static Pattern IP_V6_MIB_8_PATTERN = Pattern.compile("^([\\da-fA-F]{1,4}:){7}[\\da-fA-F]{1,4}$");
    public final static Pattern IP_V6_MIB_16_PATTERN = Pattern.compile("^([\\da-fA-F]{2}:){15}[\\da-fA-F]{2}$");
    public final static Pattern MULTI_COLONS_PATTERN = Pattern.compile("(:{2,})");
    public final static Pattern BARKETS_PATTERN = Pattern.compile("\\(\\S+\\)");
    
    public static String getIpClass(String ip) {
        long value = ip2long(ip);
        String ipClass = "";
        if (value < A_MAX)
            ipClass = A_CLASS;
        else if (value < B_MAX)
            ipClass = B_CLASS;
        else if (value < C_MAX)
            ipClass = C_CLASS;
        else if (value < D_MAX)
            ipClass = D_CLASS;
        else
            ipClass = E_CLASS;
        return ipClass;
    }

    /**
     * 
     * 
     * @param from
     *            开始IP地址
     * @param to
     *            结束IP地址
     * @param list
     * 
     */
    public static List<String> getIpListFromSegment(String from, String to) {
        List<String> list = new ArrayList<String>();
        try {
            int[] fromIp = ipToIntArray(from);
            int[] toIp = ipToIntArray(to);
            int[] pointIp = fromIp;
            list.add("" + pointIp[0] + "." + pointIp[1] + "." + pointIp[2] + "." + pointIp[3]);
            int i = 0;
            while (!(pointIp[0] == toIp[0] && pointIp[1] == toIp[1] && pointIp[2] == toIp[2] && pointIp[3] == toIp[3])) {
                pointIp[3]++;
                if (pointIp[3] > 255) {
                    pointIp[2]++;
                    pointIp[3] = 0;
                }
                if (pointIp[2] > 255) {
                    pointIp[1]++;
                    pointIp[2] = 0;
                }
                if (pointIp[1] > 255) {
                    pointIp[0]++;
                    pointIp[1] = 0;
                }
                i++;
                list.add(pointIp[0] + "." + pointIp[1] + "." + pointIp[2] + "." + pointIp[3]);
            }
        } catch (Exception ex) {
        }
        return list;
    }

    /**
     * 
     * 
     * @param ip
     *            ip
     * @param mask
     *            mask
     * @param list
     *            list
     */
    public static List<String> getIpListFromSubnet(String ip, String mask) {
        int[] ipInt = ipToIntArray(ip);
        int[] maskInt = ipToIntArray(mask);
        int[] temp = ipToIntArray("0.0.0.0");

        for (int j = 0; j < 4; j++) {
            temp[j] = ipInt[j] & maskInt[j];
        }
        temp[3] = temp[3] + 1;
        String from = intArrayToIp(temp);

        for (int i = 0; i < 4; i++) {
            maskInt[i] = 255 ^ maskInt[i];
            temp[i] = ipInt[i] | maskInt[i];
        }
        temp[3] = temp[3] - 1;
        String to = intArrayToIp(temp);
        // Modify by Rod EMS-4926
        if (IpUtils.parseLong(from) >= IpUtils.parseLong(to)) {
            List<String> result = new ArrayList<String>();
            result.add(ip);
            return result;
        }
        return getIpListFromSegment(from, to);
    }

    /**
     * This method convert subnet mash to bit sum. eg.192.168.1.0/255.255.255.0===>>192.168.1.0/24
     * 
     * @param mask
     *            --255.255.255.0
     * @return int bit --24
     */
    public static int getMaskBitSum(String mask) {
        int bit = 0;
        int[] ip = ipToIntArray(mask);
        for (int i = 0; i < 4; i++) {
            if (ip[i] == 255) {
                bit = bit + 8;
            } else if (ip[i] == 254) {
                bit = bit + 7;
            } else if (ip[i] == 252) {
                bit = bit + 6;
            } else if (ip[i] == 248) {
                bit = bit + 5;
            } else if (ip[i] == 240) {
                bit = bit + 4;
            } else if (ip[i] == 224) {
                bit = bit + 3;
            } else if (ip[i] == 192) {
                bit = bit + 2;
            } else if (ip[i] == 128) {
                bit = bit + 1;
            } else {
                break;
            }
        }
        return bit;
    }

    /**
     * @param ip
     *            ip
     * @return int
     */
    public static String intArrayToIp(int[] ip) {
        String ipAddress = "";
        for (int i = 0; i < 4; i++) {
            ipAddress = ipAddress + "" + ip[i] + ".";
        }
        return ipAddress.substring(0, (ipAddress.length() - 1));
    }

    public static long ip2long(String ip) {
        long result = 0;
        StringTokenizer token = new StringTokenizer(ip, DOT);
        for (int i = 0; i < 4; i++) {
            int v = Integer.parseInt(token.nextToken());
            if (v > MAX_NUMBER) {
                throw new FormatException("IP format error:" + ip);
            }

            result = result << OFFSET;
            result = result | v;
        }
        return result;
    }

    /**
     * @param ip
     *            ip
     * @return int
     */
    public static int[] ipToIntArray(String ip) {
        int[] ipInt = { 0, 0, 0, 0 };
        int i = 0;
        if (ip == null) {
            return ipInt;
        }
        StringTokenizer tokens = new StringTokenizer(ip, ".");
        while (tokens.hasMoreTokens()) {
            ipInt[i++] = Integer.parseInt(tokens.nextToken());
        }
        return ipInt;
    }

    public static boolean isAtMask(String mask, String ip1, String ip2) {
        long lmask = ip2long(mask);
        return (lmask & ip2long(ip1)) == (lmask & ip2long(ip2));
    }

    public static boolean isBetween(String ip, String from, String to) {
        long value = ip2long(ip);
        long start = ip2long(from);
        long end = ip2long(to);
        return start >= value && end <= value || start <= value && end >= value;
    }

    public static boolean isMoreThan(String first, String end) {
        return ip2long(first) > ip2long(end);
    }

    public static String long2ip(long l) {
        if (l < 0 && l > MAX_IP) {
            throw new FormatException(String.valueOf(l));
        }
        StringBuffer sb = new StringBuffer();
        for (int i = PART_COUNT; i > 1; i--) {
            sb.insert(0, l & MAX_NUMBER);
            sb.insert(0, DOT);
            l = l >> OFFSET;
        }
        sb.insert(0, l);
        return sb.toString();
    }

    /**
     * 解析IP输入为IP列表，多段用空格隔开
     * 
     * @param target
     * @return
     */
    public static List<String> parseIp(String target) {
        List<String> ips = new ArrayList<String>();
        StringTokenizer token = new StringTokenizer(target, " ");
        while (token.hasMoreTokens()) {
            String ip = token.nextToken();
            if (ip.indexOf('/') != -1) {
                ips.addAll(parseSubnet(ip));
            } else if (ip.indexOf("*") != -1) {
                ips.addAll(parserWildcard(ip));
            } else {
                ips.addAll(parseSegment(ip));
            }
        }
        return ips;
    }

    public static int[] parsePoint(String point) {
        String[] s1 = point.split(",");
        List<String> list = new ArrayList<String>();
        for (String s : s1) {
            if (s.indexOf('-') == -1) {
                list.add(s);
            } else {
                int index = s.indexOf('-');
                for (int i = Integer.parseInt(s.substring(0, index)); i <= Integer.parseInt(s.substring(index + 1)); i++) {
                    list.add(String.valueOf(i));
                }
            }
        }
        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = Integer.parseInt(list.get(i));
        }
        return result;
    }

    public static List<String> parserWildcard(String target) {
        String[] points = target.trim().split("\\.");
        if (points[0].equals("*")) {
            points[0] = "0-255";
        }
        if (points[1].equals("*")) {
            points[1] = "0-255";
        }
        if (points[2].equals("*")) {
            points[2] = "0-255";
        }
        if (points[3].equals("*")) {
            points[3] = "1-254";
        }
        StringBuilder sBuilder = new StringBuilder().append(points[0]).append(".").append(points[1]).append(".")
                .append(points[2]).append(".").append(points[3]);
        return parseSegment(sBuilder.toString());
    }

    /**
     * 支持网段方式，如192.168.1,2,100-103.1-255
     * 
     * @param target
     * @return
     */
    public static List<String> parseSegment(String target) {
        String[] points = target.trim().split("\\.");
        if (points.length != 4) {
            throw new IllegalArgumentException(target);
        }
        int[] points0 = parsePoint(points[0]);
        int[] points1 = parsePoint(points[1]);
        int[] points2 = parsePoint(points[2]);
        int[] points3 = handleLastSegment(parsePoint(points[3]));
        List<String> ips = new ArrayList<String>();
        for (int point0 : points0) {
            for (int point1 : points1) {
                for (int point2 : points2) {
                    for (int point3 : points3) {
                        ips.add(intArrayToIp(new int[] { point0, point1, point2, point3 }));
                    }
                }
            }
        }
        return ips;
    }

    private static int[] handleLastSegment(int[] segment) {
        List<Integer> segmentAvaiable = new ArrayList<>();
        for (int i = 0; i < segment.length; i++) {
            if (segment[i] < 255) {
                segmentAvaiable.add(segment[i]);
            }
        }
        int[] result = new int[segmentAvaiable.size()];
        for (int i = 0; i < segmentAvaiable.size(); i++) {
            result[i] = segmentAvaiable.get(i).intValue();
        }
        return result;
    }

    /**
     * 解析掩码方式
     * 
     * eg.192.168.1.1/24 or 192.168.1.1/255.255.255.0
     * 
     * @param target
     * @return
     */
    public static List<String> parseSubnet(String target) {
        int index = target.indexOf('/');
        String mask = target.substring(index + 1);
        try {
            int imask = Integer.parseInt(mask);
            if (imask < 0) {
                imask = 0;
            } else if (imask > 32) {
                imask = 32;
            }
            StringBuilder bmask = new StringBuilder();
            for (int i = 0; i < imask; i++) {
                bmask.append("1");
            }
            bmask.append("00000000000000000000000000000000");
            mask = Integer.parseInt(bmask.substring(0, 8), 2) + "." + Integer.parseInt(bmask.substring(8, 16), 2) + "."
                    + Integer.parseInt(bmask.substring(16, 24), 2) + "." + Integer.parseInt(bmask.substring(24, 32), 2)
                    + ".";
        } catch (Exception ex) {
        }
        return getIpListFromSubnet(target.substring(0, index), mask);
    }

    private byte[] bytes = new byte[4];
    private String whole = "";
    private long value = 0;

    /**
     * Constructor.
     * 
     * @param ipAddress
     *            String
     */
    public IpUtils(String ipAddress) {
        StringTokenizer st = new StringTokenizer(ipAddress, DOT);
        StringBuffer sb = new StringBuffer();
        int i = 0;
        for (; st.hasMoreTokens() && i < PART_COUNT; i++) {
            try {
                int v = Integer.parseInt(st.nextToken());
                if (v > MAX_NUMBER) {
                    throw new FormatException(ipAddress);
                }
                bytes[i] = (byte) v;
                sb.append(DOT);
                sb.append(v & 0x0000000000FFL);
                value = value << OFFSET;
                value = value | v;
            } catch (NumberFormatException ne) {
                throw new FormatException(ne);
            }
        }
        if (i != PART_COUNT || st.hasMoreTokens()) {
            throw new FormatException(ipAddress);
        }
        whole = sb.substring(DOT.length());
    }

    /**
     * Constructor.
     * 
     * @param ipAddress
     *            long
     */
    public IpUtils(long ipAddress) {
        if (ipAddress < 0 && ipAddress > MAX_IP) {
            throw new FormatException(String.valueOf(ipAddress));
        }
        value = ipAddress;
        StringBuffer sb = new StringBuffer();
        for (int i = PART_COUNT - 1; i >= 0; i--) {
            bytes[i] = (byte) (ipAddress & MAX_NUMBER);
            sb.insert(0, bytes[i] & 0x0000000000FFL);
            sb.insert(0, DOT);
            ipAddress = ipAddress >> OFFSET;
        }
        // System.out.println("sb = " + sb);
        whole = sb.substring(DOT.length());
    }

    /**
     * Constructor.
     * 
     * @param ipAddress
     *            long
     */
    public IpUtils(byte[] ipAddress) {
        if (ipAddress.length != 4) {
            throw new FormatException(Arrays.asList(ipAddress).toString());
        }
        StringBuffer sb = new StringBuffer();
        int i = 0;
        for (; i < ipAddress.length; i++) {
            try {
                int v = (int) (ipAddress[i] & 0x0000000000FFL);
                if (v > MAX_NUMBER) {
                    throw new FormatException(Arrays.asList(ipAddress).toString());
                }
                sb.append(DOT);
                sb.append(v & 0x0000000000FFL);
                value = value << OFFSET;
                value = value | v;
                bytes[i] = ipAddress[i];
            } catch (NumberFormatException ne) {
                throw new FormatException(ne);
            }
        }
        whole = sb.substring(DOT.length());
    }

    /**
     * Constructor.
     */
    public IpUtils() {
    }

    /**
     * isAtMask.
     * 
     * @param ip1
     *            IP
     * @param ip2
     *            IP
     * @return boolean
     */
    public boolean isAtMask(IpUtils ip1, IpUtils ip2) {
        return (value & ip1.value) == (value & ip2.value);
    }

    /**
     * isBetween.
     * 
     * @param first
     *            IP
     * @param second
     *            IP
     * @return boolean
     */
    public boolean isBetween(IpUtils first, IpUtils second) {
        return first.value >= value && second.value <= value || first.value <= value && second.value >= value;
    }

    /**
     * isMoreThan.
     * 
     * @param ip
     *            IP
     * @return boolean
     */
    public boolean isMoreThan(IpUtils ip) {
        return value > ip.value;
    }

    /**
     * isLessThan.
     * 
     * @param ip
     *            IP
     * @return boolean
     */
    public boolean isLessThan(IpUtils ip) {
        return value < ip.value;
    }

    /**
     * @see Object#hashCode()
     */
    public int hashCode() {
        return whole.hashCode();
    }

    /**
     * @see Object#equals(Object)
     */
    public boolean equals(Object dest) {
        if (dest instanceof IpUtils) {
            return ((IpUtils) dest).value == value;
        } else {
            return false;
        }
    }

    /**
     * @see Object#toString()
     */
    public String toString() {
        return this.whole;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(IpUtils ip) {
        if (this.value < ip.value) {
            return -1;
        } else if (this.value == ip.value) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * @return
     */
    public long longValue() {
        return value;
    }

    /**
     * @param ip
     * @return
     * @throws Exception
     */
    public static long parseLong(String ip) {
        long result = 0;
        StringTokenizer token = new StringTokenizer(ip, DOT);
        for (int i = 0; i < 4; i++) {
            int v = Integer.parseInt(token.nextToken());
            if (v > MAX_NUMBER) {
                throw new FormatException(ip);
            }

            result = result << OFFSET;
            result = result | v;
        }
        return result;
    }

    public byte[] getBytes() {
        return bytes;
    }

    /**
     * 验证IP格式是否正确
     * 
     * @param ip
     * @return
     */
    public static boolean matches(String ip) {
        return IP_PATTERN.matcher(ip).matches();
    }

    public static String getNetworkSegFromIpMask(String ip, String mask) {
        String[] ips = ip.split("\\.");
        int cursor = 0;
        StringTokenizer st = new StringTokenizer(mask, DOT);
        StringBuffer sb = new StringBuffer();
        while (st.hasMoreElements()) {
            String token = (String) st.nextElement();
            if (STOP_TOKENIZER.equals(token)) {
                sb.append(DOT);
                sb.append(STOP_TOKENIZER);
            } else {
                sb.append(DOT);
                sb.append(ips[cursor]);
            }
            cursor++;
        }
        return sb.substring(1);
    }

    public static String transferHexIp(String ipHex) {
        if (ipHex != null && !"".equals(ipHex.trim())) {
            String[] ipSegment = ipHex.split(":");
            String ip = "";
            if (ipSegment.length > 1) {
                for (String string : ipSegment) {
                    ip += Integer.parseInt(string, 16) + ".";
                }
                return ip.substring(0, ip.length() - 1);
            } else {
                return ipHex;
            }
        } else
            return "";
    }
    
    /**
     * 将inetAddress转成合适的格式，IPv4转成点分十进制，IPv6转成八段的完整格式
     * 接受的输入格式：
     * 1、点分十进制：192.168.0.1  -> 不做处理
     * 2、冒号区分的4段十六进制： 6e:12:34:01 -> 转成点分十进制 110.18.52.1
     * 3、冒号区分的8段十六进制：3001:00:00:00:140:342D:4357:D613 -> 转成八段完整格式 3001:0000:0000:0000:0140:342D:4357:D613
     * 4、冒号区分的16段十六进制：30:01:00:00:00:00:00:00:f9:ac:42:52:31:d3:c5:61 -> 转成八段完整格式：3001:0000:0000:0000:F9AC:4252:31D3:C561
     * @param ip
     * @return
     */
    public static String formatInetAddress(String ip){
        if( ip ==null ) {
            return ip;
        }
        ip = ip.trim();
        if(IP_V4_HEX_PATTERN.matcher(ip).matches()){
            String[] hexParts = ip.split(":");
            String[] decParts = new String[4];
            for(int i=0; i<hexParts.length; i++){
                decParts[i] = String.valueOf(Integer.parseInt(hexParts[i], 16));
            }
            return StringUtils.join(decParts, ".");
        }else if(IP_V6_MIB_8_PATTERN.matcher(ip).matches()){
            String[] hexParts = ip.split(":");
            for(int i=0; i < hexParts.length; i++){
                while(hexParts[i].length()<4){
                    hexParts[i] = "0" + hexParts[i];
                }
            }
            return StringUtils.join(hexParts, ":").toUpperCase();
        }else if(IP_V6_MIB_16_PATTERN.matcher(ip).matches()){
            String[] hexParts = ip.split(":");
            String[] standardParts = new String[8];
            for(int i=0; i < standardParts.length; i++){
                standardParts[i] = hexParts[i*2] + hexParts[i*2+1];
            }
            return StringUtils.join(standardParts, ":").toUpperCase();
        }
        return ip;
    }
}
