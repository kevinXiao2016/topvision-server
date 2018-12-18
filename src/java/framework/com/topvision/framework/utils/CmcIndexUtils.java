/***********************************************************************
 * $Id: CmcIndexUtils.java,v1.0 Nov 24, 2011 10:01:52 AM $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.utils;

import com.topvision.framework.constants.Symbol;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Victor
 * @created @Nov 24, 2011-10:01:52 AM
 * 
 */
public class CmcIndexUtils {
    /**
     * onuIndex(内部5个字节）转换为4个字节的cmcIndex.
     * 
     * @param onuIndex
     *            系统内部定义的onuIndex
     * @return cmcIndex，统一的cmcIndex定义
     */
    public static Long getCmcIndexFromOnuIndex(Long onuIndex) {
        // 处理PON=16的INDEX情况
        if ((onuIndex & 0xF0000000L) > 0) {
            onuIndex = ((onuIndex & 0xFF00000000L) | (onuIndex & 0xFFFFFFF));
        }
        return ((onuIndex & 0xFF00000000L) >> 5) | ((onuIndex & 0xFF000000L) >> 1) | (onuIndex & 0xFF0000L);
    }
    
    /**
     * @return Slot端口号
     */
    public static Long getSlotNo(Long index) {
        return (index.longValue() & 0xF8000000L) >> 27;
    }

    /**
     * @return Pon端口号
     */
    public static Long getPonNo(Long index) {
        return ((index.longValue() & 0x7800000L) >> 23) == 0 ? 16L : ((index.longValue() & 0x7800000L) >> 23);
    }

    /**
     * @return cmc Id
     */
    public static Long getCmcId(Long index) {
        return (index.longValue() & 0x7F0000L) >> 16;
    }

    /**
     * @return 信道类型（上行/下行）,0:UP, 1:DOWN
     */
    public static Long getChannelType(Long index) {
        return (index.longValue() & 0x8000L) >> 15;
    }

    /**
     * @return 信道ID
     */
    public static Long getChannelId(Long index) {
        return (index.longValue() & 0x7F00L) >> 8;
    }

    /**
     * 
     * @param index
     * @return CM id
     */
    public static long getCmId(Long index) {
        return (index.longValue() & 0xFFC0L) >> 6;
    }

    /**
     * cmcIndex转化为onuIndex(内部5个字节),getCmcIndexFromOnuIndex的逆运算
     * 
     * @param cmcIndex
     * @return
     * 
     *         Modify by Rod 逆运算需要对ONUID进行增加64的步骤
     */
    @Deprecated
    public static Long getOnuIndexFromCmcIndex(Long cmcIndex) {
        return ((cmcIndex & 0xF8000000) << 5) | ((cmcIndex & 0x7800000) << 1) | (cmcIndex & 0x7F0000) | 0x400000;
    }

    /**
     * channelIndex或cm的索引转化为cmcIndex
     * 
     * @param channelIndex
     * @return
     */
    public static Long getCmcIndexFromChannelIndex(Long channelIndex) {
        return (channelIndex.longValue() & 0xFFFF0000L);
    }

    /**
     * 通过cmIndex转换为cmcIndex
     * 
     * @param cmIndex
     * @return
     */
    public static Long getCmcIndexFromCmIndex(Long cmIndex) {
        return cmIndex.longValue() >> 16 << 16;
    }

    /**
     * 构造channelIndex
     * 
     * @param cmcIndex
     * @param channelType
     * @param channelId
     * @return
     */
    public static Long buildChannelIndex(Long cmcIndex, Long channelType, Long channelId) {
        if (channelType == 129) {
            return (cmcIndex.longValue() | (channelId.longValue() << 8));
        } else {
            return (cmcIndex.longValue() | 0x8000 | (channelId.longValue() << 8));
        }
    }

    /**
     * 组装channelName
     * 
     * @param channelIndex
     *            Long
     * @return String
     */
    public static String getMarkFromIndex(Long channelIndex) {
        Long slotNo = CmcIndexUtils.getSlotNo(channelIndex);
        Long ponNo = CmcIndexUtils.getPonNo(channelIndex);
        Long cmcNo = CmcIndexUtils.getCmcId(channelIndex);
        StringBuilder sb = new StringBuilder();
        sb.append(slotNo).append("/").append(ponNo).append("/").append(cmcNo);
        return sb.toString();
    }

    /**
     * 组装channelName
     * 
     * @param ifIndex
     * @return
     */
    public static String getCmcSourceString(Long ifIndex) {
        StringBuilder source = new StringBuilder();
        if (CmcIndexUtils.getChannelType(ifIndex) == 0) {
            source.append("[US]");
        } else if (CmcIndexUtils.getChannelType(ifIndex) == 1) {
            source.append("[DS]");
        }
        return source.toString();
    }

