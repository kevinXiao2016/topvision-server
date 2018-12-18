/***********************************************************************
 * $Id: GponIndex.java,v1.0 2016年12月22日 上午8:45:13 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.utils;

import java.util.List;

import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Bravin
 * @created @2016年12月22日-上午8:45:13
 *
 */
public class GponIndex {
    public static final Long getPonNoFromMibIndex(Long mibIndex) {
        return (mibIndex.longValue() & 0x0000FF00L) >> 8;
    }

    public static final Long getSlotNoFromMibIndex(Long mibIndex) {
        return (mibIndex.longValue() & 0x00FF0000L) >> 16;
    }

    public static final Long getOnuNoFromMibIndex(Long mibIndex) {
        return (mibIndex.longValue() & 0x000000FFL);
    }

    public static final Long getPonIndexFromMibIndex(long mibIndex) {
        return EponIndex.getPonIndex(getSlotNoFromMibIndex(mibIndex).intValue(), getPonNoFromMibIndex(mibIndex)
                .intValue());
    }

    public static final Long getOnuAuthId(Long... id) {
        if (id == null || id.length == 0) {
            return null;
        }
        Long index = 0L;
        for (int i = 0; i < id.length; i++) {
            index += (id[i].longValue() << ((3 - i) * 8));
        }
        return index.longValue() & 0xFFFFFFFFL;
    }

    public static final String getMibStringFromGponPorts(List<Long> list) {
        if (list.size() == 0 || list == null) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            for (long portIndex : list) {
                sb.append(":").append(EponUtil.getByteHexStringFromNum(EponIndex.getSlotNo(portIndex).intValue()))
                        .append(":").append(EponUtil.getByteHexStringFromNum(EponIndex.getSniNo(portIndex).intValue()));
            }
            return sb.substring(1);
        }
    }

    public static final String getMibStringFromGponSn(String sn) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sn.length() / 2; i++) {
            String substring = sn.substring(2 * i, 2 * i + 2);
            sb.append(":").append(substring);
        }
        return sb.substring(1);
    }

    public static void main(String[] args) {
        System.out.println(getMibStringFromGponSn("544f505601012345"));
    }
}
