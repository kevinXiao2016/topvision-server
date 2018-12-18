/***********************************************************************
 * $Id: OnuCpeUtil.java,v1.0 2016年7月13日 上午9:39:24 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onucpe.facade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.epon.performance.domain.GponOnuUniCpeList;
import com.topvision.ems.epon.performance.domain.OnuCpe;
import com.topvision.ems.epon.performance.domain.OnuCpeIpInfo;
import com.topvision.ems.epon.performance.domain.OnuUniCpeCount;
import com.topvision.ems.epon.performance.domain.OnuUniCpeList;
import com.topvision.ems.epon.performance.exception.DataFormatErrorException;
import com.topvision.ems.epon.performance.exception.EndParseException;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Bravin
 * @created @2016年7月13日-上午9:39:24
 *
 */
public class OnuCpeUtil {
    private static final Logger logger = LoggerFactory.getLogger(OnuCpeUtil.class);
    private static final int MAX_EPON_CPE_COUNT = 64;
    private static final int MAX_GPON_CPE_COUNT = 256;
    private static final int ALLOCATE_SIZE_BY_EPON_CPE = 24;
    private static final int ALLOCATE_SIZE_BY_GPON_CPE = 18;
    private static final int EPON_UNI_MAC_INFO_TOTAL_SIZE = 768;
    private static final int GPON_UNI_MAC_INFO_TOTAL_SIZE = 2304;
    private static final int EPON_MAC_SIZE = 12;
    private static final int GPON_MAC_SIZE = 12;
    private static final int EPON_TYPE_SIZE = 4;
    private static final int GPON_TYPE_SIZE = 2;
    private static final int EPON_VLAN_SIZE = 8;
    private static final int GPON_VLAN_SIZE = 4;
    private static final int ONU_CPE_IPINFO_SIZE = 11;

    public static Long getUniIndex(OnuUniCpeList onuUniCpeList) {
        Long slot = onuUniCpeList.getInfoSlotIndex();
        Long pon = onuUniCpeList.getInfoPortIndex();
        Long onu = onuUniCpeList.getInfoOnuIndex();
        Long uni = onuUniCpeList.getInfoUniIndex();
        return EponIndex.getUniIndex(slot, pon, onu, uni);
    }

    /**
     * 解析ONU CPE终端信息：
     * 变长数据，每条记录包括11个字节，第0到3字节是IP地址，第4到9字节是MAC地址，第10个字节是终端类型，最多支持256条记录
     * @param message
     * @return
     */
    public static List<OnuCpeIpInfo> parseOnuCpeIpInfoList(String message) {
        List<OnuCpeIpInfo> list = new ArrayList<OnuCpeIpInfo>();

        if ("noSuchInstance".equalsIgnoreCase(message)) {
            return list;
        }
        String[] array = message.split(":");
        int length = array.length;
        if (length == 0) {
            return list;
        }
        /*if (length % ONU_CPE_IPINFO_SIZE != 0) {
            throw new OnuCpeIpInfoWrongSizeException();
        }*/
        int cpeSize = length / ONU_CPE_IPINFO_SIZE;
        for (int i = 0; i < cpeSize; i++) {
            int indexStart = i * ONU_CPE_IPINFO_SIZE;
            String[] ipRange = Arrays.copyOfRange(array, indexStart, indexStart + 4);
            String ip = transferIp(ipRange);
            if ("0.0.0.0".equals(ip)) {
                continue;
            }
            String[] macRange = Arrays.copyOfRange(array, indexStart + 4, indexStart + 10);
            String mac = transferMac(macRange);
            String cpeType = array[indexStart + 10];
            OnuCpeIpInfo info = new OnuCpeIpInfo();
            info.setIp(ip);
            info.setMac(mac);
            info.setCpeType(Integer.parseInt(cpeType));
            list.add(info);
        }
        return list;
    }

    private static String transferMac(String[] macRange) {
        StringBuilder sb = new StringBuilder();
        for (String macSegment : macRange) {
            sb.append(":").append(macSegment);
        }
        return sb.substring(1);
    }

