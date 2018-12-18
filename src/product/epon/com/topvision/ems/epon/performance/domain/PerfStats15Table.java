/***********************************************************************
 * $ PerfStats15Table.java,v1.0 2011-11-21 8:34:40 $
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

import com.topvision.ems.facade.domain.PerformanceDomain;
import com.topvision.ems.network.util.PortUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author jay
 * @created @2011-11-21-8:34:40
 */
public class PerfStats15Table implements PerformanceDomain, Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Long entityId;
    /**
     * 设备索引号 For OLT, set to corresponding device/slot/port For ONU, set to 0
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.1", index = true)
    private Long deviceIndex;
    /**
     * 板卡索引号 For OLT, set to 0 For ONU, set to corresponding slot
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.2", index = true)
    private Long slotNo;
    /**
     * 端口索引号 For OLT, set to 0 For ONU, set to corresponding port
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.3", index = true)
    private Long portNo;
    private Long portIndex;

    /**
     * 端口类型
     */
    private Integer portType;

    /**
     * 顺序索引号
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.4", index = true)
    private Long stats15Index;
    /**
     * 接收字节数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.5")
    private String stats15InOctets;
    private String stats15InFlow;
    /**
     * 接收帧数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.6")
    private String stats15InPkts;
    /**
     * 接收广播帧数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.7")
    private String stats15InBroadcastPkts;
    /**
     * 接收组播帧数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.8")
    private String stats15InMulticastPkts;
    /**
     * 接收64字节包数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.9")
    private String stats15InPkts64Octets;
    /**
     * 接收65～127字节包数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.10")
    private String stats15InPkts65to127Octets;
    /**
     * 接收128～255字节包数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.11")
    private String stats15InPkts128to255Octets;
    /**
     * 接收256～511字节包数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.12")
    private String stats15InPkts256to511Octets;
    /**
     * 接收512～1023字节包数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.13")
    private String stats15InPkts512to1023Octets;
    /**
     * 接收1024～1518字节包数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.14")
    private String stats15InPkts1024to1518Octets;
    /**
     * 接收1519～1522字节包数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.15")
    private String stats15InPkts1519to1522Octets;
    /**
     * 接收超短帧数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.16")
    private String stats15InUndersizePkts;
    /**
     * 接收超长帧数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.17")
    private String stats15InOversizePkts;
    /**
     * 接收碎片数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.18")
    private String stats15InFragments;
    /**
     * 接收MPCP帧数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.19")
    private String stats15InMpcpFrames;
    /**
     * 接收MPCP字节数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.20")
    private String stats15InMpcpOctets;
    /**
     * 接收OAM帧数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.21")
    private String stats15InOAMFrames;
    /**
     * 接收OAM字节数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.22")
    private String stats15InOAMOctets;
    /**
     * 接收CRC错误帧
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.23")
    private String stats15InCRCErrorPkts;
    /**
     * 接收丢包事件次数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.24")
    private String stats15InDropEvents;
    /**
     * 接收超长错帧数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.25")
    private String stats15InJabbers;
    /**
     * 接收碰撞帧数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.26")
    private String stats15InCollision;
    /**
     * 发送字节数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.27")
    private String stats15OutOctets;
    private String stats15OutFlow;
    /**
     * 发送帧数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.28")
    private String stats15OutPkts;
    /**
     * 发送广播帧数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.29")
    private String stats15OutBroadcastPkts;
    /**
     * 发送组播帧数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.30")
    private String stats15OutMulticastPkts;
    /**
     * 发送64字节包数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.31")
    private String stats15OutPkts64Octets;
    /**
     * 发送65～127字节包数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.32")
    private String stats15OutPkts65to127Octets;
    /**
     * 发送128～255字节包数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.33")
    private String stats15OutPkts128to255Octets;
    /**
     * 发送256～511字节包数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.34")
    private String stats15OutPkts256to511Octets;
    /**
     * 发送512～1023字节包数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.35")
    private String stats15OutPkts512to1023Octets;
    /**
     * 发送1024～1518字节包数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.36")
    private String stats15OutPkts1024to1518Octets;
    /**
     * 发送1519～1522字节包数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.37")
    private String stats15OutPkts1519to1522Octets;
    /**
     * 发送超短帧数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.38")
    private String stats15OutUndersizePkts;
    /**
     * 发送超长帧数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.39")
    private String stats15OutOversizePkts;
    /**
     * 发送碎片数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.40")
    private String stats15OutFragments;
    /**
     * 发送MPCP帧数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.41")
    private String stats15OutMpcpFrames;
    /**
     * 发送MPCP字节数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.42")
    private String stats15OutMpcpOctets;
    /**
     * 发送OAM帧数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.43")
    private String stats15OutOAMFrames;
    /**
     * 发送OAM字节数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.44")
    private String stats15OutOAMOctets;
    /**
     * 发送CRC错误帧
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.45")
    private String stats15OutCRCErrorPkts;
    /**
     * 发送丢包事件次数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.46")
    private String stats15OutDropEvents;
    /**
     * 发送超长错帧数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.47")
    private String stats15OutJabbers;
    /**
     * 发送碰撞帧数
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.48")
    private String stats15OutCollision;

    /**
     * 当前统计状态及操作 clean(2) 统计量清零
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.49", writable = true, type = "Integer32")
    private Integer stats15StatusAndAction;
    /**
     * Whether or not the current 15-min PM data is valid
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.50")
    private Integer stats15ValidityTag;
    /**
     * It is elapsed time, since start of this 15-min interval. It is only valid for current PM
     * data. For history PM data, set to 0
     */
    // @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.51")
    private Long stats15ElapsedTime;
    /**
     * It is time, when the PM history data is created. It is only valid for history PM data
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.2.1.52")
    private String stats15EndTime;
    private Timestamp stats15EndTimeDb;
    private String stats15EndTimeString;

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

    public Long getStats15ElapsedTime() {
        return stats15ElapsedTime;
    }

    public void setStats15ElapsedTime(Long stats15ElapsedTime) {
        this.stats15ElapsedTime = stats15ElapsedTime;
    }

    public String getStats15EndTime() {
        return stats15EndTime;
    }

    public void setStats15EndTime(String stats15EndTime) {
        this.stats15EndTime = stats15EndTime;
        if (stats15EndTime != null) {
            stats15EndTimeDb = new Timestamp(Long.parseLong(stats15EndTime) > 0 ? Long.parseLong(stats15EndTime) : 1);
            stats15EndTimeString = sdf.format(new Timestamp(Long.parseLong(stats15EndTime)));
        }
    }

    public Timestamp getStats15EndTimeDb() {
        return stats15EndTimeDb;
    }

    public void setStats15EndTimeDb(Timestamp stats15EndTimeDb) {
        this.stats15EndTimeDb = stats15EndTimeDb;
        stats15EndTime = String.valueOf(stats15EndTimeDb.getTime());
        stats15EndTimeString = sdf.format(stats15EndTimeDb);
    }

    public String getStats15EndTimeString() {
        return stats15EndTimeString;
    }

    public void setStats15EndTimeString(String stats15EndTimeString) {
        this.stats15EndTimeString = stats15EndTimeString;
        try {
            stats15EndTime = String.valueOf(sdf.parse(stats15EndTimeString).getTime());
            stats15EndTimeDb = new Timestamp(Long.parseLong(stats15EndTime));
        } catch (ParseException e) {
        }
    }

    public String getStats15InBroadcastPkts() {
        return stats15InBroadcastPkts;
    }

    public void setStats15InBroadcastPkts(String stats15InBroadcastPkts) {
        this.stats15InBroadcastPkts = stats15InBroadcastPkts;
    }

    public String getStats15InCollision() {
        return stats15InCollision;
    }

    public void setStats15InCollision(String stats15InCollision) {
        this.stats15InCollision = stats15InCollision;
    }

    public String getStats15InCRCErrorPkts() {
        return stats15InCRCErrorPkts;
    }

    public void setStats15InCRCErrorPkts(String stats15InCRCErrorPkts) {
        this.stats15InCRCErrorPkts = stats15InCRCErrorPkts;
    }

    public Long getStats15Index() {
        return stats15Index;
    }

    public void setStats15Index(Long stats15Index) {
        this.stats15Index = stats15Index;
    }

    public String getStats15InDropEvents() {
        return stats15InDropEvents;
    }

    public void setStats15InDropEvents(String stats15InDropEvents) {
        this.stats15InDropEvents = stats15InDropEvents;
    }

    public String getStats15InFragments() {
        return stats15InFragments;
    }

    public void setStats15InFragments(String stats15InFragments) {
        this.stats15InFragments = stats15InFragments;
    }

    public String getStats15InJabbers() {
        return stats15InJabbers;
    }

    public void setStats15InJabbers(String stats15InJabbers) {
        this.stats15InJabbers = stats15InJabbers;
    }

    public String getStats15InMpcpFrames() {
        return stats15InMpcpFrames;
    }

    public void setStats15InMpcpFrames(String stats15InMpcpFrames) {
        this.stats15InMpcpFrames = stats15InMpcpFrames;
    }

    public String getStats15InMpcpOctets() {
        return stats15InMpcpOctets;
    }

    public void setStats15InMpcpOctets(String stats15InMpcpOctets) {
        this.stats15InMpcpOctets = stats15InMpcpOctets;
    }

    public String getStats15InMulticastPkts() {
        return stats15InMulticastPkts;
    }

    public void setStats15InMulticastPkts(String stats15InMulticastPkts) {
        this.stats15InMulticastPkts = stats15InMulticastPkts;
    }

    public String getStats15InOAMFrames() {
        return stats15InOAMFrames;
    }

    public void setStats15InOAMFrames(String stats15InOAMFrames) {
        this.stats15InOAMFrames = stats15InOAMFrames;
    }

    public String getStats15InOAMOctets() {
        return stats15InOAMOctets;
    }

    public void setStats15InOAMOctets(String stats15InOAMOctets) {
        this.stats15InOAMOctets = stats15InOAMOctets;
    }

    public String getStats15InOctets() {
        return stats15InOctets;
    }

    public void setStats15InOctets(String stats15InOctets) {
        stats15InFlow = PortUtil.getIfOctetsRateString(Double.parseDouble(stats15InOctets) * 8 / (15 * 60));
        this.stats15InOctets = stats15InOctets;
    }

    public String getStats15InOversizePkts() {
        return stats15InOversizePkts;
    }

    public void setStats15InOversizePkts(String stats15InOversizePkts) {
        this.stats15InOversizePkts = stats15InOversizePkts;
    }

    public String getStats15InPkts1024to1518Octets() {
        return stats15InPkts1024to1518Octets;
    }

    public void setStats15InPkts1024to1518Octets(String stats15InPkts1024to1518Octets) {
        this.stats15InPkts1024to1518Octets = stats15InPkts1024to1518Octets;
    }

    public String getStats15InPkts128to255Octets() {
        return stats15InPkts128to255Octets;
    }

    public void setStats15InPkts128to255Octets(String stats15InPkts128to255Octets) {
        this.stats15InPkts128to255Octets = stats15InPkts128to255Octets;
    }

    public String getStats15InPkts1519to1522Octets() {
        return stats15InPkts1519to1522Octets;
    }

    public void setStats15InPkts1519to1522Octets(String stats15InPkts1519to1522Octets) {
        this.stats15InPkts1519to1522Octets = stats15InPkts1519to1522Octets;
    }

    public String getStats15InPkts256to511Octets() {
        return stats15InPkts256to511Octets;
    }

    public void setStats15InPkts256to511Octets(String stats15InPkts256to511Octets) {
        this.stats15InPkts256to511Octets = stats15InPkts256to511Octets;
    }

    public String getStats15InPkts512to1023Octets() {
        return stats15InPkts512to1023Octets;
    }

    public void setStats15InPkts512to1023Octets(String stats15InPkts512to1023Octets) {
        this.stats15InPkts512to1023Octets = stats15InPkts512to1023Octets;
    }

    public String getStats15InPkts64Octets() {
        return stats15InPkts64Octets;
    }

    public void setStats15InPkts64Octets(String stats15InPkts64Octets) {
        this.stats15InPkts64Octets = stats15InPkts64Octets;
    }

    public String getStats15InPkts65to127Octets() {
        return stats15InPkts65to127Octets;
    }

    public void setStats15InPkts65to127Octets(String stats15InPkts65to127Octets) {
        this.stats15InPkts65to127Octets = stats15InPkts65to127Octets;
    }

    public String getStats15InPkts() {
        return stats15InPkts;
    }

    public void setStats15InPkts(String stats15InPkts) {
        this.stats15InPkts = stats15InPkts;
    }

    public String getStats15InUndersizePkts() {
        return stats15InUndersizePkts;
    }

    public void setStats15InUndersizePkts(String stats15InUndersizePkts) {
        this.stats15InUndersizePkts = stats15InUndersizePkts;
    }

    public String getStats15OutBroadcastPkts() {
        return stats15OutBroadcastPkts;
    }

    public void setStats15OutBroadcastPkts(String stats15OutBroadcastPkts) {
        this.stats15OutBroadcastPkts = stats15OutBroadcastPkts;
    }

    public String getStats15OutCollision() {
        return stats15OutCollision;
    }

    public void setStats15OutCollision(String stats15OutCollision) {
        this.stats15OutCollision = stats15OutCollision;
    }

    public String getStats15OutCRCErrorPkts() {
        return stats15OutCRCErrorPkts;
    }

    public void setStats15OutCRCErrorPkts(String stats15OutCRCErrorPkts) {
        this.stats15OutCRCErrorPkts = stats15OutCRCErrorPkts;
    }

    public String getStats15OutDropEvents() {
        return stats15OutDropEvents;
    }

    public void setStats15OutDropEvents(String stats15OutDropEvents) {
        this.stats15OutDropEvents = stats15OutDropEvents;
    }

    public String getStats15OutFragments() {
        return stats15OutFragments;
    }

    public void setStats15OutFragments(String stats15OutFragments) {
        this.stats15OutFragments = stats15OutFragments;
    }

    public String getStats15OutJabbers() {
        return stats15OutJabbers;
    }

    public void setStats15OutJabbers(String stats15OutJabbers) {
        this.stats15OutJabbers = stats15OutJabbers;
    }

    public String getStats15OutMpcpFrames() {
        return stats15OutMpcpFrames;
    }

    public void setStats15OutMpcpFrames(String stats15OutMpcpFrames) {
        this.stats15OutMpcpFrames = stats15OutMpcpFrames;
    }

    public String getStats15OutMpcpOctets() {
        return stats15OutMpcpOctets;
    }

    public void setStats15OutMpcpOctets(String stats15OutMpcpOctets) {
        this.stats15OutMpcpOctets = stats15OutMpcpOctets;
    }

    public String getStats15OutMulticastPkts() {
        return stats15OutMulticastPkts;
    }

    public void setStats15OutMulticastPkts(String stats15OutMulticastPkts) {
        this.stats15OutMulticastPkts = stats15OutMulticastPkts;
    }

    public String getStats15OutOAMFrames() {
        return stats15OutOAMFrames;
    }

    public void setStats15OutOAMFrames(String stats15OutOAMFrames) {
        this.stats15OutOAMFrames = stats15OutOAMFrames;
    }

    public String getStats15OutOAMOctets() {
        return stats15OutOAMOctets;
    }

    public void setStats15OutOAMOctets(String stats15OutOAMOctets) {
        this.stats15OutOAMOctets = stats15OutOAMOctets;
    }

    public String getStats15OutOctets() {
        return stats15OutOctets;
    }

    public void setStats15OutOctets(String stats15OutOctets) {
        stats15OutFlow = PortUtil.getIfOctetsRateString(Double.parseDouble(stats15OutOctets) * 8 / (15 * 60));
        this.stats15OutOctets = stats15OutOctets;
    }

    public String getStats15OutOversizePkts() {
        return stats15OutOversizePkts;
    }

    public void setStats15OutOversizePkts(String stats15OutOversizePkts) {
        this.stats15OutOversizePkts = stats15OutOversizePkts;
    }

    public String getStats15OutPkts1024to1518Octets() {
        return stats15OutPkts1024to1518Octets;
    }

    public void setStats15OutPkts1024to1518Octets(String stats15OutPkts1024to1518Octets) {
        this.stats15OutPkts1024to1518Octets = stats15OutPkts1024to1518Octets;
    }

    public String getStats15OutPkts128to255Octets() {
        return stats15OutPkts128to255Octets;
    }

    public void setStats15OutPkts128to255Octets(String stats15OutPkts128to255Octets) {
        this.stats15OutPkts128to255Octets = stats15OutPkts128to255Octets;
    }

    public String getStats15OutPkts1519to1522Octets() {
        return stats15OutPkts1519to1522Octets;
    }

    public void setStats15OutPkts1519to1522Octets(String stats15OutPkts1519to1522Octets) {
        this.stats15OutPkts1519to1522Octets = stats15OutPkts1519to1522Octets;
    }

    public String getStats15OutPkts256to511Octets() {
        return stats15OutPkts256to511Octets;
    }

    public void setStats15OutPkts256to511Octets(String stats15OutPkts256to511Octets) {
        this.stats15OutPkts256to511Octets = stats15OutPkts256to511Octets;
    }

    public String getStats15OutPkts512to1023Octets() {
        return stats15OutPkts512to1023Octets;
    }

    public void setStats15OutPkts512to1023Octets(String stats15OutPkts512to1023Octets) {
        this.stats15OutPkts512to1023Octets = stats15OutPkts512to1023Octets;
    }

    public String getStats15OutPkts64Octets() {
        return stats15OutPkts64Octets;
    }

    public void setStats15OutPkts64Octets(String stats15OutPkts64Octets) {
        this.stats15OutPkts64Octets = stats15OutPkts64Octets;
    }

    public String getStats15OutPkts65to127Octets() {
        return stats15OutPkts65to127Octets;
    }

    public void setStats15OutPkts65to127Octets(String stats15OutPkts65to127Octets) {
        this.stats15OutPkts65to127Octets = stats15OutPkts65to127Octets;
    }

    public String getStats15OutPkts() {
        return stats15OutPkts;
    }

    public void setStats15OutPkts(String stats15OutPkts) {
        this.stats15OutPkts = stats15OutPkts;
    }

    public String getStats15OutUndersizePkts() {
        return stats15OutUndersizePkts;
    }

    public void setStats15OutUndersizePkts(String stats15OutUndersizePkts) {
        this.stats15OutUndersizePkts = stats15OutUndersizePkts;
    }

    public Integer getStats15StatusAndAction() {
        return stats15StatusAndAction;
    }

    public void setStats15StatusAndAction(Integer stats15StatusAndAction) {
        this.stats15StatusAndAction = stats15StatusAndAction;
    }

    public Integer getStats15ValidityTag() {
        return stats15ValidityTag;
    }

    public void setStats15ValidityTag(Integer stats15ValidityTag) {
        this.stats15ValidityTag = stats15ValidityTag;
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

    public String getStats15InFlow() {
        return stats15InFlow;
    }

    public void setStats15InFlow(String stats15InFlow) {
        this.stats15InFlow = stats15InFlow;
    }

    public String getStats15OutFlow() {
        return stats15OutFlow;
    }

    public void setStats15OutFlow(String stats15OutFlow) {
        this.stats15OutFlow = stats15OutFlow;
    }

    public Integer getPortType() {
        return portType;
    }

    public void setPortType(Integer portType) {
        this.portType = portType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PerfStats15Table");
        sb.append("{deviceIndex=").append(deviceIndex);
        sb.append(", sdf=").append(sdf);
        sb.append(", entityId=").append(entityId);
        sb.append(", slotNo=").append(slotNo);
        sb.append(", portNo=").append(portNo);
        sb.append(", portIndex=").append(portIndex);
        sb.append(", stats15Index=").append(stats15Index);
        sb.append(", stats15InOctets=").append(stats15InOctets);
        sb.append(", stats15InPkts=").append(stats15InPkts);
        sb.append(", stats15InBroadcastPkts=").append(stats15InBroadcastPkts);
        sb.append(", stats15InMulticastPkts=").append(stats15InMulticastPkts);
        sb.append(", stats15InPkts64Octets=").append(stats15InPkts64Octets);
        sb.append(", stats15InPkts65to127Octets=").append(stats15InPkts65to127Octets);
        sb.append(", stats15InPkts128to255Octets=").append(stats15InPkts128to255Octets);
        sb.append(", stats15InPkts256to511Octets=").append(stats15InPkts256to511Octets);
        sb.append(", stats15InPkts512to1023Octets=").append(stats15InPkts512to1023Octets);
        sb.append(", stats15InPkts1024to1518Octets=").append(stats15InPkts1024to1518Octets);
        sb.append(", stats15InPkts1519to1522Octets=").append(stats15InPkts1519to1522Octets);
        sb.append(", stats15InUndersizePkts=").append(stats15InUndersizePkts);
        sb.append(", stats15InOversizePkts=").append(stats15InOversizePkts);
        sb.append(", stats15InFragments=").append(stats15InFragments);
        sb.append(", stats15InMpcpFrames=").append(stats15InMpcpFrames);
        sb.append(", stats15InMpcpOctets=").append(stats15InMpcpOctets);
        sb.append(", stats15InOAMFrames=").append(stats15InOAMFrames);
        sb.append(", stats15InOAMOctets=").append(stats15InOAMOctets);
        sb.append(", stats15InCRCErrorPkts=").append(stats15InCRCErrorPkts);
        sb.append(", stats15InDropEvents=").append(stats15InDropEvents);
        sb.append(", stats15InJabbers=").append(stats15InJabbers);
        sb.append(", stats15InCollision=").append(stats15InCollision);
        sb.append(", stats15OutOctets=").append(stats15OutOctets);
        sb.append(", stats15OutPkts=").append(stats15OutPkts);
        sb.append(", stats15OutBroadcastPkts=").append(stats15OutBroadcastPkts);
        sb.append(", stats15OutMulticastPkts=").append(stats15OutMulticastPkts);
        sb.append(", stats15OutPkts64Octets=").append(stats15OutPkts64Octets);
        sb.append(", stats15OutPkts65to127Octets=").append(stats15OutPkts65to127Octets);
        sb.append(", stats15OutPkts128to255Octets=").append(stats15OutPkts128to255Octets);
        sb.append(", stats15OutPkts256to511Octets=").append(stats15OutPkts256to511Octets);
        sb.append(", stats15OutPkts512to1023Octets=").append(stats15OutPkts512to1023Octets);
        sb.append(", stats15OutPkts1024to1518Octets=").append(stats15OutPkts1024to1518Octets);
        sb.append(", stats15OutPkts1519o1522Octets=").append(stats15OutPkts1519to1522Octets);
        sb.append(", stats15OutUndersizePkts=").append(stats15OutUndersizePkts);
        sb.append(", stats15OutOversizePkts=").append(stats15OutOversizePkts);
        sb.append(", stats15OutFragments=").append(stats15OutFragments);
        sb.append(", stats15OutMpcpFrames=").append(stats15OutMpcpFrames);
        sb.append(", stats15OutMpcpOctets=").append(stats15OutMpcpOctets);
        sb.append(", stats15OutOAMFrames=").append(stats15OutOAMFrames);
        sb.append(", stats15OutOAMOctets=").append(stats15OutOAMOctets);
        sb.append(", stats15OutCRCErrorPkts=").append(stats15OutCRCErrorPkts);
        sb.append(", stats15OutDropEvents=").append(stats15OutDropEvents);
        sb.append(", stats15OutJabbers=").append(stats15OutJabbers);
        sb.append(", stats15OutCollision=").append(stats15OutCollision);
        sb.append(", stats15StatusAndAction=").append(stats15StatusAndAction);
        sb.append(", stats15ValidityTag=").append(stats15ValidityTag);
        sb.append(", stats15ElapsedTime=").append(stats15ElapsedTime);
        sb.append(", stats15EndTime=").append(stats15EndTime);
        sb.append(", stats15EndTimeDb=").append(stats15EndTimeDb);
        sb.append(", stats15EndTimeString='").append(stats15EndTimeString).append('\'');
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
        PerfStats15Table table = (PerfStats15Table) performanceDomain;
        if (this.entityId.equals(table.entityId) && this.portIndex.equals(table.portIndex)) {
            return true;
        }
        return false;
    }

}
