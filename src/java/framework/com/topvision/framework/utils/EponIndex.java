/***********************************************************************
 * $Id: EponIndex.java,v1.0 2011-9-28 上午08:38:44 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.utils;

import java.util.List;

/**
 * epon系统中索引处理。
 * 
 * epon采用5级索引来定位。 示例： 0x0203150103=8641642755L 表示slot 2，pon 3， onu 15，onu slot 1（sfu默认为1），uni 3.
 * 
 * 从slot，pon，onu，onu slot，uni对应的No号，后面为0的不用填写
 * 
 * <code>          
 *   EponIndex index = new EponIndex(2);//slot 2
 *   assertEquals(index.getUniIndex().longValue(), 8589934592L);//
 *   index = new EponIndex(2, 3);//2号槽，3端口
 *   assertEquals(index.getUniIndex().longValue(), 8640266240L);
 *   index = new EponIndex(2, 3, 21);//2号槽，Pon 3，21号ONU
 *   assertEquals(index.getUniIndex().longValue(), 8641642496L);
 *   index = new EponIndex(2, 3, 21, 1, 3);//2号槽，Pon 3，21号ONU，3号端口
 * </code>
 * 
 * @author Victor
 * @created @2011-9-28-上午08:38:44
 * 
 */
public class EponIndex {
    private Long index;

    /**
     * 根据位置号来定义Index
     * 
     * @param id
     *            数组表示，依次为slot，pon，onu，onu slot，uni，后面没有的可以不用填写
     */
    public EponIndex(Integer... id) {
        if (id == null || id.length == 0) {
            return;
        }
        index = 0L;
        for (int i = 0; i < id.length; i++) {
            index += (id[i].longValue() << ((4 - i) * 8));
        }
    }

    /**
     * @see EponIndex(Integer... id)
     */
    public EponIndex(String... id) {
        if (id == null || id.length == 0) {
            return;
        }
        index = 0L;
        for (int i = 0; i < id.length; i++) {
            index += (Long.parseLong(id[i]) << ((4 - i) * 8));
        }
    }

    /**
     * @return 槽位索引值
     */
    public Long getSlotIndex() {
        return index.longValue() & 0xFF00000000L;
    }

    /**
     * @return 槽位号
     */
    public Long getSlotNo() {
        return (index.longValue() & 0xFF00000000L) >> 32;
    }

    /**
     * @return Pon索引值
     */
    public Long getPonIndex() {
        return index.longValue() & 0xFFFF000000L;
    }

    /**
     * @return Pon端口号
     */
    public Long getPonNo() {
        return (index.longValue() & 0x00FF000000L) >> 24;
    }

    /**
     * @return 上联口索引值
     */
    public Long getSniIndex() {
        return index.longValue() & 0xFFFF000000L;
    }

    /**
     * @return 上联口端口号
     */
    public Long getSniNo() {
        return (index.longValue() & 0x00FF000000L) >> 24;
    }

    /**
     * @return onu的索引值
     */
    public Long getOnuIndex() {
        return index.longValue() & 0xFFFFFF0000L;
    }

    /**
     * @return onu的LLID
     */
    public Long getOnuNo() {
        return (index.longValue() & 0x0000FF0000L) >> 16;
    }

    /**
     * @return uni的索引值
     */
    public Long getUniIndex() {
        return index.longValue() & 0xFFFFFFFFFFL;
    }

    /**
     * @return onu的端口号
     */
    public Long getUniNo() {
        return index.longValue() & 0x00000000FFL;
    }

    /**
     * @return 槽位索引值
     */
    public static Long getSlotIndex(Long index) {
        return index.longValue() & 0xFF00000000L;
    }

    /**
     * @return 槽位索引值
     */
    public static Long getSlotIndex(Integer... id) {
        if (id == null || id.length == 0) {
            return null;
        }
        Long index = 0L;
        for (int i = 0; i < id.length; i++) {
            index += (id[i].longValue() << ((4 - i) * 8));
        }
        return index.longValue() & 0xFF00000000L;
    }