    private static String transferIp(String[] ipRange) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            sb.append(".").append(Integer.parseInt(ipRange[i], 16));
        }
        return sb.substring(1);
    }

    public static List<OnuCpe> makeOnuCpeList(OnuUniCpeList onuUniCpeList, Long entityId, List<OnuCpeIpInfo> list) {
        if (onuUniCpeList != null) {
            if (!onuUniCpeList.isGponOnu()) {
                return makeEponOnuCpeList(onuUniCpeList, entityId, list);
            } else {
                return makeGponOnuCpeList(onuUniCpeList, entityId);
            }
        }
        return new ArrayList<OnuCpe>();
    }

    private static List<OnuCpe> makeEponOnuCpeList(OnuUniCpeList onuUniCpeList, Long entityId, List<OnuCpeIpInfo> list) {
        List<OnuCpe> re = new ArrayList<>();
        String uniMacInfoTotal = onuUniCpeList.getUniMacInfoTotal().replace(":", "");
        if (uniMacInfoTotal.length() != EPON_UNI_MAC_INFO_TOTAL_SIZE * 2) {
            throw new DataFormatErrorException("EPON uniMacInfoTotal length [" + uniMacInfoTotal.length() + "]");
        }
        int i = 1;
        try {
            for (; i <= MAX_EPON_CPE_COUNT; i++) {
                int startIndex = (i - 1) * ALLOCATE_SIZE_BY_EPON_CPE;
                int endIndex = i * ALLOCATE_SIZE_BY_EPON_CPE;
                OnuCpe onuCpe = makeOnuCpe(onuUniCpeList, uniMacInfoTotal.substring(startIndex, endIndex));
                onuCpe.setEntityId(entityId);
                String macAddress = onuCpe.getMac();
                if (!macAddress.equals("00:00:00:00:00:00")) {
                    re.add(onuCpe);
                }
                if (list != null && !list.isEmpty()) {
                    for (OnuCpeIpInfo ipInfo : list) {
                        if (macAddress.equalsIgnoreCase(ipInfo.getMac())) {
                            onuCpe.setIpAddress(ipInfo.getIp());
                            onuCpe.setCpeType(ipInfo.getCpeType());
                        }
                    }
                }
            }
        } catch (EndParseException e) {
            logger.debug("EPON uniMacInfoTotal:" + uniMacInfoTotal + " cpeNum:" + (i - 1));
        }
        return re;
    }

    private static List<OnuCpe> makeGponOnuCpeList(OnuUniCpeList onuUniCpeList, Long entityId) {
        List<OnuCpe> re = new ArrayList<>();
        String uniMacInfoTotal = onuUniCpeList.getUniMacInfoTotal().replace(":", "");
        //GPON uniMacInfoTotal变成变长的了
        if (uniMacInfoTotal == null || uniMacInfoTotal.length() == 0) {
            return re;
        }
        if (uniMacInfoTotal.length() > 0 && uniMacInfoTotal.length() % ALLOCATE_SIZE_BY_GPON_CPE != 0) {
            throw new DataFormatErrorException("GPON uniMacInfoTotal length [" + uniMacInfoTotal.length() + "]");
        }
        int n = uniMacInfoTotal.length() / ALLOCATE_SIZE_BY_GPON_CPE;
        if (n > MAX_GPON_CPE_COUNT) {
            throw new DataFormatErrorException("GPON uniMacInfoTotal length [" + uniMacInfoTotal.length() + "]");
        }
        int i = 1;
        try {
            for (; i <= n; i++) {
                int startIndex = (i - 1) * ALLOCATE_SIZE_BY_GPON_CPE;
                int endIndex = i * ALLOCATE_SIZE_BY_GPON_CPE;
                OnuCpe onuCpe = makeOnuCpe(onuUniCpeList, uniMacInfoTotal.substring(startIndex, endIndex));
                onuCpe.setEntityId(entityId);
                String macAddress = onuCpe.getMac();
                if (!macAddress.equals("00:00:00:00:00:00")) {
                    re.add(onuCpe);
                }
            }
        } catch (EndParseException e) {
            logger.debug("GPON uniMacInfoTotal:" + uniMacInfoTotal + " cpeNum:" + (i - 1));
        }
        return re;
    }

    public static OnuCpe makeOnuCpe(OnuUniCpeList onuUniCpeList, String data) {
        if (!onuUniCpeList.isGponOnu()) {
            return makeEponOnuCpe(onuUniCpeList, data);
        } else {
            return makeGponOnuCpe(onuUniCpeList, data);
        }
    }

    private static OnuCpe makeEponOnuCpe(OnuUniCpeList onuUniCpeList, String data) {
        String mac = data.substring(0, EPON_MAC_SIZE);
        Integer type = Integer.parseInt(data.substring(EPON_MAC_SIZE, EPON_MAC_SIZE + EPON_TYPE_SIZE), 16);
        Integer vlan = Integer.parseInt(data.substring(EPON_MAC_SIZE + EPON_TYPE_SIZE, ALLOCATE_SIZE_BY_EPON_CPE), 16);
        OnuCpe onuCpe = new OnuCpe();
        onuCpe.setEntityId(onuUniCpeList.getEntityId());
        Long uniIndex = getUniIndex(onuUniCpeList);
        onuCpe.setUniIndex(uniIndex);
        onuCpe.setMac(MacUtils.formatMac(mac).toString());
        onuCpe.setType(type);
        onuCpe.setVlan(vlan);
        onuCpe.setRealtime(onuUniCpeList.getRealTime());
        return onuCpe;
    }

    private static OnuCpe makeGponOnuCpe(OnuUniCpeList onuUniCpeList, String data) {
        String mac = data.substring(0, GPON_MAC_SIZE);
        Integer type = Integer.parseInt(data.substring(GPON_MAC_SIZE, GPON_MAC_SIZE + GPON_TYPE_SIZE), 16);
        Integer vlan = Integer.parseInt(data.substring(GPON_MAC_SIZE + GPON_TYPE_SIZE), 16);
        OnuCpe onuCpe = new OnuCpe();
        onuCpe.setEntityId(onuUniCpeList.getEntityId());
        Long uniIndex = getUniIndex(onuUniCpeList);
        onuCpe.setUniIndex(uniIndex);
        onuCpe.setMac(MacUtils.formatMac(mac).toString());
        onuCpe.setType(type);
        onuCpe.setVlan(vlan);
        onuCpe.setRealtime(onuUniCpeList.getRealTime());
        return onuCpe;
    }

    public static OnuUniCpeCount makeOnuUniCpeCount(OnuUniCpeList onuUniCpeList, List<OnuCpe> onuCpes) {
        OnuUniCpeCount onuUniCpeCount = new OnuUniCpeCount();
        if (onuUniCpeList != null) {
            Long uniIndex = getUniIndex(onuUniCpeList);
            onuUniCpeCount.setUniIndex(uniIndex);
            onuUniCpeCount.setUniNo(onuUniCpeList.getInfoUniIndex());
            onuUniCpeCount.setCpecount(onuCpes.size());
            onuUniCpeCount.setRealtime(onuUniCpeList.getRealTime());
            return onuUniCpeCount;
        } else {
            return onuUniCpeCount;
        }
    }

    public static OnuUniCpeList makeOnuUniCpeList(GponOnuUniCpeList gponOnuUniCpeList) {
        OnuUniCpeList onuUniCpeList = new OnuUniCpeList();
        onuUniCpeList.setEntityId(gponOnuUniCpeList.getEntityId());
        onuUniCpeList.setInfoSlotIndex(gponOnuUniCpeList.getInfoSlotIndex());
        onuUniCpeList.setInfoPortIndex(gponOnuUniCpeList.getInfoPortIndex());
        onuUniCpeList.setInfoOnuIndex(gponOnuUniCpeList.getInfoOnuIndex());
        onuUniCpeList.setInfoUniIndex(gponOnuUniCpeList.getInfoUniIndex());
        onuUniCpeList.setUniMacInfoTotal(gponOnuUniCpeList.getUniMacInfoTotal());
        onuUniCpeList.setDhcpIpInfoTotal(gponOnuUniCpeList.getDhcpIpInfoTotal());
        onuUniCpeList.setRealTime(gponOnuUniCpeList.getRealTime());
        onuUniCpeList.setGponOnu();
        return onuUniCpeList;
    }
}