    /**
     * 由于CMC中不能调用EPON的东西，故在这里提供一个将portIndex转成 slot/port形式的方法
     * 
     * @param index
     * @return
     */
    public static String getEponPortFromIndex(Long index) {
        Long slot = (index.longValue() & 0xFF00000000L) >> 32;
        Long port = (index.longValue() & 0x00FF000000L) >> 24;
        return slot + "/" + port;
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

    /**
     * @return onu的LLID
     */
    public static Long getOnuNo(Long index) {
        return (index.longValue() & 0x0000FF0000L) >> 16;
    }

    public static Long getUpChannelIndex(Long cmcIndex, Long channelId) {
        return cmcIndex.longValue() | (channelId << 8);
    }

    public static Long getDownChannelIndex(Long cmcIndex, Long channelId) {
        return cmcIndex.longValue() | 0x8000L | (channelId << 8);
    }

    /**
     * docsLoadBalGrpId转化为cmcIndex
     * 
     * @param docsLoadBalGrpId
     * @return
     */
    public static Long getCmcIndexFromDocsLoadBalGrpId(Long docsLoadBalGrpId) {
        return (docsLoadBalGrpId & 0xFFFF0000L);
    }

    /**
     * docsLoadBalGrpId获取groupId
     * 
     * @param docsLoadBalGrpId
     * @return
     */
    public static Long getGroupIdFromDocsLoadBalGrpId(Long docsLoadBalGrpId) {
        return (docsLoadBalGrpId & 0xFF00L) >> 8;
    }

    /**
     * docsLoadBalGrpId获取groupId
     * 
     * @param docsLoadBalGrpId
     * @return
     */
    public static Long getNextDocsLoadBalGrpId(Long docsLoadBalGrpId) {
        return (docsLoadBalGrpId + 0x100L);
    }

    /**
     * 从List中找出第一个未使用的整数
     * 
     * @param indexs
     * @return
     */
    public static Integer generateIndex(List<Integer> indexs) {
        int index = 0;
        if (indexs != null && indexs.size() > 0) {
            int i = 0;
            for (; i < indexs.size(); i++) {
                if (index < indexs.get(i) - 1) {
                    return ++index;
                } else {
                    index = indexs.get(i);
                }
            }
            index = indexs.get(indexs.size() - 1);
            index++;
        }
        if (index == 0) {
            index++;
        }
        return index;
    }

    /**
     * 通过指定索引的起始值、终止值、已经使用的索引的数组，获取当前可用的最小的索引
     * 
     * @param indexs
     *            当前所使用的索引的数组
     * @param begin
     *            可以使用的索引的最小值，必须大于等于0
     * @param end
     *            可以使用的索引的最大值，必须大于等于begin
     * @return 返回值，如果返回-1则表示无可用索引
     * @throws Exception
     *             当begin，end参数不合法时抛出异常
     */
    public static int generateIndex(int[] indexs, int begin, int end) throws Exception {
        int index = begin;
        if (begin > end || begin < 0) {
            throw new Exception("Generate Index Paramete Error. begin:" + begin + ",end:" + end);
        }
        if (indexs != null) {
            List<Integer> allIndexs = new ArrayList<Integer>();
            for (int i = begin; i <= end; i++) {
                allIndexs.add(i);
            }
            List<Integer> usedIndexs = new ArrayList<Integer>();
            for (int i = 0; i < indexs.length; i++) {
                usedIndexs.add(indexs[i]);
            }
            allIndexs.removeAll(usedIndexs);
            if (allIndexs.size() > 0) {
                index = allIndexs.get(0);
            } else {
                index = -1;
            }
        }
        return index;
    }

    /**
     * 将ifIndex转换为字符串描述，用来查看当前ifIndex的具体含义
     * 
     * @param ifIndex
     * @return 返回ifIndex的字符串描述
     */
    public static String getPortInfoFromIfIndex(Integer ifIndex) {
        int slot = getSlotNo(ifIndex.longValue()).intValue();
        int pon = getPonNo(ifIndex.longValue()).intValue();
        int cmcId = getCmcId(ifIndex.longValue()).intValue();
        int channelType = getChannelType(ifIndex.longValue()).intValue();
        int channelId = getChannelId(ifIndex.longValue()).intValue();
        int cmcPon = (int) (ifIndex & 0x1L);
        StringBuilder sb = new StringBuilder();
        if (ifIndex <= 2) {
            return "CCMTS 8800B UpLink" + ifIndex;
        }
        if (cmcId == 0) {
            sb.append("OLT ");
        } else {
            sb.append("CCMTS ");
        }
        sb.append("SLOT:");
        sb.append(slot);
        sb.append(",PON:");
        sb.append(pon);
        if (cmcId != 0) {
            if (channelType == 1) {
                sb.append(",DOWM:");
                sb.append(channelId);
            } else {
                if (channelId == 0) {
                    if (cmcPon == 0) {
                        sb.append(",MAC Domain");
                    } else {
                        sb.append(",PON");
                    }

                } else {
                    sb.append(",UP:");
                    sb.append(channelId);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 通过channelIndex获取channelName
     * 
     * @param index
     * @return
     */
    public static String getChannelNameByIndex(Long index) {
        String channelTypeString = getChannelType(index) == 1l ? "DS" : "US";
        String Slot_Pon_cmcId = getSlotNo(index) + Symbol.SLASH + getPonNo(index) + Symbol.SLASH + getCmcId(index);
        String channelName = channelTypeString + " " + Slot_Pon_cmcId + Symbol.SLASH + getChannelId(index);
        return channelName;
    }

    /**
     * 获得理论上的下一个CC的Index，适用于单个CC的刷新过程
     * 
     * @param cmcIndex
     * @return
     */
    public static Long getNextCmcIndex(Long cmcIndex) {
        return cmcIndex + 0x00010000;
    }

    /**
     * 获取下一个CMindex
     * 
     * @param cmIndex
     * @return
     */
    public static Long getNextCmIndex(Long cmIndex) {
        Long nextCmIndex = cmIndex + 0x40;
        return nextCmIndex;
    }

    public static Long getChannelIndexFromEqamIndex(Long EqamIndex) {
        return (EqamIndex & 0xFFFFFF00L);
    }

    public static Long getSessionIdFromSessionIndex(Long EqamIndex) {
        return (EqamIndex & 0x000000FFL);
    }
}