    /**
     * @return 槽位索引值
     */
    public static Long getSlotIndex(String... id) {
        if (id == null || id.length == 0) {
            return null;
        }
        Long index = 0L;
        for (int i = 0; i < id.length; i++) {
            index += (Long.parseLong(id[i]) << ((4 - i) * 8));
        }
        return index.longValue() & 0xFF00000000L;
    }

    /**
     * @return 槽位号
     */
    public static Long getSlotNo(Long index) {
        return (index.longValue() & 0xFF00000000L) >> 32;
    }

    /**
     * @return Pon索引值
     */
    public static Long getPonIndex(Long index) {
        return index.longValue() & 0xFFFF000000L;
    }

    /**
     * @return Pon索引值
     */
    public static Long getPonIndex(Integer... id) {
        if (id == null || id.length == 0) {
            return null;
        }
        Long index = 0L;
        for (int i = 0; i < id.length; i++) {
            index += (id[i].longValue() << ((4 - i) * 8));
        }
        return index.longValue() & 0xFFFF000000L;
    }

    /**
     * @return Pon索引值
     */
    public static Long getPonIndex(String... id) {
        if (id == null || id.length == 0) {
            return null;
        }
        Long index = 0L;
        for (int i = 0; i < id.length; i++) {
            index += (Long.parseLong(id[i]) << ((4 - i) * 8));
        }
        return index.longValue() & 0xFFFF000000L;
    }

    /**
     * 解析SNI/PON端口的index,sourceIndex格式如下：0X01:03:01:00 其中第一段表示设备号，第二段表示槽位号，第三段表示端口号， 00表示onu
     * LLID（一般不用）
     * 
     * @param sourceIndex
     * @return
     */
    public static Long getPortIndex(String sourceIndex) {
        if (sourceIndex == null || sourceIndex.length() == 0) {
            return null;
        }
        String[] id = sourceIndex.split(":");
        Long index = 0L;
        for (int i = 1; i < id.length; i++) {
            index += (Long.parseLong(id[i], 16) << ((4 - i + 1) * 8));
        }
        return index;
    }

    /**
     * 将一个long型的index转成0X01:03:01:00格式
     * 
     * @param index
     * @return
     */
    public static String getPortIndex(Long index) {
        String slot = Long.toHexString(getSlotNo(index));
        slot = slot.length() == 1 ? String.format("0%S", slot) : String.format("0%S", slot);
        String port = Long.toHexString(getPonNo(index));
        port = port.length() == 1 ? String.format("0%S", port) : String.format("0%S", port);
        String onu = Long.toHexString(getOnuNo(index));
        onu = onu.length() == 1 ? String.format("0%S", onu) : String.format("0%S", onu);
        return String.format("01:%S:%S:%S", slot, port, onu);
    }

    /**
     * 将一个long型的index转成0X01:03:01:00:00的格式
     * 
     * @param index
     * @return
     */
    public static String getOnuPonIndex(Long index) {
        StringBuilder sb = new StringBuilder(Long.toHexString(index));
        int count = 10 - sb.length();
        // 补全为10位
        for (int i = 0; i < count; i++) {
            sb = sb.insert(0, "0");
        }
        String str = sb.substring(0, 2) + ":" + sb.substring(2, 4) + ":" + sb.substring(4, 6) + ":"
                + sb.substring(6, 8) + ":" + sb.substring(8, 10);

        return str;
    }

    /**
     * @return Pon端口号
     */
    public static Long getPonNo(Long index) {
        return (index.longValue() & 0x00FF000000L) >> 24;
    }

    /**
     * @return 上联口索引值
     */
    public static Long getSniIndex(Long index) {
        return index.longValue() & 0xFFFF000000L;
    }

