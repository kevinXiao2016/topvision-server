/***********************************************************************
 * $Id: EponUtil.java,v1.0 2011-10-14 下午01:37:34 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.smi.OctetString;

import com.topvision.ems.epon.dhcp.domain.OltDhcpGiaddrConfig;
import com.topvision.ems.epon.dhcp.domain.OltDhcpServerConfig;
import com.topvision.ems.epon.domain.OltPerf;
import com.topvision.ems.epon.domain.TableHeader;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.SystemConstants;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author huqiao
 * @created @2011-10-14-下午01:37:34
 * 
 */
public class EponUtil {
    private static Logger logger = LoggerFactory.getLogger(EponUtil.class);

    // public static void main(String[] args) {
    // StringBuilder sb = new StringBuilder();
    // sb.append("select tt.* from (select rownum rn , t.* from (");
    // sb.append("SELECT alertId, message, levelId FROM Alert WHERE entityId=#entityId# order by
    // levelId desc, firstTime desc");
    // sb.append(") t) tt where rn <![CDATA[>=]]> $start$ and rn <![CDATA[<=]]> ($start$+$limit$)");
    // System.out.println(sb.toString());
    // }

    /*
     * public static Integer isFileInFtpServer(String fileName) { Integer isExist = new Integer(0);
     * StringBuilder toSrc = new StringBuilder(); toSrc.append(SystemConstants.ROOT_REAL_PATH);
     * toSrc.append("META-INF/ftpTemp/"); toSrc.append(fileName); File file = new
     * File(toSrc.toString()); if (file.exists()) { isExist = 1; } return isExist; }
     */

    // 需要考虑如果只有一个字节 没有":" 的情况 split方法能默认处理这种情况

    /**
     * 复制文件
     * 
     * @param src
     * @param dst
     * @return String
     */
    public static String write2File(File src, File dst) {
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
                result = "{success:true,msg:'suc'}";
            } finally {
                if (null != in) {
                    in.close();
                }
                if (null != out) {
                    out.close();
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = "fileNotExists";
        }
        return result;
    }

    /**
     * 通过mib里面的vlan位图获得所包括的vlan的List
     * 
     * @param vlanBitMap
     * @return
     */
    public static List<Integer> getVlanListFromMib(String vlanBitMap) {
        List<Integer> vlanList = new ArrayList<Integer>();
        if (vlanBitMap.length() == 1535) {
            String result = "";
            for (String s : vlanBitMap.split(":")) {
                int i = Integer.parseInt(s, 16);
                int str2 = Integer.parseInt(Integer.toBinaryString(i));
                result += String.format("%08d", str2);
            }
            if (result.indexOf("1") != -1) {
                int sum = result.indexOf("1");
                vlanList.add(sum);
                while (result.substring(result.indexOf("1") + 1).indexOf("1") >= 0) {
                    result = result.substring(result.indexOf("1") + 1);
                    sum += result.indexOf("1") + 1;
                    vlanList.add(sum);
                }
            }
        } else {
            vlanList = null;
        }
        return vlanList;
    }

    /**
     * 通过VLAN list获得mib中需要的位图
     * 
     * @param list
     * @return
     */
    public static String getVlanBitMapFormList(List<Integer> list) {
        StringBuilder vlanBitMapBuilder = new StringBuilder(String.format("%04096d", 0));
        for (int i : list) {
            // 字符串从0开始 vlan从0开始 需要从vlan = 字符串对应的位置
            vlanBitMapBuilder.setCharAt(i, '1');
        }
        StringBuilder vlanBitMapInHex = new StringBuilder();
        for (int i = 8; i <= vlanBitMapBuilder.length();) {
            String index = vlanBitMapBuilder.substring(i - 8, i);
            i = i + 8;
            int k = Integer.parseInt(index, 2); // 转成10进制
            if (Integer.toHexString(k).length() == 1) {
                vlanBitMapInHex.append(String.format("0%s", Integer.toHexString(k))).append(":");
            } else if (Integer.toHexString(k).length() == 2) {
                vlanBitMapInHex.append(String.format("%s", Integer.toHexString(k))).append(":");
            }

        }
        return vlanBitMapInHex.substring(0, vlanBitMapInHex.length() - 1);
    }

    /**
     * 通过SNI口MIB 转化为sniIndex的List / 同样适用于PON口
     * 
     * @param mibString
     * @return
     */
    public static List<Long> getSniPortIndexFromMib(String mibString) {
        List<Long> sniIndexList = new ArrayList<Long>();
        if (mibString != null && mibString.length() > 0) {
            String[] snis = mibString.split(":");
            for (int i = 0; i < snis.length / 4; i++) {
                // 由于snis[0]默认为1 , snis[4*i+3]默认为0,所以不考虑
                sniIndexList.add(EponIndex.getSniIndex(Integer.parseInt(snis[4 * i + 1], 16),
                        Integer.parseInt(snis[4 * i + 2], 16)));
            }
        }
        return sniIndexList;
    }

