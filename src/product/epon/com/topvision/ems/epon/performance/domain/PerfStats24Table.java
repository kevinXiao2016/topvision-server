/***********************************************************************
 * $ PerfStats24Table.java,v1.0 2011-11-21 8:35:00 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.topvision.framework.utils.EponIndex;
import com.topvision.ems.facade.domain.PerformanceDomain;
import com.topvision.ems.network.util.PortUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2011-11-21-8:35:00
 */
public class PerfStats24Table implements PerformanceDomain, Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Long entityId;
    /**
     * 设备索引号 For OLT, set to corresponding device/slot/port For ONU, set to 0
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.1", index = true)
    private Long deviceIndex;
    /**
     * 板卡索引号 For OLT, set to 0 For ONU, set to corresponding slot
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.2", index = true)
    private Long slotNo;
    /**
     * 端口索引号 For OLT, set to 0 For ONU, set to corresponding port
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.3", index = true)
    private Long portNo;
    private Long portIndex;
    /**
     * 顺序索引号
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.4", index = true)
    private Long stats24Index;
    /**
     * 接收字节数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.5")
    private String stats24InOctets;
    private String stats24InFlow;
    /**
     * 接收帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.6")
    private String stats24InPkts;
    /**
     * 接收广播帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.7")
    private String stats24InBroadcastPkts;
    /**
     * 接收组播帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.8")
    private String stats24InMulticastPkts;
    /**
     * 接收64字节包数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.9")
    private String stats24InPkts64Octets;
    /**
     * 接收65～127字节包数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.10")
    private String stats24InPkts65to127Octets;
    /**
     * 接收128～255字节包数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.11")
    private String stats24InPkts128to255Octets;
    /**
     * 接收256～511字节包数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.12")
    private String stats24InPkts256to511Octets;
    /**
     * 接收512～1023字节包数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.13")
    private String stats24InPkts512to1023Octets;
    /**
     * 接收1024～1518字节包数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.14")
    private String stats24InPkts1024to1518Octets;
    /**
     * 接收1519～1522字节包数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.15")
    private String stats24InPkts1519to1522Octets;
    /**
     * 接收超短帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.16")
    private String stats24InUndersizePkts;
    /**
     * 接收超长帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.17")
    private String stats24InOversizePkts;
    /**
     * 接收碎片数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.18")
    private String stats24InFragments;
    /**
     * 接收MPCP帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.19")
    private String stats24InMpcpFrames;
    /**
     * 接收MPCP字节数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.20")
    private String stats24InMpcpOctets;
    /**
     * 接收OAM帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.21")
    private String stats24InOAMFrames;
    /**
     * 接收OAM字节数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.22")
    private String stats24InOAMOctets;
    /**
     * 接收CRC错误帧
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.23")
    private String stats24InCRCErrorPkts;
    /**
     * 接收丢包事件次数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.24")
    private String stats24InDropEvents;
    /**
     * 接收超长错帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.25")
    private String stats24InJabbers;
    /**
     * 接收碰撞帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.26")
    private String stats24InCollision;
    /**
     * 发送字节数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.27")
    private String stats24OutOctets;
    private String stats24OutFlow;
    /**
     * 发送帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.28")
    private String stats24OutPkts;
    /**
     * 发送广播帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.29")
    private String stats24OutBroadcastPkts;
    /**
     * 发送组播帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.30")
    private String stats24OutMulticastPkts;
    /**
     * 发送64字节包数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.31")
    private String stats24OutPkts64Octets;
    /**
     * 发送65～127字节包数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.32")
    private String stats24OutPkts65to127Octets;
    /**
     * 发送128～255字节包数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.33")
    private String stats24OutPkts128to255Octets;
    /**
     * 发送256～511字节包数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.34")
    private String stats24OutPkts256to511Octets;
    /**
     * 发送512～1023字节包数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.35")
    private String stats24OutPkts512to1023Octets;
    /**
     * 发送1024～1518字节包数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.36")
    private String stats24OutPkts1024to1518Octets;
    /**
     * 发送1519～1522字节包数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.37")
    private String stats24OutPkts1519to1522Octets;
    /**
     * 发送超短帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.38")
    private String stats24OutUndersizePkts;
    /**
     * 发送超长帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.39")
    private String stats24OutOversizePkts;
    /**
     * 发送碎片数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.40")
    private String stats24OutFragments;
    /**
     * 发送MPCP帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.41")
    private String stats24OutMpcpFrames;
    /**
     * 发送MPCP字节数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.42")
    private String stats24OutMpcpOctets;
    /**
     * 发送OAM帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.43")
    private String stats24OutOAMFrames;
    /**
     * 发送OAM字节数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.44")
    private String stats24OutOAMOctets;
    /**
     * 发送CRC错误帧
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.45")
    private String stats24OutCRCErrorPkts;
    /**
     * 发送丢包事件次数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.46")
    private String stats24OutDropEvents;
    /**
     * 发送超长错帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.47")
    private String stats24OutJabbers;
    /**
     * 发送碰撞帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.48")
    private String stats24OutCollision;

    /**
     * 当前统计状态及操作 clean(2) 统计量清零
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.49", writable = true, type = "Integer32")
    private Integer stats24StatusAndAction;
    /**
     * Whether or not the current 15-min PM data is valid
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.50")
    private Integer stats24ValidityTag;
    /**
     * It is elapsed time, since start of this 15-min interval. It is only valid for current PM
     * data. For history PM data, set to 0
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.51")
    private Long stats24ElapsedTime;
    /**
     * It is time, when the PM history data is created. It is only valid for history PM data
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.3.1.52")
    private String stats24EndTime;
    private Timestamp stats24EndTimeDb;
    private String stats24EndTimeString;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getPortIndex() {
        if (portIndex == null) {
            if (EponIndex.getOnuNoByMibDeviceIndex(deviceIndex) == 0) {
                // 此时代表为OLT
                portIndex = EponIndex.getPonIndexByMibDeviceIndex(deviceIndex);
            } else {
                portIndex = EponIndex.getUniIndexByMibIndex(deviceIndex, slotNo, portNo);
            }
        }
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
        Long onuNo = EponIndex.getOnuNo(portIndex);
        if (onuNo == 0) {
            deviceIndex = EponIndex.getOnuMibIndexByIndex(portIndex);
            slotNo = 0L;
            portNo = 0L;
        } else {
            deviceIndex = EponIndex.getOnuMibIndexByIndex(portIndex);
            slotNo = EponIndex.getOnuCardNo(portIndex);
            if (EponIndex.getUniNo(portIndex) == 255) {
                portNo = 0l;
            } else {
                portNo = EponIndex.getUniNo(portIndex);
            }
        }
    }

    public Long getStats24ElapsedTime() {
        return stats24ElapsedTime;
    }

    public void setStats24ElapsedTime(Long stats24ElapsedTime) {
        this.stats24ElapsedTime = stats24ElapsedTime;
    }

    public String getStats24InBroadcastPkts() {
        return stats24InBroadcastPkts;
    }

    public void setStats24InBroadcastPkts(String stats24InBroadcastPkts) {
        this.stats24InBroadcastPkts = stats24InBroadcastPkts;
    }

    public String getStats24InCollision() {
        return stats24InCollision;
    }

    public void setStats24InCollision(String stats24InCollision) {
        this.stats24InCollision = stats24InCollision;
    }

    public String getStats24InCRCErrorPkts() {
        return stats24InCRCErrorPkts;
    }

    public void setStats24InCRCErrorPkts(String stats24InCRCErrorPkts) {
        this.stats24InCRCErrorPkts = stats24InCRCErrorPkts;
    }

    public Long getStats24Index() {
        return stats24Index;
    }

    public void setStats24Index(Long stats24Index) {
        this.stats24Index = stats24Index;
    }

    public String getStats24InDropEvents() {
        return stats24InDropEvents;
    }

    public void setStats24InDropEvents(String stats24InDropEvents) {
        this.stats24InDropEvents = stats24InDropEvents;
    }

    public String getStats24InFragments() {
        return stats24InFragments;
    }

    public void setStats24InFragments(String stats24InFragments) {
        this.stats24InFragments = stats24InFragments;
    }

    public String getStats24InJabbers() {
        return stats24InJabbers;
    }

    public void setStats24InJabbers(String stats24InJabbers) {
        this.stats24InJabbers = stats24InJabbers;
    }

    public String getStats24InMpcpFrames() {
        return stats24InMpcpFrames;
    }

    public void setStats24InMpcpFrames(String stats24InMpcpFrames) {
        this.stats24InMpcpFrames = stats24InMpcpFrames;
    }

    public String getStats24InMpcpOctets() {
        return stats24InMpcpOctets;
    }

    public void setStats24InMpcpOctets(String stats24InMpcpOctets) {
        this.stats24InMpcpOctets = stats24InMpcpOctets;
    }

    public String getStats24InMulticastPkts() {
        return stats24InMulticastPkts;
    }

    public void setStats24InMulticastPkts(String stats24InMulticastPkts) {
        this.stats24InMulticastPkts = stats24InMulticastPkts;
    }

    public String getStats24InOAMFrames() {
        return stats24InOAMFrames;
    }

    public void setStats24InOAMFrames(String stats24InOAMFrames) {
        this.stats24InOAMFrames = stats24InOAMFrames;
    }

    public String getStats24InOAMOctets() {
        return stats24InOAMOctets;
    }

    public void setStats24InOAMOctets(String stats24InOAMOctets) {
        this.stats24InOAMOctets = stats24InOAMOctets;
    }

    public String getStats24InOctets() {
        return stats24InOctets;
    }

    public void setStats24InOctets(String stats24InOctets) {
        stats24InFlow = PortUtil.getIfOctetsRateString(Double.parseDouble(stats24InOctets) * 8 / (24 * 60 * 60));
        this.stats24InOctets = stats24InOctets;
    }

    public String getStats24InOversizePkts() {
        return stats24InOversizePkts;
    }

    public void setStats24InOversizePkts(String stats24InOversizePkts) {
        this.stats24InOversizePkts = stats24InOversizePkts;
    }

    public String getStats24InPkts1024to1518Octets() {
        return stats24InPkts1024to1518Octets;
    }

    public void setStats24InPkts1024to1518Octets(String stats24InPkts1024to1518Octets) {
        this.stats24InPkts1024to1518Octets = stats24InPkts1024to1518Octets;
    }

    public String getStats24InPkts128to255Octets() {
        return stats24InPkts128to255Octets;
    }

    public void setStats24InPkts128to255Octets(String stats24InPkts128to255Octets) {
        this.stats24InPkts128to255Octets = stats24InPkts128to255Octets;
    }

    public String getStats24InPkts1519to1522Octets() {
        return stats24InPkts1519to1522Octets;
    }

    public void setStats24InPkts1519to1522Octets(String stats24InPkts1519to1522Octets) {
        this.stats24InPkts1519to1522Octets = stats24InPkts1519to1522Octets;
    }

    public String getStats24InPkts256to511Octets() {
        return stats24InPkts256to511Octets;
    }

    public void setStats24InPkts256to511Octets(String stats24InPkts256to511Octets) {
        this.stats24InPkts256to511Octets = stats24InPkts256to511Octets;
    }

    public String getStats24InPkts512to1023Octets() {
        return stats24InPkts512to1023Octets;
    }

    public void setStats24InPkts512to1023Octets(String stats24InPkts512to1023Octets) {
        this.stats24InPkts512to1023Octets = stats24InPkts512to1023Octets;
    }

    public String getStats24InPkts64Octets() {
        return stats24InPkts64Octets;
    }

    public void setStats24InPkts64Octets(String stats24InPkts64Octets) {
        this.stats24InPkts64Octets = stats24InPkts64Octets;
    }

    public String getStats24InPkts65to127Octets() {
        return stats24InPkts65to127Octets;
    }

    public void setStats24InPkts65to127Octets(String stats24InPkts65to127Octets) {
        this.stats24InPkts65to127Octets = stats24InPkts65to127Octets;
    }

    public String getStats24InPkts() {
        return stats24InPkts;
    }

    public void setStats24InPkts(String stats24InPkts) {
        this.stats24InPkts = stats24InPkts;
    }

    public String getStats24InUndersizePkts() {
        return stats24InUndersizePkts;
    }

    public void setStats24InUndersizePkts(String stats24InUndersizePkts) {
        this.stats24InUndersizePkts = stats24InUndersizePkts;
    }

    public String getStats24OutBroadcastPkts() {
        return stats24OutBroadcastPkts;
    }

    public void setStats24OutBroadcastPkts(String stats24OutBroadcastPkts) {
        this.stats24OutBroadcastPkts = stats24OutBroadcastPkts;
    }

    public String getStats24OutCollision() {
        return stats24OutCollision;
    }

    public void setStats24OutCollision(String stats24OutCollision) {
        this.stats24OutCollision = stats24OutCollision;
    }

    public String getStats24OutCRCErrorPkts() {
        return stats24OutCRCErrorPkts;
    }

    public void setStats24OutCRCErrorPkts(String stats24OutCRCErrorPkts) {
        this.stats24OutCRCErrorPkts = stats24OutCRCErrorPkts;
    }

    public String getStats24OutDropEvents() {
        return stats24OutDropEvents;
    }

    public void setStats24OutDropEvents(String stats24OutDropEvents) {
        this.stats24OutDropEvents = stats24OutDropEvents;
    }

    public String getStats24OutFragments() {
        return stats24OutFragments;
    }

    public void setStats24OutFragments(String stats24OutFragments) {
        this.stats24OutFragments = stats24OutFragments;
    }

    public String getStats24OutJabbers() {
        return stats24OutJabbers;
    }

    public void setStats24OutJabbers(String stats24OutJabbers) {
        this.stats24OutJabbers = stats24OutJabbers;
    }

    public String getStats24OutMpcpFrames() {
        return stats24OutMpcpFrames;
    }

    public void setStats24OutMpcpFrames(String stats24OutMpcpFrames) {
        this.stats24OutMpcpFrames = stats24OutMpcpFrames;
    }

    public String getStats24OutMpcpOctets() {
        return stats24OutMpcpOctets;
    }

    public void setStats24OutMpcpOctets(String stats24OutMpcpOctets) {
        this.stats24OutMpcpOctets = stats24OutMpcpOctets;
    }

    public String getStats24OutMulticastPkts() {
        return stats24OutMulticastPkts;
    }

    public void setStats24OutMulticastPkts(String stats24OutMulticastPkts) {
        this.stats24OutMulticastPkts = stats24OutMulticastPkts;
    }

    public String getStats24OutOAMFrames() {
        return stats24OutOAMFrames;
    }

    public void setStats24OutOAMFrames(String stats24OutOAMFrames) {
        this.stats24OutOAMFrames = stats24OutOAMFrames;
    }

    public String getStats24OutOAMOctets() {
        return stats24OutOAMOctets;
    }

    public void setStats24OutOAMOctets(String stats24OutOAMOctets) {
        this.stats24OutOAMOctets = stats24OutOAMOctets;
    }

    public String getStats24OutOctets() {
        return stats24OutOctets;
    }

    public void setStats24OutOctets(String stats24OutOctets) {
        stats24OutFlow = PortUtil.getIfOctetsRateString(Double.parseDouble(stats24OutOctets) * 8 / (24 * 60 * 60));
        this.stats24OutOctets = stats24OutOctets;
    }

    public String getStats24OutOversizePkts() {
        return stats24OutOversizePkts;
    }

    public void setStats24OutOversizePkts(String stats24OutOversizePkts) {
        this.stats24OutOversizePkts = stats24OutOversizePkts;
    }

    public String getStats24OutPkts1024to1518Octets() {
        return stats24OutPkts1024to1518Octets;
    }

    public void setStats24OutPkts1024to1518Octets(String stats24OutPkts1024to1518Octets) {
        this.stats24OutPkts1024to1518Octets = stats24OutPkts1024to1518Octets;
    }

    public String getStats24OutPkts128to255Octets() {
        return stats24OutPkts128to255Octets;
    }

    public void setStats24OutPkts128to255Octets(String stats24OutPkts128to255Octets) {
        this.stats24OutPkts128to255Octets = stats24OutPkts128to255Octets;
    }

    public String getStats24OutPkts1519to1522Octets() {
        return stats24OutPkts1519to1522Octets;
    }

    public void setStats24OutPkts1519to1522Octets(String stats24OutPkts1519to1522Octets) {
        this.stats24OutPkts1519to1522Octets = stats24OutPkts1519to1522Octets;
    }

    public String getStats24OutPkts256to511Octets() {
        return stats24OutPkts256to511Octets;
    }

    public void setStats24OutPkts256to511Octets(String stats24OutPkts256to511Octets) {
        this.stats24OutPkts256to511Octets = stats24OutPkts256to511Octets;
    }

    public String getStats24OutPkts512to1023Octets() {
        return stats24OutPkts512to1023Octets;
    }

    public void setStats24OutPkts512to1023Octets(String stats24OutPkts512to1023Octets) {
        this.stats24OutPkts512to1023Octets = stats24OutPkts512to1023Octets;
    }

    public String getStats24OutPkts64Octets() {
        return stats24OutPkts64Octets;
    }

    public void setStats24OutPkts64Octets(String stats24OutPkts64Octets) {
        this.stats24OutPkts64Octets = stats24OutPkts64Octets;
    }

    public String getStats24OutPkts65to127Octets() {
        return stats24OutPkts65to127Octets;
    }

    public void setStats24OutPkts65to127Octets(String stats24OutPkts65to127Octets) {
        this.stats24OutPkts65to127Octets = stats24OutPkts65to127Octets;
    }

    public String getStats24OutPkts() {
        return stats24OutPkts;
    }

    public void setStats24OutPkts(String stats24OutPkts) {
        this.stats24OutPkts = stats24OutPkts;
    }

    public String getStats24OutUndersizePkts() {
        return stats24OutUndersizePkts;
    }

    public void setStats24OutUndersizePkts(String stats24OutUndersizePkts) {
        this.stats24OutUndersizePkts = stats24OutUndersizePkts;
    }

    public Integer getStats24StatusAndAction() {
        return stats24StatusAndAction;
    }

    public void setStats24StatusAndAction(Integer stats24StatusAndAction) {
        this.stats24StatusAndAction = stats24StatusAndAction;
    }

    public Integer getStats24ValidityTag() {
        return stats24ValidityTag;
    }

    public void setStats24ValidityTag(Integer stats24ValidityTag) {
        this.stats24ValidityTag = stats24ValidityTag;
    }

    public Long getDeviceIndex() {
        return deviceIndex;
    }

    public void setDeviceIndex(Long deviceIndex) {
        this.deviceIndex = deviceIndex;
    }

    public Long getPortNo() {
        return portNo;
    }

    public void setPortNo(Long portNo) {
        this.portNo = portNo;
    }

    public Long getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(Long slotNo) {
        this.slotNo = slotNo;
    }

    public String getStats24EndTime() {
        return stats24EndTime;
    }

    public void setStats24EndTime(String stats24EndTime) {
        this.stats24EndTime = stats24EndTime;
        if (stats24EndTime != null) {
            stats24EndTimeDb = new Timestamp(Long.parseLong(stats24EndTime) > 0 ? Long.parseLong(stats24EndTime) : 1);
            stats24EndTimeString = sdf.format(new Timestamp(Long.parseLong(stats24EndTime)));
        }
    }

    public Timestamp getStats24EndTimeDb() {
        return stats24EndTimeDb;
    }

    public void setStats24EndTimeDb(Timestamp stats24EndTimeDb) {
        this.stats24EndTimeDb = stats24EndTimeDb;
        stats24EndTime = String.valueOf(stats24EndTimeDb.getTime());
        stats24EndTimeString = sdf.format(stats24EndTimeDb);
    }

    public String getStats24EndTimeString() {
        return stats24EndTimeString;
    }

    public void setStats24EndTimeString(String stats24EndTimeString) {
        this.stats24EndTimeString = stats24EndTimeString;
        try {
            stats24EndTime = String.valueOf(sdf.parse(stats24EndTimeString).getTime());
            stats24EndTimeDb = new Timestamp(Long.parseLong(stats24EndTime));
        } catch (ParseException e) {
        }
    }

    public String getStats24InFlow() {
        return stats24InFlow;
    }

    public void setStats24InFlow(String stats24InFlow) {
        this.stats24InFlow = stats24InFlow;
    }

    public String getStats24OutFlow() {
        return stats24OutFlow;
    }

    public void setStats24OutFlow(String stats24OutFlow) {
        this.stats24OutFlow = stats24OutFlow;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PerfStats24Table");
        sb.append("{deviceIndex=").append(deviceIndex);
        sb.append(", sdf=").append(sdf);
        sb.append(", entityId=").append(entityId);
        sb.append(", slotNo=").append(slotNo);
        sb.append(", portNo=").append(portNo);
        sb.append(", portIndex=").append(portIndex);
        sb.append(", stats24Index=").append(stats24Index);
        sb.append(", stats24InOctets=").append(stats24InOctets);
        sb.append(", stats24InPkts=").append(stats24InPkts);
        sb.append(", stats24InBroadcastPkts=").append(stats24InBroadcastPkts);
        sb.append(", stats24InMulticastPkts=").append(stats24InMulticastPkts);
        sb.append(", stats24InPkts64Octets=").append(stats24InPkts64Octets);
        sb.append(", stats24InPkts65to127Octets=").append(stats24InPkts65to127Octets);
        sb.append(", stats24InPkts128to255Octets=").append(stats24InPkts128to255Octets);
        sb.append(", stats24InPkts256to511Octets=").append(stats24InPkts256to511Octets);
        sb.append(", stats24InPkts512to1023Octets=").append(stats24InPkts512to1023Octets);
        sb.append(", stats24InPkts1024to1518Octets=").append(stats24InPkts1024to1518Octets);
        sb.append(", stats24InPkts1519to1522Octets=").append(stats24InPkts1519to1522Octets);
        sb.append(", stats24InUndersizePkts=").append(stats24InUndersizePkts);
        sb.append(", stats24InOversizePkts=").append(stats24InOversizePkts);
        sb.append(", stats24InFragments=").append(stats24InFragments);
        sb.append(", stats24InMpcpFrames=").append(stats24InMpcpFrames);
        sb.append(", stats24InMpcpOctets=").append(stats24InMpcpOctets);
        sb.append(", stats24InOAMFrames=").append(stats24InOAMFrames);
        sb.append(", stats24InOAMOctets=").append(stats24InOAMOctets);
        sb.append(", stats24InCRCErrorPkts=").append(stats24InCRCErrorPkts);
        sb.append(", stats24InDropEvents=").append(stats24InDropEvents);
        sb.append(", stats24InJabbers=").append(stats24InJabbers);
        sb.append(", stats24InCollision=").append(stats24InCollision);
        sb.append(", stats24OutOctets=").append(stats24OutOctets);
        sb.append(", stats24OutPkts=").append(stats24OutPkts);
        sb.append(", stats24OutBroadcastPkts=").append(stats24OutBroadcastPkts);
        sb.append(", stats24OutMulticastPkts=").append(stats24OutMulticastPkts);
        sb.append(", stats24OutPkts64Octets=").append(stats24OutPkts64Octets);
        sb.append(", stats24OutPkts65to127Octets=").append(stats24OutPkts65to127Octets);
        sb.append(", stats24OutPkts128to255Octets=").append(stats24OutPkts128to255Octets);
        sb.append(", stats24OutPkts256to511Octets=").append(stats24OutPkts256to511Octets);
        sb.append(", stats24OutPkts512to1023Octets=").append(stats24OutPkts512to1023Octets);
        sb.append(", stats24OutPkts1024to1518Octets=").append(stats24OutPkts1024to1518Octets);
        sb.append(", stats24OutPkts1519o1522Octets=").append(stats24OutPkts1519to1522Octets);
        sb.append(", stats24OutUndersizePkts=").append(stats24OutUndersizePkts);
        sb.append(", stats24OutOversizePkts=").append(stats24OutOversizePkts);
        sb.append(", stats24OutFragments=").append(stats24OutFragments);
        sb.append(", stats24OutMpcpFrames=").append(stats24OutMpcpFrames);
        sb.append(", stats24OutMpcpOctets=").append(stats24OutMpcpOctets);
        sb.append(", stats24OutOAMFrames=").append(stats24OutOAMFrames);
        sb.append(", stats24OutOAMOctets=").append(stats24OutOAMOctets);
        sb.append(", stats24OutCRCErrorPkts=").append(stats24OutCRCErrorPkts);
        sb.append(", stats24OutDropEvents=").append(stats24OutDropEvents);
        sb.append(", stats24OutJabbers=").append(stats24OutJabbers);
        sb.append(", stats24OutCollision=").append(stats24OutCollision);
        sb.append(", stats24StatusAndAction=").append(stats24StatusAndAction);
        sb.append(", stats24ValidityTag=").append(stats24ValidityTag);
        sb.append(", stats24ElapsedTime=").append(stats24ElapsedTime);
        sb.append(", stats24EndTime=").append(stats24EndTime);
        sb.append(", stats24EndTimeDb=").append(stats24EndTimeDb);
        sb.append(", stats24EndTimeString='").append(stats24EndTimeString).append('\'');
        sb.append('}');
        return sb.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.facade.domain.PerformanceDomain#compare(com.topvision.ems.facade.domain
     * .PerformanceDomain)
     */
    @Override
    public boolean compare(PerformanceDomain performanceDomain) {
        PerfStats24Table table = (PerfStats24Table) performanceDomain;
        if (this.entityId.equals(table.entityId) && this.portIndex.equals(table.portIndex)) {
            return true;
        }
        return false;
    }
}