    /**
     * @return 上联口索引值
     */
    public static Long getSniIndex(Integer... id) {
        if (id == null || id.length == 0) {
            return null;
        }
        Long index = 0L;
        for (int i = 0; i < id.length; i++) {
            index += (id[i].longValue() << ((4 - i) * 8));
        }
        return index.longValue() & 0xFFFF000000L;
    }

    /**
     * @return 上联口索引值
     */
    public static Long getSniIndex(String... id) {
        if (id == null || id.length == 0) {
            return null;
        }
        Long index = 0L;
        for (int i = 0; i < id.length; i++) {
            if (id[i].equalsIgnoreCase("ff")) {
                id[i] = "0";
            }
            index += (Long.parseLong(id[i]) << ((4 - i) * 8));
        }
        return index.longValue() & 0xFFFF000000L;
    }

    /**
     * @return 上联口端口号
     */
    public static Long getSniNo(Long index) {
        return (index.longValue() & 0x00FF000000L) >> 24;
    }

    /**
     * @return onu的索引值
     */
    public static Long getOnuIndex(Long index) {
        return index.longValue() & 0xFFFFFF0000L;
    }

    /**
     * @return onu的索引值
     */
    public static Long getOnuIndex(Integer... id) {
        if (id == null || id.length == 0) {
            return null;
        }
        Long index = 0L;
        for (int i = 0; i < id.length; i++) {
            index += (id[i].longValue() << ((4 - i) * 8));
        }
        return index.longValue() & 0xFFFFFF0000L;
    }

    /**
     * @return onu的索引值
     */
    public static Long getOnuIndex(String... id) {
        if (id == null || id.length == 0) {
            return null;
        }
        Long index = 0L;
        for (int i = 0; i < id.length; i++) {
            index += (Long.parseLong(id[i]) << ((4 - i) * 8));
        }
        return index.longValue() & 0xFFFFFF0000L;
    }

    /**
     * @return onu的LLID
     */
    public static Long getOnuNo(Long index) {
        return (index.longValue() & 0x0000FF0000L) >> 16;
    }

    /**
     * @return uni的索引值
     */
    public static Long getUniIndex(Long index) {
        return index.longValue() & 0xFFFFFFFFFFL;
    }

    /**
     * @return uni的索引值
     */
    public static Long getUniIndex(Integer... id) {
        if (id == null || id.length == 0) {
            return null;
        }
        Long index = 0L;
        for (int i = 0; i < id.length; i++) {
            index += (id[i].longValue() << ((4 - i) * 8));
        }
        return index.longValue() & 0xFFFFFFFFFFL;
    }

    /**
     * @return uni的索引值
     */
    public static Long getUniIndex(String... id) {
        if (id == null || id.length == 0) {
            return null;
        }
        Long index = 0L;
        for (int i = 0; i < id.length; i++) {
            index += (Long.parseLong(id[i]) << ((4 - i) * 8));
        }
        return index.longValue() & 0xFFFFFFFFFFL;
    }

    /**
     * @return onu的端口号
     */
    public static Long getUniNo(Long index) {
        return index.longValue() & 0x00000000FFL;
    }

    public static Long getOnuIndexByMibIndex(Long index) {
        return (index.longValue() & 0x00FFFFFFL) << 16;
    }

    public static Long getOnuMibIndexByIndex(Long index) {
        return ((index.longValue() & 0xFFFFFF0000L) >> 16) | 0x01000000L;
    }

    // 由于ONUPON口的Index会与ONU的Index完全相同，故此处做特殊处理
    public static Long getOnuPonIndexByMibIndex(Long onuNo, Long onuCardNo, Long onuPonNo) {
        return (((onuNo.longValue() & 0x00FFFFFFL) << 16) | (onuCardNo << 8)) | 0xFF;
    }

