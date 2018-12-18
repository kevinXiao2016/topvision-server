/***********************************************************************
 * $ OltPerf.java,v1.0 2011-11-21 12:34:46 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.platform.ResourceManager;

/**
 * @author jay
 * @created @2011-11-21-12:34:46
 */
public class OltPerf implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    /**
     * 显示名数组
     */
    public static String[] PERFNAME = { "", "InOctets", "InPkts", "InBroadcastPkts", "InMulticastPkts",
            "InPkts64Octets", "InPkts65to127Octets", "InPkts128to255Octets", "InPkts256to511Octets",
            "InPkts512to1023Octets", "InPkts1024to1518Octets", "InPkts1519to1522Octets", "InUndersizePkts",
            "InOversizePkts", "InFragments", "InMpcpFrames", "InMpcpOctets", "InOAMFrames", "InOAMOctets",
            "InCRCErrorPkts", "InDropEvents", "InJabbers", "InCollision", "OutOctets", "OutPkts", "OutBroadcastPkts",
            "OutMulticastPkts", "OutPkts64Octets", "OutPkts65to127Octets", "OutPkts128to255Octets",
            "OutPkts256to511Octets", "OutPkts512to1023Octets", "OutPkts1024to1518Octets", "OutPkts1519to1522Octets",
            "OutUndersizePkts", "OutOversizePkts", "OutFragments", "OutMpcpFrames", "OutMpcpOctets", "OutOAMFrames",
            "OutOAMOctets", "OutCRCErrorPkts", "OutDropEvents", "OutJabbers", "OutCollision", "", "", "", "", "",
            "Temperature" };

    // SNI端口支持的指标项
    public static Integer[] SNIPERF = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 13, 14, 19, 21, 23, 24, 25, 26, 27, 28, 29,
            30, 31, 32, 35 };
    // PON端口支持的指标项
    public static Integer[] PONPERF = { 1, 2, 3, 4, 12, 15, 16, 17, 18, 19, 20, 23, 24, 25, 26, 34, 36, 37, 38, 39, 40 };
    // ONUPON端口支持的指标项
    public static Integer[] ONUPONPERF = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 15, 17, 19, 20, 23, 24, 25, 26, 27, 28,
            29, 30, 31, 32, 37, 39, 42 };
    // UNI端口支持的指标项
    public static Integer[] UNIPERF = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 13, 19, 23, 24, 25, 26, 27, 28, 29, 30, 31,
            32, 42, 44 };
    public static Integer[] PERF = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
            24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44 };
    public static Integer[] TEMP = { 50 };
    // 阈值对应的topPerfThresholdTypeIndex
    /**
     * 接收字节数
     */
    public static int INOCTETS = 1;
    /**
     * 接收帧数
     */
    public static int INPKTS = 2;
    /**
     * 接收广播帧数
     */
    public static int INBROADCASTPKTS = 3;
    /**
     * 接收组播帧数
     */
    public static int INMULTICASTPKTS = 4;
    /**
     * 接收64字节包数
     */
    public static int INPKTS64OCTETS = 5;
    /**
     * 接收65～127字节包数
     */
    public static int INPKTS128TO255OCTETS = 7;
    /**
     * 接收128～255字节包数
     */
    public static int INPKTS65TO127OCTETS = 6;
    /**
     * 接收256～511字节包数
     */
    public static int INPKTS256TO511OCTETS = 8;
    /**
     * 接收512～1023字节包数
     */
    public static int INPKTS512TO1023OCTETS = 9;
    /**
     * 接收1024～1518字节包数
     */
    public static int INPKTS1024TO1518OCTETS = 10;
    /**
     * 接收1519～1522字节包数
     */
    public static int INPKTS1519TO1522OCTETS = 11;
    /**
     * 接收超短帧数
     */
    public static int INUNDERSIZEPKS = 12;
    /**
     * 接收超长帧数
     */
    public static int INOVERSIZEPKS = 13;
    /**
     * 接收碎片数
     */
    public static int INMPCPFRAMES = 15;
    /**
     * 接收MPCP帧数
     */
    public static int INFRAGMENTS = 14;

    /**
     * 接收MPCP字节数
     */
    public static int INMPCPOCTETS = 16;
    /**
     * 接收OAM帧数
     */
    public static int INOAMFRAMES = 17;
    /**
     * 接收OAM字节数
     */
    public static int INOAMOCTETS = 18;
    /**
     * 接收CRC错误帧
     */
    public static int INCRCERRORPKTS = 19;
    /**
     * 接收丢包事件次数
     */
    public static int INDROPEVENTS = 20;
    /**
     * 接收超长错帧数
     */
    public static int INJABBERS = 21;
    /**
     * 接收碰撞帧数
     */
    public static int INCOLLISION = 22;
    /**
     * 发送字节数
     */
    public static int OUTOCTETS = 23;
    /**
     * 发送帧数
     */
    public static int OUTPKTS = 24;
    /**
     * 发送广播帧数
     */
    public static int OUTBROADCASTPKTS = 25;
    /**
     * 发送组播帧数
     */
    public static int OUTMULTICASTPKTS = 26;
    /**
     * 发送64字节包数
     */
    public static int OUTPKTS64OCTETS = 27;
    /**
     * 发送65～127字节包数
     */
    public static int OUTPKTS65TO127OCTETS = 28;
    /**
     * 发送128～255字节包数
     */
    public static int OUTPKTS128TO255OCTETS = 29;
    /**
     * 发送256～511字节包数
     */
    public static int OUTPKTS256TO511OCTETS = 30;
    /**
     * 发送512～1023字节包数
     */
    public static int OUTPKTS512TO1023OCTETS = 31;
    /**
     * 发送1024～1518字节包数
     */
    public static int OUTPKTS1024TO1518OCTETS = 32;
    /**
     * 发送1519～1522字节包数
     */
    public static int OUTPKTS1519TO1522OCTETS = 33;
    /**
     * 发送超短帧数
     */
    public static int OUTUNDERSIZEPKS = 34;
    /**
     * 发送超长帧数
     */
    public static int OUTOVERSIZEPKS = 35;
    /**
     * 发送碎片数
     */
    public static int OUTFRAGMENTS = 36;
    /**
     * 发送MPCP帧数
     */
    public static int OUTMPCPFRAMES = 37;
    /**
     * 发送MPCP字节数
     */
    public static int OUTMPCPOCTETS = 38;
    /**
     * 发送OAM帧数
     */
    public static int OUTOAMFRAMES = 39;
    /**
     * 发送OAM字节数
     */
    public static int OUTOAMOCTETS = 40;
    /**
     * 发送CRC错误帧
     */
    public static int OUTCRCERRORPKTS = 41;
    /**
     * 发送丢包事件次数
     */
    public static int OUTDROPEVENTS = 42;
    /**
     * 发送超长错帧数
     */
    public static int OUTJABBERS = 43;
    /**
     * 发送碰撞帧数
     */
    public static int OUTCOLLISION = 44;
    // 阈值对应的topPerfTemperatureThresholdTypeIdx
    /**
     * 温度
     */
    public static int TEMPERATURE = 50;

    /**
     * 指标的界面显示名字 英文 需要国际化
     */
    private String perfName;
    /**
     * 指标对应的index 数据库 阈值都会用到
     */
    private Integer perfIndex;

    public Integer getPerfIndex() {
        return perfIndex;
    }

    public void setPerfIndex(Integer perfIndex) {
        this.perfIndex = perfIndex;
    }

    public String getPerfName() {
        return perfName;
    }

    public void setPerfName(String perfName) {
        this.perfName = perfName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OltPerf");
        sb.append("{perfIndex=").append(perfIndex);
        sb.append(", perfName='").append(perfName).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static String getPerfNameById(int i, int type, String language) {
        ResourceManager resourceManager = ResourceManager.getResourceManager("com.topvision.ems.epon.resources");
        String perfNameString = "PERF.moI." + PERFNAME[i];
        if (i == 50) {// 表示温度指标
            if (type == 1)// 表示为主控板
            {
                perfNameString = "PERF.moI.mpuBoardTemperature";
            }
            if (type == 2)// 表示为PON板
            {
                perfNameString = "PERF.moI.ponBoardTemperature";
            }
            if (type == 3)// 表示为上联板
            {
                perfNameString = "PERF.moI.uplinkBoardTemperature";
            }
            if (type == 4)// 表示为ONU
            {
                perfNameString = "PERF.moI.onuTemperature";
            }
        }
        if (i == 0) {// 表为采集时间
            perfNameString = "PERF.moI.EndTime";
        }
        return resourceManager.getNotNullString(perfNameString);
    }

    public static List<OltPerf> createPerfsList(String portType) {
        List<OltPerf> oltPerfs = new ArrayList<OltPerf>();
        Integer[] tempPerf = getPerfTargetGroup(portType);
        for (int i : tempPerf) {
            if (PERFNAME[i] != null && !PERFNAME[i].equals("")) {
                OltPerf oltPerf = new OltPerf();
                oltPerf.setPerfName(PERFNAME[i]);
                oltPerf.setPerfIndex(i);
                oltPerfs.add(oltPerf);
            }
        }
        return oltPerfs; // To change body of created methods use File | Settings | File Templates.
    }

    public static Integer[] getPerfTargetGroup(String type) {
        if (type.equals("SNI")) {
            return SNIPERF;
        } else if (type.equals("PON")) {
            return PONPERF;
        } else if (type.equals("ONUPON")) {
            return ONUPONPERF;
        } else if (type.equals("ONUUNI")) {
            return UNIPERF;
        } else if (type.equals("TEMPERATURE")) {
            return TEMP;
        } else {
            return PERF;
        }
    }

    public static Map<Integer, Long> createPerfMap(Object object) throws InvocationTargetException,
            IllegalAccessException {
        Method[] ms = object.getClass().getMethods();
        Map<Integer, Long> re = new HashMap<Integer, Long>();
        for (Method m : ms) {
            if (m.getName().startsWith("get")) {
                for (int i = 1; i < PERFNAME.length + 1; i++) {
                    if (!PERFNAME[i - 1].equals("") && m.getName().indexOf(PERFNAME[i - 1]) != -1) {
                        Long value = (Long) m.invoke(object);
                        re.put(i - 1, value);
                    }
                }
            }
        }
        return re;
    }
}