    /**
     * 通过sniIndex的List 转化为MIB中可用的字符串 / 同样适用于PON口
     * 
     * @param sniPortList
     * @return
     */
    public static String getMibStringFromSniPortList(List<Long> sniPortList) {
        if (sniPortList.size() == 0 || sniPortList == null) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            for (long sniIndex : sniPortList) {
                sb.append(String.format("%02d", 1)).append(":")
                        .append(getByteHexStringFromNum(EponIndex.getSlotNo(sniIndex).intValue())).append(":")
                        .append(getByteHexStringFromNum(EponIndex.getSniNo(sniIndex).intValue())).append(":")
                        .append(String.format("%02d", 0)).append(":");
            }
            // 上面的代码需要注意，第二个字节与第三个字节分别表示slot位与sni位，表示的方法是16进制，而不是10进制
            return sb.substring(0, sb.length() - 1).toString();
        }
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
                result.append(String.format("0%s", (Integer.toHexString(Integer.parseInt(mac))))).append(":");
            } else {
                result.append(String.format("%s", (Integer.toHexString(Integer.parseInt(mac))))).append(":");
            }
        }
        return result.substring(0, result.length() - 1);

    }

    /**
     * 将系统中十六进制的mac转换为mib中可用的十进制mac
     * 
     * @param hexMac
     * @return
     */
    public static String getMibMacFromHexMac(String hexMac) {
        String[] hexMacStrings = hexMac.split(":");
        StringBuilder result = new StringBuilder();
        for (String mac : hexMacStrings) {
            result.append(Integer.parseInt(mac, 16)).append(".");
        }
        return result.substring(0, result.length() - 1);
    }

    /**
     * 将MIB中用数字1作为定位标示的字节转化为特定值(从1开始)
     * 
     * @param mibByte
     * @return
     */
    public static Integer getSymbolInfoFromMibByte(String mibByte) {
        if (isConSpeCharacters(mibByte)) {
            mibByte = getOctetStringFromBit(mibByte);
        }
        String[] hexMacStrings = mibByte.split(":");
        StringBuilder result = new StringBuilder();
        for (String hexMac : hexMacStrings) {
            int i = Integer.parseInt(hexMac, 16);
            int str2 = Integer.parseInt(Integer.toBinaryString(i));
            result.append(String.format("%08d", str2));
        }
        return result.indexOf("1") + 1;
    }

    /**
     * 将MIB中用数字1作为定位标示的字节转化为特定值(从0开始)
     * 
     * @param mibByte
     * @return
     */
    public static Integer getSymbolInfoFromMibByteStartFromZero(String mibByte) {
        if (isConSpeCharacters(mibByte)) {
            mibByte = getOctetStringFromBit(mibByte);
        }
        String[] hexMacStrings = mibByte.split(":");
        StringBuilder result = new StringBuilder();
        for (String hexMac : hexMacStrings) {
            int i = Integer.parseInt(hexMac, 16);
            int str2 = Integer.parseInt(Integer.toBinaryString(i));
            result.append(String.format("%08d", str2));
        }
        return result.indexOf("1");
    }

    /**
     * 将系统中的特定值转为MIB中定位标示（从1开始）的字节（单个特定值）
     * 
     * @param byteNum
     *            MIB中的字节个数
     * @param index
     * @return
     */
    public static String getMibByteFromSymbolInfo(int byteNum, int index) {
        StringBuilder result = new StringBuilder();
        StringBuilder mibByte = new StringBuilder();
        for (int i = 0; i < byteNum; i++) {
            result.append("00000000");
        }
        // 此处与下面一个方法仅仅此处不同，本方法代表MIB的值从1开始
        result.setCharAt(index - 1, '1');
        for (int i = 0; i < byteNum; i++) {
            String byteString = result.substring(i * 8, i * 8 + 8);
            String hex = Integer.toHexString(Integer.parseInt(byteString.toString(), 2)); // 转成16进制
            if (hex.length() == 1) {
                mibByte.append(String.format("0%s", hex)).append(":");
            } else if (hex.length() == 2) {
                mibByte.append(String.format("%s", hex)).append(":");
            }
        }
        return mibByte.substring(0, mibByte.length() - 1);
    }

    /**
     * 将系统中的特定值转为MIB中定位标示（从0开始）的字节（单个特定值）
     * 
     * @param byteNum
     *            MIB中的字节个数
     * @param index
     * @return
     */
    public static String getMibByteFromSymbolInfoByStartZero(int byteNum, int index) {
        StringBuilder result = new StringBuilder();
        StringBuilder mibByte = new StringBuilder();
        for (int i = 0; i < byteNum; i++) {
            result.append("00000000");
        }
        // 此处与上面一个方法仅仅此处不同，本方法代表MIB的值从0开始
        result.setCharAt(index, '1');
        for (int i = 0; i < byteNum; i++) {
            String byteString = result.substring(i * 8, i * 8 + 8);
            String hex = Integer.toHexString(Integer.parseInt(byteString.toString(), 2)); // 转成16进制
            if (hex.length() == 1) {
                mibByte.append(String.format("0%s", hex)).append(":");
            } else if (hex.length() == 2) {
                mibByte.append(String.format("%s", hex)).append(":");
            }
        }
        return mibByte.substring(0, mibByte.length() - 1);
    }

    /**
     * 将系统中的特定值转为MIB中定位标示的字节(多个特定值)
     * 
     * @param byteNum
     * @param symbol
     * @return
     */
    public static String getMibByteFromSymbolInfoListByStartZero(int byteNum, List<Integer> symbol) {
        StringBuilder aclBitMapBuilder = new StringBuilder();
        for (int i = 0; i < byteNum; i++) {
            aclBitMapBuilder.append("00000000");
        }
        for (int i : symbol) {
            // 此处代表标识符从0开始
            aclBitMapBuilder.setCharAt(i, '1');
        }
        StringBuilder aclBitMapInHex = new StringBuilder();
        for (int i = 8; i <= aclBitMapBuilder.length();) {
            String index = aclBitMapBuilder.substring(i - 8, i);
            i = i + 8;
            int k = Integer.parseInt(index, 2); // 转成10进制
            if (Integer.toHexString(k).length() == 1) {
                aclBitMapInHex.append(String.format("0%s", Integer.toHexString(k))).append(":");
            } else if (Integer.toHexString(k).length() == 2) {
                aclBitMapInHex.append(String.format("%s", Integer.toHexString(k))).append(":");
            }

        }
        return aclBitMapInHex.substring(0, aclBitMapInHex.length() - 1);
    }

    // 通过ONU 端口的位图 获得相应ONU端口的位置集合
    public static List<Integer> getOnuPortListFromMibBitMap(String onuPortbitMap) {
        List<Integer> portList = new ArrayList<Integer>();
        String result = "";
        // 由于BITMAP的开始第一个字节为FF 表示盒式ONU 后续可扩展
        onuPortbitMap = onuPortbitMap.substring(3);
        for (String s : onuPortbitMap.split(":")) {
            int i = Integer.parseInt(s, 16);
            int str2 = Integer.parseInt(Integer.toBinaryString(i));
            result += String.format("%08d", str2);
        }
        if (result.indexOf("1") != -1) {
            int sum = result.indexOf("1") + 1;
            portList.add(sum);
            while (result.substring(result.indexOf("1") + 1).indexOf("1") >= 0) {
                result = result.substring(result.indexOf("1") + 1);
                sum += result.indexOf("1") + 1;
                portList.add(sum);
            }
        }
        return portList;
    }

    // 获得自动升级的ONU状态列表
    public static List<Integer> getAutoUpgOnuStatListFromMib(String BitMap) {
        List<Integer> symbolList = new ArrayList<Integer>();
        for (String s : BitMap.split(":")) {
            int i = Integer.parseInt(s, 16);
            if (i > 0) {
                symbolList.add(i);
            }
        }
        return symbolList;
    }

    // 获得自动升级的ONU列表
    public static List<String> getAutoUpgOnuListFromMib(String BitMap) {
        List<String> symbolList = new ArrayList<String>();
        String[] mapStr = BitMap.split(":");
        for (int i = 0; i < mapStr.length; i++) {
            int temp = Integer.parseInt(mapStr[i], 16);
            if (temp > 0) {
                symbolList.add((i / 128 + 1) + "_" + (i % 128 + 1));
            }
        }
        return symbolList;
    }

    //
    public static List<Integer> getSymbolListFromMib(String BitMap) {
        List<Integer> symbolList = new ArrayList<Integer>();
        String result = "";
        for (String s : BitMap.split(":")) {
            int i = Integer.parseInt(s, 16);
            int str2 = Integer.parseInt(Integer.toBinaryString(i));
            result += String.format("%08d", str2);
        }
        if (result.indexOf("1") != -1) {
            int sum = result.indexOf("1");
            symbolList.add(sum);
            while (result.substring(result.indexOf("1") + 1).indexOf("1") >= 0) {
                result = result.substring(result.indexOf("1") + 1);
                sum += result.indexOf("1") + 1;
                symbolList.add(sum);
            }
        }
        return symbolList;
    }

    // ACL action param 位图(size 18字节)->List(size 13)，未设置的字节值为：0xFF
    public static List<Integer> getTopAclActionParameterValueList(String bitMap) {
        List<Integer> list = new ArrayList<Integer>();
        if (bitMap.length() == 18 * 2 + 17) {
            String[] vlist = bitMap.split(":");
            // 位图1-4：端口入方向限速，单位：kpbs
            Integer v1 = Integer.parseInt(vlist[0] + vlist[1] + vlist[2] + vlist[3], 16);
            // 位图5：slotNo
            Integer v2 = Integer.parseInt(vlist[4], 16);
            // 位图6：ponNo/sniNo
            Integer v3 = Integer.parseInt(vlist[5], 16);
            // 位图7：onuNo
            Integer v4 = Integer.parseInt(vlist[6], 16);
            // 位图8：uniNo
            Integer v5 = Integer.parseInt(vlist[7], 16);
            // 位图9：队列号
            Integer v6 = Integer.parseInt(vlist[8], 16);
            // 位图10：内层COS
            Integer v7 = Integer.parseInt(vlist[9], 16);
            // 位图11：DSCP
            Integer v8 = Integer.parseInt(vlist[10], 16);
            // 位图12：TOS
            Integer v9 = Integer.parseInt(vlist[11], 16);
            // 位图13-14：外层VLAN ID
            Integer v10 = Integer.parseInt(vlist[12] + vlist[13], 16);
            // 位图15：外层VLAN COS，默认值：0
            Integer v11 = Integer.parseInt(vlist[14], 16);
            // 位图16-17：外层VLAN TPID 默认值：0x8100
            Integer v12 = Integer.parseInt(vlist[15] + vlist[16], 16);
            // 位图18：外层VLAN的优先级（0-7） 默认值：0
            Integer v13 = Integer.parseInt(vlist[17], 16);
            list.add(v1);
            list.add(v2);
            list.add(v3);
            list.add(v4);
            list.add(v5);
            list.add(v6);
            list.add(v7);
            list.add(v8);
            list.add(v9);
            list.add(v10);
            list.add(v11);
            list.add(v12);
            list.add(v13);
        }
        return list;
    }

    // ACL action param List(size 13)->位图(size 18字节)，未设置的字节值为：0xFF
    public static String getTopAclActionParameterFromValueList(List<Integer> list) {
        StringBuilder sbBuilder = new StringBuilder();
        if (list.size() == 13) {
            // 位图1-4：端口入方向限速，单位：kpbs
            String s1 = flushLeft('0', 8, Integer.toHexString(list.get(0)));
            sbBuilder.append(s1.substring(0, 2)).append(":").append(s1.substring(2, 4)).append(":")
                    .append(s1.substring(4, 6)).append(":").append(s1.substring(6, 8)).append(":");
            // 位图5：slotNo
            String s2 = flushLeft('0', 2, Integer.toHexString(list.get(1)));
            sbBuilder.append(s2).append(":");
            // 位图6：ponNo/sniNo
            String s3 = flushLeft('0', 2, Integer.toHexString(list.get(2)));
            sbBuilder.append(s3).append(":");
            // 位图7：onuNo
            String s4 = flushLeft('0', 2, Integer.toHexString(list.get(3)));
            sbBuilder.append(s4).append(":");
            // 位图8：uniNo
            String s5 = flushLeft('0', 2, Integer.toHexString(list.get(4)));
            sbBuilder.append(s5).append(":");
            // 位图9：队列号
            String s6 = flushLeft('0', 2, Integer.toHexString(list.get(5)));
            sbBuilder.append(s6).append(":");
            // 位图10：内层COS
            String s7 = flushLeft('0', 2, Integer.toHexString(list.get(6)));
            sbBuilder.append(s7).append(":");
            // 位图11：DSCP
            String s8 = flushLeft('0', 2, Integer.toHexString(list.get(7)));
            sbBuilder.append(s8).append(":");
            // 位图12：TOS
            String s9 = flushLeft('0', 2, Integer.toHexString(list.get(8)));
            sbBuilder.append(s9).append(":");
            // 位图13-14：外层VLAN ID
            String s10 = flushLeft('0', 4, Integer.toHexString(list.get(9)));
            sbBuilder.append(s10.substring(0, 2)).append(":").append(s10.substring(2, 4)).append(":");
            // 位图15：外层VLAN COS，默认值：0
            String s11 = flushLeft('0', 2, Integer.toHexString(list.get(10)));
            sbBuilder.append(s11).append(":");
            // 位图16-17：外层VLAN TPID 默认值：0x8100
            String s12 = flushLeft('0', 4, Integer.toHexString(list.get(11)));
            sbBuilder.append(s12.substring(0, 2)).append(":").append(s12.substring(2, 4)).append(":");
            // 位图18：外层VLAN的优先级（0-7）默认值：0
            String s13 = flushLeft('0', 2, Integer.toHexString(list.get(12)));
            sbBuilder.append(s13);
        }
        return sbBuilder.toString();
    }

    /**
     * 处理16进制位图的移动，比如说转成的十六进制为FFF，需要位图为0FFF
     * 
     * @param c
     *            需要补的字符
     * @param l
     *            需要补成后的长度
     * @param string
     * @return
     */
    public static String flushLeft(char c, long l, String string) {
        String str = "";
        String cs = "";
        if (string.length() > l)
            str = string;
        else
            for (int i = 0; i < l - string.length(); i++)
                cs = c + cs;
        str = cs + string;
        return str;
    }

    /**
     * 将bitMap的每个字节转化为十进制值
     * 
     * @param bitMap
     * @return
     */
    public static List<Integer> getEachBitValueFromOcterString(String bitMap) {
        List<Integer> list = new ArrayList<Integer>();
        for (String bitValue : bitMap.split(":")) {
            Integer value = Integer.parseInt(bitValue, 16);
            list.add(value);
        }
        return list;
    }

    /**
     * 将bitMap的每两个字节转化为十进制值
     * 
     * @param bitMap
     * @return
     */
    public static List<Integer> getTwiceBitValueFromOcterString(String bitMap) {
        List<Integer> list = new ArrayList<Integer>();
        String[] bitValue = bitMap.split(":");
        for (int k = 0; k < bitValue.length / 2; k++) {
            Integer value = Integer.parseInt(bitValue[2 * k] + bitValue[2 * k + 1], 16);
            list.add(value);
        }
        return list;
    }

    /**
     * 将bitMap的每四个字节转化为十进制值(不带符号)
     * 
     * @param bitMap
     * @return
     */
    public static List<Integer> getFourBitValueFromOcterString(String bitMap) {
        List<Integer> list = new ArrayList<Integer>();
        String[] bitValue = bitMap.split(":");
        for (int k = 0; k < bitValue.length / 4; k++) {
            Integer value = Integer
                    .parseInt(bitValue[4 * k] + bitValue[4 * k + 1] + bitValue[4 * k + 2] + bitValue[4 * k + 3], 16);
            list.add(value);
        }
        return list;
    }

    /**
     * 将bitMap的每四个字节转化为十进制值(带符号 可能存在负数)
     * 
     * @param bitMap
     * @return
     */
    public static List<Integer> getFourBitValueFromOcterStringInSymbol(String bitMap) {
        List<Integer> list = new ArrayList<Integer>();
        String[] bitValue = bitMap.split(":");
        for (int k = 0; k < bitValue.length / 4; k++) {
            Long longValue = Long
                    .parseLong(bitValue[4 * k] + bitValue[4 * k + 1] + bitValue[4 * k + 2] + bitValue[4 * k + 3], 16);
            list.add(longValue.intValue());
        }
        return list;
    }

    /**
     * 将List的十进制数字转化为bitMap(每个数字代表一个字节)
     * 
     * @param list
     * @return
     */
    public static String getOcterStringFromValueList(List<Integer> list) {
        StringBuilder sBuilder = new StringBuilder();
        for (Integer i : list) {
            String hex = flushLeft('0', 2, Integer.toHexString(i));
            sBuilder.append(hex).append(":");
        }
        return sBuilder.substring(0, sBuilder.length() - 1);
    }

    /**
     * 将List的十进制数字转化为bitMap(每个数字代表二个字节)
     * 
     * @param list
     * @return
     */
    public static String getOcterStringFromTwoByteValueList(List<Integer> list, Integer listSize) {
        while (list.size() < listSize) {
            list.add(0);
        }
        StringBuilder sBuilder = new StringBuilder();
        for (Integer i : list) {
            String s = flushLeft('0', 4, Integer.toHexString(i));
            sBuilder.append(s.substring(0, 2)).append(":").append(s.substring(2, 4)).append(":");

        }
        return sBuilder.substring(0, sBuilder.length() - 1);
    }

    /**
     * 将List的十进制数字转化为bitMap(每个数字代表四个字节)(不带符号)
     * 
     * @param list
     * @return
     */
    public static String getOcterStringFromFourByteValueList(List<Integer> list) {
        StringBuilder sBuilder = new StringBuilder();
        for (Integer i : list) {
            String s = flushLeft('0', 8, Integer.toHexString(i));
            sBuilder.append(s.substring(0, 2)).append(":").append(s.substring(2, 4)).append(":")
                    .append(s.substring(4, 6)).append(":").append(s.substring(6, 8)).append(":");

        }
        return sBuilder.substring(0, sBuilder.length() - 1);
    }

    /**
     * 将List的十进制数字转化为bitMap(每个数字代表四个字节)(带符号)
     * 
     * @param list
     * @return
     */
    public static String getOcterStringFromFourByteValueListInSymbol(List<Integer> list) {
        StringBuilder sBuilder = new StringBuilder();
        for (Integer i : list) {
            String s = flushLeft('0', 8, Long.toHexString(i));
            sBuilder.append(s.substring(0, 2)).append(":").append(s.substring(2, 4)).append(":")
                    .append(s.substring(4, 6)).append(":").append(s.substring(6, 8)).append(":");

        }
        return sBuilder.substring(0, sBuilder.length() - 1);
    }

    /**
     * 通过mib里面的cmProxy位图获得所包括的proxy的List
     * 
     * @param vlanBitMap
     * @return
     */
    public static List<Integer> getCmProxyListFromMib(String bitMap) {
        List<Integer> vlanList = new ArrayList<Integer>();
        // 10个字节 以及 9个 :
        // if (bitMap.length() == 2 * 10 + 9) {
        String result = "";
        for (String s : bitMap.split(":")) {
            int i = Integer.parseInt(s, 16);
            int str2 = Integer.parseInt(Integer.toBinaryString(i));
            result += String.format("%08d", str2);
        }
        if (result.indexOf("1") != -1) {
            int sum = result.indexOf("1");
            // cmProxy 从1开始，与Vlan从0开始不同
            vlanList.add(sum + 1);
            while (result.substring(result.indexOf("1") + 1).indexOf("1") >= 0) {
                result = result.substring(result.indexOf("1") + 1);
                sum += result.indexOf("1") + 1;
                vlanList.add(sum + 1);
            }
        }
        // } else {
        // vlanList = null;
        // }
        return vlanList;
    }

    /**
     * 通过cmProxy list获得mib中需要的位图
     * 
     * @param list
     * @return
     */
    public static String getBitMapFormProxyList(List<Integer> list) {
        StringBuilder proxyBitMapBuilder = new StringBuilder(String.format("%02000d", 0));
        if (!list.isEmpty()) {
            for (int i : list) {
                // 字符串从0开始 Proxy从1开始 需要从Proxy = 字符串对应的位置+1
                proxyBitMapBuilder.setCharAt(i - 1, '1');
            }
        }
        StringBuilder proxyBitMapInHex = new StringBuilder();
        for (int i = 8; i <= proxyBitMapBuilder.length();) {
            String index = proxyBitMapBuilder.substring(i - 8, i);
            i = i + 8;
            int k = Integer.parseInt(index, 2); // 转成10进制
            if (Integer.toHexString(k).length() == 1) {
                proxyBitMapInHex.append(String.format("0%s", Integer.toHexString(k))).append(":");
            } else if (Integer.toHexString(k).length() == 2) {
                proxyBitMapInHex.append(String.format("%s", Integer.toHexString(k))).append(":");
            }

        }
        return proxyBitMapInHex.substring(0, proxyBitMapInHex.length() - 1);
    }

    /**
     * // 通过sniIndex 获得2个字节组成的位图 第一个字节为slotNum 第二个字节为sniNum
     * 
     * @param sniIndex
     * @return
     */
    public static String getSniPortBitMapFormSniIndex(Long sniIndex) {
        StringBuilder sBuilder = new StringBuilder();
        String slotNumHex = Integer.toHexString(EponIndex.getSlotNo(sniIndex).intValue());
        String sniNumHex = Integer.toHexString(EponIndex.getSniNo(sniIndex).intValue());
        if (slotNumHex.length() == 1) {
            sBuilder.append(String.format("0%s", slotNumHex)).append(":");
        } else if (slotNumHex.length() == 2) {
            sBuilder.append(String.format("%s", slotNumHex)).append(":");
        }
        if (sniNumHex.length() == 1) {
            sBuilder.append(String.format("0%s", sniNumHex));
        } else if (slotNumHex.length() == 2) {
            sBuilder.append(String.format("%s", sniNumHex));
        }
        return sBuilder.toString();
    }

    /**
     * // 通过1-4096的VLAN ID 获得2个字节组成的位图
     * 
     * @param vlanIdList
     * @return
     */
    public static String getTwiceByteBitMapFromVlanId(List<Integer> vlanIdList) {
        StringBuilder sBuilder = new StringBuilder();
        for (Integer vlanId : vlanIdList) {
            String vlanHex = flushLeft('0', 4, Integer.toHexString(vlanId));
            sBuilder.append(vlanHex.substring(0, 2)).append(":").append(vlanHex.substring(2, 4)).append(":");
        }
        return sBuilder.substring(0, sBuilder.length() - 1);
    }

    /**
     * // ONU升级的LIST 每一个ONU对应一个字节，如果选择为01 ， 不选择为00
     * 
     * @param onuList
     * @return
     */
    public static String getOnuListBitMapFromList(List<Integer> onuList) {
        StringBuilder bitMap = new StringBuilder();
        StringBuilder sBuilder = new StringBuilder();
        Integer maxValue = EponConstants.UPGRADE_ONU;
        Integer maxPort = EponConstants.UPGRADE_PORT;
        for (Integer sq : onuList) {
            if (sq > EponConstants.UPGRADE_ONU) {
                maxValue = EponConstants.UPGRADE_ONU_MAX;
                maxPort = EponConstants.UPGRADE_PORT_MAX;
            }
        }
        for (int k = 0; k < maxValue; k++) {
            if (onuList.contains(k + 1)) {
                bitMap.append("1");
            } else {
                bitMap.append("0");
            }
        }
        /*
         * for (Integer i : onuList) { // ONU的序号从1开始 bitMap.(i - 1, "1"); }
         */
        String bitString = bitMap.toString();
        for (int j = 0; j < maxPort; j++) {
            int radix = Integer.parseInt(bitString.substring(j * 8, j * 8 + 8), 2);
            String hexRadix = Integer.toHexString(radix);
            if (hexRadix.length() == 1) {
                sBuilder.append(String.format("0%s", hexRadix)).append(":");
            } else if (Integer.toHexString(radix).length() == 2) {
                sBuilder.append(String.format("%s", hexRadix)).append(":");
            }
        }
        return sBuilder.substring(0, sBuilder.length() - 1);
    }

    // ONU升级的LIST 每一个ONU对应一个字节，如果选择为01 ， 不选择为00
    public static List<Integer> getOnuListFromBitMap(String bitMap) {
        List<Integer> onuList = new ArrayList<Integer>();
        StringBuilder sBuilder = new StringBuilder();
        for (String s : bitMap.split(":")) {
            int i = Integer.parseInt(s, 16);
            int str2 = Integer.parseInt(Integer.toBinaryString(i));
            sBuilder.append(String.format("%08d", str2));
        }
        String s = sBuilder.toString();
        /*
         * if (s.indexOf("1") != -1) { int sum = s.indexOf("1"); onuList.add(sum + 1); while
         * (s.substring(s.indexOf("1") + 1).indexOf("1") >= 0) { s = s.substring(s.indexOf("1") +
         * 1); sum += s.indexOf("1") + 1; onuList.add(sum + 1); } }
         */
        while (s.indexOf("1") != -1) {
            onuList.add(s.indexOf("1") + 1);
            s = s.replaceFirst("1", "0");
        }
        return onuList;
    }

    /**
     * stp 功能中优先级与MAC地址组成的位图处理
     * 
     * @param stpGlobalSetDesignatedRoot
     * @return
     */
    public static String getStpRootFromMibString(String stpGlobalSetDesignatedRoot) {
        String[] stpByte = stpGlobalSetDesignatedRoot.split(":");
        String stpString = "";
        if (stpByte.length == 8) {
            Integer stpStep = Integer.parseInt(stpByte[0] + stpByte[1], 16);
            String stpMac = stpByte[2] + "-" + stpByte[3] + "-" + stpByte[4] + "-" + stpByte[5] + "-" + stpByte[6] + "-"
                    + stpByte[7];
            stpString = stpStep.toString() + "/" + stpMac;
        }
        return stpString;
    }

    /**
     * 获得DhcpServer的网元类型与Index的对应关系
     * 
     * @return
     */
    public static List<OltDhcpServerConfig> getOltDhcpServerConfigList() {
        List<OltDhcpServerConfig> list = new ArrayList<OltDhcpServerConfig>();
        for (int k = EponConstants.OLT_DHCP_HOST_MIN_INDEX; k <= EponConstants.OLT_DHCP_HOST_MAX_INDEX; k++) {
            OltDhcpServerConfig config = new OltDhcpServerConfig();
            // 因为设置Index在Domain处理中已经设置onuType 故可省略下步
            // config.setOnuType("HOST");
            config.setTopOltDHCPServerIndex(k);
            list.add(config);
        }
        for (int k = EponConstants.OLT_DHCP_CM_MIN_INDEX; k <= EponConstants.OLT_DHCP_CM_MAX_INDEX; k++) {
            OltDhcpServerConfig config = new OltDhcpServerConfig();
            config.setTopOltDHCPServerIndex(k);
            list.add(config);
        }
        for (int k = EponConstants.OLT_DHCP_STB_MIN_INDEX; k <= EponConstants.OLT_DHCP_STB_MAX_INDEX; k++) {
            OltDhcpServerConfig config = new OltDhcpServerConfig();
            config.setTopOltDHCPServerIndex(k);
            list.add(config);
        }
        for (int k = EponConstants.OLT_DHCP_MTA_MIN_INDEX; k <= EponConstants.OLT_DHCP_MTA_MAX_INDEX; k++) {
            OltDhcpServerConfig config = new OltDhcpServerConfig();
            config.setTopOltDHCPServerIndex(k);
            list.add(config);
        }
        for (int k = EponConstants.OLT_DHCP_DEFAULT_MIN_INDEX; k <= EponConstants.OLT_DHCP_DEFAULT_MAX_INDEX; k++) {
            OltDhcpServerConfig config = new OltDhcpServerConfig();
            config.setTopOltDHCPServerIndex(k);
            list.add(config);
        }
        return list;
    }

    public static List<OltDhcpGiaddrConfig> getDhcpGiaddrConfigList() {
        List<OltDhcpGiaddrConfig> list = new ArrayList<OltDhcpGiaddrConfig>();
        OltDhcpGiaddrConfig hostGiaddrConfig = new OltDhcpGiaddrConfig();
        hostGiaddrConfig.setOnuType("HOST");
        hostGiaddrConfig.setTopOltDHCPGiaddrIndex(EponConstants.OLT_DHCP_HOST_GIADDR_INDEX);
        OltDhcpGiaddrConfig cmGiaddrConfig = new OltDhcpGiaddrConfig();
        cmGiaddrConfig.setOnuType("CM");
        cmGiaddrConfig.setTopOltDHCPGiaddrIndex(EponConstants.OLT_DHCP_CM_GIADDR_INDEX);
        OltDhcpGiaddrConfig stbGiaddrConfig = new OltDhcpGiaddrConfig();
        stbGiaddrConfig.setOnuType("STB");
        stbGiaddrConfig.setTopOltDHCPGiaddrIndex(EponConstants.OLT_DHCP_STB_GIADDR_INDEX);
        OltDhcpGiaddrConfig mtaGiaddrConfig = new OltDhcpGiaddrConfig();
        mtaGiaddrConfig.setOnuType("MTA");
        mtaGiaddrConfig.setTopOltDHCPGiaddrIndex(EponConstants.OLT_DHCP_MTA_GIADDR_INDEX);
        OltDhcpGiaddrConfig defaultGiaddrConfig = new OltDhcpGiaddrConfig();
        defaultGiaddrConfig.setOnuType("DEFAULT");
        defaultGiaddrConfig.setTopOltDHCPGiaddrIndex(EponConstants.OLT_DHCP_DEFAULT_GIADDR_INDEX);
        list.add(hostGiaddrConfig);
        list.add(cmGiaddrConfig);
        list.add(stbGiaddrConfig);
        list.add(mtaGiaddrConfig);
        list.add(defaultGiaddrConfig);
        return list;
    }

    /**
     * 根据用户选择的指标动态生成表头
     * 
     * @param perfs
     *            指标
     * @param type
     *            性能类型
     * @return List<TableHeader>
     */
    public static List<TableHeader> getTableHeaders(JSONArray perfs, String type, String language) {
        List<TableHeader> head = new ArrayList<TableHeader>();
        for (Object perf : perfs) {
            if (perf instanceof JSONObject) {
                JSONObject o = (JSONObject) perf;
                OltPerf oltPerf = (OltPerf) JSONObject.toBean(o, OltPerf.class);
                TableHeader header = new TableHeader();
                header.setDataIndex(type + oltPerf.getPerfName());
                header.setAlign("center");
                header.setHeader(OltPerf.getPerfNameById(oltPerf.getPerfIndex(), '1', language));
                head.add(header);
            }
        }
        TableHeader timeHeader = new TableHeader();
        timeHeader.setAlign("center");
        timeHeader.setDataIndex(type + "EndTimeString");
        timeHeader.setHeader(OltPerf.getPerfNameById(0, 1, language));
        head.add(timeHeader);
        return head;
    }

    /**
     * 获得当前时间 用于加在文件名前面
     * 
     * @return
     */
    public static String getSystemDate() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.CHINESE);
        calendar.setTime(new Date());
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(month);
        sBuilder.append(day);
        sBuilder.append(hour);
        sBuilder.append(minute);
        sBuilder.append(second);
        return sBuilder.toString();
    }

    public static String getMacStringFromNoISOControl(String noISOControlString) {
        Pattern hexStringPattern = Pattern.compile("^([0-9a-fA-F]{2})([-:][0-9a-fA-F]{2})+$");
        if (!hexStringPattern.matcher(noISOControlString).matches()) {
            String result = "";
            if (isChineseCode(noISOControlString)) {
                byte[] bArray = noISOControlString.getBytes();
                for (byte b : bArray) {
                    result += Integer.toHexString(b & 0xFF) + ":";
                }
                return result.substring(0, result.length() - 1);
            } else {
                char[] charArray = noISOControlString.toCharArray();
                for (char c : charArray) {
                    result += Integer.toHexString(c) + ":";
                }
                return result.substring(0, result.length() - 1);
            }
        }
        return noISOControlString;
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

    public static String getRootPort(String stpGlobalSetRootPort) {
        String[] rootPort = stpGlobalSetRootPort.split(":");
        StringBuilder sb = new StringBuilder();
        sb.append(rootPort[1]);
        sb.append("/");
        sb.append(rootPort[2]);
        return sb.toString();

    }

    /**
     * 如果范围的BIT位图都是可见字符，此方法可将字符转为系统可用的标准十六进制String位图
     * 
     * @param s
     * @return
     */
    public static String getOctetStringFromBit(String s) {
        String result = "";
        String temp;
        for (char c : s.toCharArray()) {
            temp = Integer.toHexString(c) + ":";
            result += temp;
        }
        return result.substring(0, result.length() - 1);
    }

    /**
     * 判断字符中是否存在不是标准十六进制String的字符
     * 
     * @param string
     * @return
     */
    public static boolean isConSpeCharacters(String string) {
        final Pattern hexStringPattern = Pattern.compile("^([0-9a-fA-F]{2})([:][0-9a-fA-F]{2})+$");
        if (!hexStringPattern.matcher(string).matches()) {
            return true;
        }
        return false;
    }

    public static Integer getEponAlarmCodeByConfig(String type) {
        String hexCode = "00" + "00" + "01" + "0" + type;
        return Integer.parseInt(hexCode, 16);
    }

    /**
     * 处理索引为字符串的mib表
     * 
     * @param string
     * @return
     */
    public static String getOidFromStringWithLength(String string) {
        String oid = string.length() + ".";
        for (char a : string.toCharArray()) {
            oid += (int) a + ".";
        }
        return oid.substring(0, oid.length() - 1);
    }

    /**
     * 16进制转字符串
     * 
     * @param hex
     * @return
     */
    public static String hex2ChineseCode(String hex) {
        String[] str = hex.split(":");
        StringBuffer buffer = new StringBuffer();
        for (String s : str) {
            int i = Integer.parseInt(s, 16);
            buffer.append((char) i);
        }

        String ret = null;
        try {
            ret = new String(buffer.toString().getBytes("ISO8859-1"), "GBK");
        } catch (UnsupportedEncodingException e) {
            logger.error("", e);
            return null;
        }
        return ret;

    }

    /**
     * 通过EntityId 获取 配置文件列表
     * 
     * @deprecated
     * @param entityId
     * @return
     */
    @Deprecated
    public static List<String> getFileNameFromEntityId(Long entityId) {
        List<String> fileList = new ArrayList<String>();
        StringBuilder toSrc = new StringBuilder();
        toSrc.append(SystemConstants.ROOT_REAL_PATH);
        toSrc.append("META-INF/startConfig/");
        toSrc.append(entityId);
        File file = new File(toSrc.toString());
        if (!file.isDirectory()) {
        } else if (file.isDirectory()) {
            String[] filelist = file.list();
            for (int i = 0; i < filelist.length; i++) {
                File readfile = new File(toSrc.toString() + "\\" + filelist[i]);
                if (!readfile.isDirectory()) {
                    fileList.add(readfile.getName());
                }

            }
        }
        return fileList;
    }

    /**
     * 通过EntityId 获取 配置文件夹列表
     * 
     * @param entityId
     * @return
     */
    public static List<String> getFileFolderNameFromEntityId(Long entityId) {
        List<String> fileList = new ArrayList<String>();
        StringBuilder toSrc = new StringBuilder();
        toSrc.append(SystemConstants.ROOT_REAL_PATH);
        toSrc.append("META-INF/startConfig/");
        toSrc.append(entityId);
        File file = new File(toSrc.toString());
        if (!file.isDirectory()) {
        } else if (file.isDirectory()) {
            String[] filelist = file.list();
            for (int i = 0; i < filelist.length; i++) {
                File readfile = new File(toSrc.toString() + "\\" + filelist[i]);
                if (readfile.isDirectory()) {
                    fileList.add(readfile.getName());
                }
            }
        }
        return fileList;
    }

    /**
     * 通过EntityId 获取 配置文件列表
     * 
     * @param entityId
     * @return
     */
    public static List<String> getFileNameFromEntityId(String path) {
        List<String> fileList = new ArrayList<String>();
        StringBuilder toSrc = new StringBuilder();
        toSrc.append(SystemConstants.ROOT_REAL_PATH);
        toSrc.append("META-INF/startConfig/");
        toSrc.append(path);
        File file = new File(toSrc.toString());
        if (!file.isDirectory()) {
        } else if (file.isDirectory()) {
            String[] filelist = file.list();
            for (int i = 0; i < filelist.length; i++) {
                File readfile = new File(toSrc.toString() + "\\" + filelist[i]);
                if (!readfile.isDirectory()) {
                    fileList.add(readfile.getName());
                }
            }
        }
        return fileList;
    }

    /**
     * IGMP呼叫记录事件消息解析
     * 
     * @param str
     * @return
     */
    public static Long[] getArrayByHexString(String str) {
        String[] temp = str.split(":");
        long temp1 = 0;
        long temp2 = 0;
        temp1 = Integer.parseInt(temp[0] + temp[1] + temp[2] + temp[3], 16);
        temp2 = Integer.parseInt(temp[4] + temp[5] + temp[6] + temp[7], 16);
        Long[] result = new Long[] { temp1, temp2 };
        return result;
    }

    /**
     * 验证IP格式是否正确
     * 
     * @param text
     * @return
     */
    public static boolean matches(String text) {
        if (text != null && !text.isEmpty()) {
            String regex = "^(1\\d{2}|2[0-4 ]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
            if (text.matches(regex)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 将“1-2,3,5-9”这样格式的字符串转换为BitMap形式的 将指定位数置1，不包括在里面的置0
     * 
     * @param text
     *            字符串 格式“1-2,3,5-9”
     * @param length
     *            最大长度 应该为8的整数倍
     * @return
     */
    public static String getMapfromStr(String text, Integer length) {
        if (text == null || text.length() < 1) {
            return null;
        } else {
            char[] temp = new char[length];
            for (int i = 0; i < length; i++) {
                temp[i] = '0';
            }
            String[] textArr = text.split(",");
            for (String s : textArr) {
                String[] sArr = s.split("-");
                if (sArr.length > 3 || sArr.length < 1) {
                    return null;
                } else if (sArr.length == 1) {
                    temp[Integer.parseInt(sArr[0].trim())] = '1';
                } else if (sArr.length == 2) {
                    if (Integer.parseInt(sArr[0].trim()) > Integer.parseInt(sArr[1].trim())) {
                        return null;
                    } else {
                        if (Integer.parseInt(sArr[1].trim()) <= length - 1) {
                            for (int j = Integer.parseInt(sArr[0].trim()); j < Integer.parseInt(sArr[1].trim())
                                    + 1; j++) {
                                temp[j] = '1';
                            }
                        } else {
                            return null;
                        }
                    }
                }
            }
            String tempStr = new String(temp);
            StringBuilder mapSb = new StringBuilder();
            for (int i = 0; i <= tempStr.length() - 8; i += 8) {
                mapSb.append(Integer.toHexString(Integer.parseInt(tempStr.substring(i, i + 4), 2)));
                mapSb.append(Integer.toHexString(Integer.parseInt(tempStr.substring(i + 4, i + 8), 2)));
                mapSb.append(":");
            }
            String map = mapSb.substring(0, mapSb.toString().length() - 1);
            return map;
        }
    }

    /**
     * 从bitMap的字符串中获取字符串描述，getMapfromStr的逆运算 第0位和最后一位不进行转换
     * 
     * @param map
     * @return
     */
    public static String getStrFromMap(String map) {
        if (map == null || map.length() < 1) {
            return null;
        }
        StringBuilder bitSb = new StringBuilder();
        String[] bs = map.split(":");
        for (String s : bs) {
            String temp = "00000000" + Integer.toBinaryString(Integer.parseInt(s, 16));
            bitSb.append(temp.substring(temp.length() - 8, temp.length()));
        }
        int start = -1;
        int end = -1;
        String bitStr = bitSb.toString();
        StringBuilder descrSb = new StringBuilder();
        for (int i = 1; i < bitStr.length() - 1; i++) {
            if (start == -1 && bitStr.charAt(i) == '1') {
                start = i;
            }
            if (start != -1 && end == -1 && bitStr.charAt(i) == '0') {
                end = i - 1;
                if (start == end) {
                    descrSb.append(start);
                    descrSb.append(",");
                } else {
                    descrSb.append(start);
                    descrSb.append("-");
                    descrSb.append(end);
                    descrSb.append(",");
                }
                start = -1;
                end = -1;
            }
        }
        if (start != -1) {
            end = bitStr.length() - 2;
            descrSb.append(start);
            descrSb.append("-");
            descrSb.append(end);
            descrSb.append(",");
        }
        return descrSb.substring(0, descrSb.length() - 1);
    }

    public static List<Integer> getWanWorkMode(String tmpHex) {
        List<Integer> result = new ArrayList<>();
        int tmp = Integer.parseInt(tmpHex, 16);
        for (int i = 1; i <= 8; i++) {
            if ((tmp & 0x1) == 1) {
                result.add(i);
            }
            tmp = tmp >> 1;
        }
        return result;
    }

    /**
     * onu wan BindIf 信息位图转换 00000000 00000000 0000 0 0 0 0 0 0 0 0 0 0 0 0 lan8 lan7 lan6 lan5 lan4
     * lan3 lan2 lan1 ssid4 ssid3 ssid2 ssid1
     * 
     * V1.10.0版本有变动，采用一个字节表示ssid1(0), ssid2(1), ssid3(2), ssid4(3), lan1(4), lan2(5), lan3(6),
     * lan4(7),
     * 
     * @param tmpHex
     * @return
     */
    public static List<Integer> getWanBandIfList(String tmpHex) {
        List<Integer> r = new ArrayList<Integer>();
        if (tmpHex.length() == 2) {
            int t = Integer.parseInt(tmpHex, 16);
            for (int i = 8; i >= 1; i--) {
                if ((t & 0x1) == 1) {
                    r.add(i);
                }
                t = t >> 1;
            }
            Collections.sort(r);  
        } else {
            String[] tmpBytes = tmpHex.split(":");
            int intHigh = Integer.parseInt(tmpBytes[2], 16);
            int intLow = Integer.parseInt(tmpBytes[3], 16);
            for (int i = 1; i <= 8; i++) {
                if ((intLow & 0x1) == 1) {
                    r.add(i);
                }
                intLow = intLow >> 1;
            }
            for (int i = 9; i <= 16; i++) {
                if ((intHigh & 0x1) == 1) {
                    r.add(i);
                }
                intHigh = intHigh >> 1;
            }
        }
        return r;
    }

    /**
     * onu wan BindIf 信息位图转换 00000000 00000000 0000 0 0 0 0 0 0 0 0 0 0 0 0 lan8 lan7 lan6 lan5 lan4
     * lan3 lan2 lan1 ssid4 ssid3 ssid2 ssid1 V1.10.0.0版本之后，只采用一个字节来标识
     * 
     * @param r
     * @return
     */
    public static String getWanBandIfMibString(List<Integer> r, Boolean f) {
        if (r == null || r.size() == 0) {
            if (f) {
                return "00";
            }
            return "00:00:00:00";
        }
        String tmpHex;
        String b ;
        
        if (f) {
            b = "00000000";
            for (int i = 1; i < 9; i++) {
                if (r.contains(i)) {
                    b = b + "1";
                } else {
                    b = b + "0";
                }
            }
            tmpHex = flushLeft('0', 2, Integer.toHexString(Integer.parseInt(b.substring(8, 16), 2)));
        } else {
            b = "0000";
            for (int i = 12; i > 0; i--) {
                if (r.contains(i)) {
                    b = b + "1";
                } else {
                    b = b + "0";
                }
            }
            tmpHex = "00:00:" + flushLeft('0', 2, Integer.toHexString(Integer.parseInt(b.substring(0, 8), 2))) + ":"
                    + flushLeft('0', 2, Integer.toHexString(Integer.parseInt(b.substring(8, 16), 2)));
        }
        return tmpHex;
    }

    /**
     * 根据MTK ONU全局开关位图解出开关
     * 
     * @param hex
     * @return
     */
    public static Integer getOnuGlobalCfgMgmt(String tmpHex) {
        int s = 0;
        String mgmt = Integer.toBinaryString(Integer.parseInt(tmpHex, 16));
        if (mgmt.length() < 8) {
            return s;
        } else {
            s = Integer.parseInt(mgmt.substring(0, 1));
        }
        return s;
    }

    /**
     * 根据MTK ONU全局开关组成位图
     * 
     * @param v
     * @return
     */
    public static String getOnuGlobalCfgMgmtMibValue(Integer v) {
        String mibValue = "";
        switch (v) {
        case 0:
            mibValue = "00";
            break;
        case 1:
            mibValue = "80";
            break;
        default:
            break;
        }
        return mibValue;
    }

    /**
     * 获取ONU WLAN能力
     * 
     * @param tmpHex
     * @return
     */
    public static Integer getWlanCapability(String tmpHex) {
        String[] tmpBytes = tmpHex.split(":");
        int wlanCapobility = Integer.parseInt(tmpBytes[6], 16);
        return wlanCapobility;
    }

    /**
     * 获取ONU CATV能力
     * 
     * @param tmpHex
     * @return
     */
    public static Integer getCatvCapability(String tmpHex) {
        String[] tmpBytes = tmpHex.split(":");
        int catvCapobility = Integer.parseInt(tmpBytes[8], 16);
        return catvCapobility;
    }

    /**
     * 获得理论上的下一个onu的Index，适用于单个onu的刷新过程
     * 
     * @param cmcIndex
     * @return
     */
    public static Long getNextOnuIndex(Long onuIndex) {
        return onuIndex + 0x00010000;
    }

    /**
     * OLT的super密码
     * 
     * @param date_year
     *            年
     * @param date_mnth
     *            月
     * @param date_day
     *            日
     * @return 数字
     */
    public static long pwdGetByDate(int date_year, int date_mnth, int date_day) {
        int date = 0;
        int year = date_year, mnth = date_mnth, day = date_day;
        year = year * 17; /* 34221-- */
        mnth = (mnth + 12) * 19; /* 247--456 */
        day = day + 23; /* 24--54 */
        date = year * 1000 + mnth * 100 + day * (day + 29) * 341 - 7;
        return date;
    }

    /**
     * OLT当天的super密码
     * 
     * @return 数字
     */
    public static long pwdGetByDate() {
        Calendar today = Calendar.getInstance();
        int date = 0;
        int year = today.get(Calendar.YEAR), mnth = today.get(Calendar.MONTH) + 1,
                day = today.get(Calendar.DAY_OF_MONTH);
        year = year * 17; /* 34221-- */
        mnth = (mnth + 12) * 19; /* 247--456 */
        day = day + 23; /* 24--54 */
        date = year * 1000 + mnth * 100 + day * (day + 29) * 341 - 7;
        return date;
    }

    public static String getIpAddressFormByte(byte ip1, byte ip2, byte ip3, byte ip4) {
        StringBuilder sb = new StringBuilder();
        sb.append(ip1 & 0xFF).append(".").append(ip2 & 0xFF).append(".").append(ip3 & 0xFF).append(".")
                .append(ip4 & 0xFF);
        return sb.toString();
    }

    public static boolean isValidBoardTemp(Integer boardTemp) {
        if (boardTemp > -40 && boardTemp < 125) {
            return true;
        }
        return false;
    }

    /**
     * 将Long类型表示的时间转换为设备支持的DateAndTime格式
     * 
     * @param dateTime
     * @return
     */
    public static String parseToDateAndTimeFormat(Long dateTime) {
        if (dateTime == null) {
            return null;
        }
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(dateTime);
        int year = time.get(Calendar.YEAR);
        String formatStr = new OctetString(new byte[] { (byte) ((year & 0xFF00) >> 8), (byte) (year & 0xFF),
                (byte) (time.get(Calendar.MONTH) + 1), (byte) time.get(Calendar.DAY_OF_MONTH),
                (byte) time.get(Calendar.HOUR_OF_DAY), (byte) time.get(Calendar.MINUTE),
                (byte) time.get(Calendar.SECOND), (byte) time.get(Calendar.MILLISECOND) }).toString();
        return formatStr;
    }

    /**
     * Version Compare
     * 
     * @param deviceVersion
     * @param supportVersion
     * @return
     */
    public static boolean versionAvaiable(String deviceVersion, String supportVersion) {
        if (deviceVersion.contains("V")) {
            deviceVersion = deviceVersion.substring(deviceVersion.indexOf("V") + 1);
        }
        if (deviceVersion.contains("-")) {
            deviceVersion = deviceVersion.substring(0, deviceVersion.indexOf("-"));
        }
        String[] dvarray = deviceVersion.split("\\.");
        String[] svarray = supportVersion.split("\\.");
        int count = Math.max(dvarray.length, svarray.length);
        for (int i = 0; i < count; i++) {
            if (i >= dvarray.length) {
                return false;
            } else if (i >= svarray.length) {
                return true;
            } else if (Integer.parseInt(dvarray[i]) == Integer.parseInt(svarray[i])) {
                continue;
            } else {
                if (Integer.parseInt(dvarray[i]) - Integer.parseInt(svarray[i]) > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isChineseCode(String value) {
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            // 可打印的英文字符
            if (!((Character.isISOControl(c) || ((c & 0xFF) >= 0x80)) && (!Character.isWhitespace(c)))) {
                continue;
            }
            // 中文的范围是：\u4E00-\u9FA5

            if (!((c >= '\u4E00') && (c <= '\u9FA5'))) {
                return false;
            }
        }
        return true;

    }

    public static String getGponSrvProfilePortVlanAggrVlanListString(String vlanBytes) {
        StringBuilder result = new StringBuilder();
        String[] str = vlanBytes.split(":");
        for (int i = 0; i < str.length; i = i + 2) {
            result.append(Integer.valueOf(str[i] + str[i + 1], 16)).append(",");
        }
        return result.substring(0, result.length() - 1);
    }

    public static String getGponSrvProfilePortVlanAggrVlanListBytes(String vlanString) {
        String[] str = vlanString.split(",");
        StringBuilder sBuilder = new StringBuilder();
        for (String tmp : str) {
            String s = EponUtil.flushLeft('0', 4, Integer.toHexString(Integer.parseInt(tmp)));
            sBuilder.append(s.substring(0, 2)).append(":").append(s.substring(2, 4)).append(":");

        }
        return sBuilder.substring(0, sBuilder.length() - 1);
    }

    public static List<Integer> getRogueOnuFromMib(String rogueBitMap) {
        List<Integer> rogueList = new ArrayList<Integer>();
        if (rogueBitMap.length() == 95) {
            String result = "";
            for (String s : rogueBitMap.split(":")) {
                int i = Integer.parseInt(s, 16);
                int str2 = Integer.parseInt(Integer.toBinaryString(i));
                result += String.format("%08d", str2);
            }
            if (result.indexOf("1") != -1) {
                int sum = result.indexOf("1");
                rogueList.add(sum);
                while (result.substring(result.indexOf("1") + 1).indexOf("1") >= 0) {
                    result = result.substring(result.indexOf("1") + 1);
                    sum += result.indexOf("1") + 1;
                    rogueList.add(sum);
                }
            }
        } else {
            rogueList = null;
        }
        return rogueList;
    }

    public static String getRogueBitMapFormList(List<Integer> list) {
        StringBuilder rogueOnuMapBuilder = new StringBuilder(String.format("%0256d", 0));
        for (int i : list) {
            rogueOnuMapBuilder.setCharAt(i - 1, '1');
        }
        StringBuilder rogueOnuInHex = new StringBuilder();
        for (int i = 8; i <= rogueOnuMapBuilder.length();) {
            String index = rogueOnuMapBuilder.substring(i - 8, i);
            i = i + 8;
            int k = Integer.parseInt(index, 2); // 转成10进制
            if (Integer.toHexString(k).length() == 1) {
                rogueOnuInHex.append(String.format("0%s", Integer.toHexString(k))).append(":");
            } else if (Integer.toHexString(k).length() == 2) {
                rogueOnuInHex.append(String.format("%s", Integer.toHexString(k))).append(":");
            }

        }
        return rogueOnuInHex.substring(0, rogueOnuInHex.length() - 1);
    }
}