    // 增加判断 自动识别onuPon口
    public static Long getUniIndexByMibIndex(Long onuNo, Long onuCardNo, Long uniNo) {
        if (uniNo == 0) {
            return (((onuNo.longValue() & 0x00FFFFFFL) << 16) | (onuCardNo << 8)) | 0xFF;
        }
        return (((onuNo.longValue() & 0x00FFFFFFL) << 16) | (onuCardNo << 8)) | uniNo;
    }

    public static Long getOnuCardIndex(Long index) {
        return index.longValue() & 0xFFFFFFFF00L;
    }

    public static Long getOnuCardNo(Long index) {
        return (index.longValue() & 0x000000FF00L) >> 8;
    }

    public static Long getSlotMibIndexByIndex(Long index) {
        return ((index.longValue() & 0xFF00000000L) >> 16) | 0x01000000L;
    }

    public static Long getOnuNoByMibDeviceIndex(Long index) {
        return index.longValue() & 0x000000FFL;
    }

    public static Long getOnuNoByPortIndex(Long index) {
        return (index.longValue() & 0x0000FF0000L) >> 16;
    }

    /**
     * 
     * 
     * @param index
     * @return
     */
    public static Long getPonIndexByMibDeviceIndex(Long index) {
        return (index.longValue() & 0xFFFFFFL) << 16;
    }

    public static Long getAclPortIndexByMibDeviceIndex(Long index) {
        return (index.longValue() & 0xFFFFFF00L) << 8 | (index.longValue() & 0xFF);
    }

    // 获得ACL的PON Index 方法同getMibSniRedirectIndexBySniIndex
    public static Long getAclDeviceIndexByPonIndex(Long index) {
        return (index.longValue() & 0xFFFF000000L) >> 8;
    }

    public static Long getAclDeviceIndexByUniIndex(Long index) {
        return (index.longValue() & 0xFFFFFF0000L) >> 8 | (index.longValue() & 0xFF);
    }

    public static Long getSniIndexByMibSniRedirectIndex(Long index) {
        return (index.longValue() & 0xFFFFFFFFL) << 8;
    }

    public static Long getMibSniRedirectIndexBySniIndex(Long index) {
        return (index.longValue() & 0xFFFF000000L) >> 8;
    }

    public static Long getMibPonIndexIndexByPonIndex(Long index) {
        return ((index.longValue() & 0xFFFF000000L) >> 16) | 0x01000000L;
    }

    public static Long getAuthenOnuIndex(Long ponIndex, Integer onuNo) {
        return (ponIndex.longValue() & 0xFFFF000000L) | (onuNo << 16);
    }

    // 对于系统index onupon位为0
    public static Long getInstanceIndexByAlertIndex(Long index) {
        return ((index.longValue() & 0xFFFFFF00L) << 8) | ((index.longValue() & 0xFFL));
    }

    public static Long getAlertIndexByInstanceIndex(Long index) {
        return ((index.longValue() & 0xFFFFFF0000L) >> 8) | (index.longValue() & 0xFFL);
    }

    /**
     * 根据Trap发送的instance解析成系统内部的Index
     * 
     * @param instance
     *            trap的instance
     * @return 系统内部Index
     */
    public static Long getIndex(String[] instance) {
        return (Long.parseLong(instance[1], 16) << 32) + (Long.parseLong(instance[2], 16) << 24)
                + (Long.parseLong(instance[3], 16) << 16) + Long.parseLong(instance[5], 16);
    }

    public static Long getUniIndex(Long slotNo, Long ponNo, Long onuNo, Long uniNo) {
        return (slotNo << 32) + (ponNo << 24) + (onuNo << 16) + uniNo;
    }

    public static Long getUniIndex(Integer slotNo, Integer ponNo, Integer onuNo, Integer uniNo) {
        return (slotNo.longValue() << 32) + (ponNo.longValue() << 24) + (onuNo.longValue() << 16) + uniNo.longValue();
    }

    /**
     * 找到端口同一芯片上的另个端口的index
     * 
     * @param index
     * @return
     */
    public static long getChipPortIndex(long index) {
        long loc = EponIndex.getPonNo(index);
        if (loc % 2 == 0L) { // 2,4,6,8
            index -= Long.parseLong("0001000000", 16);
        } else {// 1,3,5,7
            index += Long.parseLong("0001000000", 16);
        }
        return index;
    }

    /**
     * 通过PON口Index（对SNI端口同样适用）获得端口的slot和端口号组成的字符串
     * 
     * @param index
     * @return
     */
    public static StringBuilder getPortStringByIndex(Long index) {
        StringBuilder location = new StringBuilder();
        Long slotNum = getSlotNo(index);
        Long portNum = getPonNo(index);
        return location.append(slotNum.toString()).append("/").append(portNum.toString());
    }

    /**
     * 通过ONU index 获得有槽位、端口和位置组成的字符串
     * 
     * @param index
     * @return
     */
    public static StringBuilder getOnuStringByIndex(Long index) {
        StringBuilder location = new StringBuilder();
        Long slotNum = getSlotNo(index);
        Long portNum = getPonNo(index);
        Long onuNum = getOnuNo(index);
        return location.append(slotNum.toString()).append("/").append(portNum.toString()).append(":")
                .append(onuNum.toString());
    }

    public static String getCcmtsStringByIndex(Long index) {
        StringBuilder location = new StringBuilder();
        Long slotNum = getSlotNo(index);
        Long portNum = getPonNo(index);
        Long onuNum = getOnuNo(index);
        return location.append(slotNum.toString()).append("/").append(portNum.toString()).append("/")
                .append(onuNum.toString()).toString();
    }

    /**
     * 通过UNI index 获得有槽位、端口、ONU和位置组成的字符串
     * 
     * @param index
     * @return
     */
    public static StringBuilder getUniStringByIndex(Long index) {
        StringBuilder location = new StringBuilder();
        Long slotNum = getSlotNo(index);
        Long portNum = getPonNo(index);
        Long onuNum = getOnuNo(index);
        Long uniNum = getUniNo(index);
        return location.append(slotNum.toString()).append("/").append(portNum.toString()).append(":")
                .append(onuNum.toString()).append(":").append(uniNum.toString());
    }

    // 获得ONU的串口服务器Index,注意第四个字节为FF
    public static Long getOnuComIndex(Integer slotNo, Integer ponNo, Integer onuNo, Integer comNo) {
        Long index = 0L;
        index = (slotNo.longValue() << 32) + (ponNo.longValue() << 24) + (onuNo << 16) + comNo;
        return (index.longValue() & 0xFFFFFF00FFL) | 0xFF00L;
    }

    /**
     * 从数组中找出第一个未使用的整数
     * 
     * @param indexs
     * @return
     */
    public static Integer generateIndex(List<Integer> indexs) {
        int index = 1;
        if (indexs != null && indexs.size() > 0) {
            int i = 0;
            for (; i < indexs.size(); i++) {
                if (index < indexs.get(i) - 1) {
                    return index;
                } else {
                    index = indexs.get(i);
                }
            }
            index = indexs.get(indexs.size() - 1);
        }
        return index;
    }

    public static Long getOnuIndexFromCmcIndex(Long cmcIndex) {
        return ((cmcIndex & 0xF8000000) << 5) | ((cmcIndex & 0x7800000) << 1) | (cmcIndex & 0x7F0000);
    }

    public static Long getOnuIndexFromOnuIndexAdd64(Long onuIndex) {
        return onuIndex + 0x0000400000;
    }

    public static Long getPonIndexFromOnuIndexInInterface(Long onuIndex) {
        return onuIndex & 0xFF800000;
    }

    /**
     * Add by Rod
     * 
     * @param onuMibIndex
     * @return
     */
    public static Long getCmcIndexByOnuMibIndex(Long onuMibIndex) {
        Long onuIndex = getOnuIndexByMibIndex(onuMibIndex);
        if ((onuIndex & 0xF0000000L) > 0) {
            onuIndex = onuIndex & 0xFFFFFFF;
        }
        if (((onuIndex & 0xFF0000L) >> 16) > 64) {
            return ((onuIndex & 0xFF00000000L) >> 5) | ((onuIndex & 0xFF000000L) >> 1)
                    | ((onuIndex & 0xFF0000L) - 0x400000);
        } else {
            return ((onuIndex & 0xFF00000000L) >> 5) | ((onuIndex & 0xFF000000L) >> 1) | (onuIndex & 0xFF0000L);
        }
    }

    /**
     * 解析index为 SLOT/PORT:ONU:UNI的格式
     * @param index
     * @return
     */
    public static String getStringFromIndex(Long index) {
        if (getUniNo(index) > 0) {
            return getUniStringByIndex(index).toString();
        } else if (getOnuNo(index) > 0) {
            return getOnuStringByIndex(index).toString();
        } else if (getPonNo(index) > 0) {
            return getPortStringByIndex(index).toString();
        } else if (getSlotNo(index) > 0) {
            return getSlotNo(index).toString();
        }
        throw new RuntimeException();
    }

    /**
     * 根据ONU端口Index获取ONU端口的DeviceIndex
     * <pre>
     * 31-24 type  23-16 slot 15-8 port  7-0 onuId
     * typedef enum
     *   {
     *      PerfStatType_OLT_ETH= 0,
     *      PerfStatType_OLT_PON,
     *      PerfStatType_ONU_PON,
     *      PerfStatType_ONU_UNI,
     *      PerfStatType_OLT_TEMP,
     *      PerfStatType_ONU_TEMP,
     *      PerfStatType_OLT_CPU,
     *      PerfStatType_SIMULATOR,
     *      PerfStatType_MAX
     *   }PerfStatType_E
     * <pre>
     * @param index
     * @param typeFlag
     * @return
     */
    public static Long getOnuPortDeviceIndex(Long index, long typeFlag) {
        long slotNo = EponIndex.getSlotNo(index);
        long ponNo = EponIndex.getPonNo(index);
        long onuNo = EponIndex.getOnuNo(index);
        return (typeFlag << 24) + (slotNo << 16) + (ponNo << 8) + onuNo;
    }

    public static Long getPortDeviceIndex(Long index, long typeFlag) {
        long slotNo = EponIndex.getSlotNo(index);
        long portNo = EponIndex.getPonNo(index);
        return (typeFlag << 24) + (slotNo << 16) + (portNo << 8);
    }

    /**
     * 通过ONU index 获得有槽位、端口和位置组成的字符串
     * ONU告警处理的格式如2/6/1
     * @param index
     * @return
     */
    public static String getOnuAlertSourceFormat(Long index) {
        StringBuilder location = new StringBuilder();
        Long slotNum = getSlotNo(index);
        Long portNum = getPonNo(index);
        Long onuNum = getOnuNo(index);
        return location.append(slotNum.toString()).append("/").append(portNum.toString()).append("/")
                .append(onuNum.toString()).toString();
    }

    /**
     * 将十六进制字符转化为文本
     * @param s
     * @return
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
     * OLT设备portIndex转换为ifIndex,针对PN8602-E设备16端口特殊处理
     * @param s
     * @return
     */
    public static Long portIndexToIfIndex(Long portIndex) {
        if ((portIndex & 0xFF000000L) >> 24 > 16) {
            return ((portIndex & 0x1F000000L) >> 1 | 0x02);
        } else if ((portIndex & 0xFF000000L) >> 24 == 16) {
            return (portIndex & 0xFF00000000L) >> 5;
        } else {
            return ((portIndex & 0xFF00000000L) >> 5) | ((portIndex & 0xFF000000L) >> 1);
        }
    }
}
