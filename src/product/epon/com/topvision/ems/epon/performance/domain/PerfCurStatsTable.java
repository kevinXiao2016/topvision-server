/***********************************************************************
 * $ PerfCurStatsTable.java,v1.0 2011-11-21 8:34:20 $
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
import com.topvision.ems.network.util.PortUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2011-11-21-8:34:20
 */
public class PerfCurStatsTable implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Long entityId;
    /**
     * 设备索引号 For OLT, set to corresponding device/slot/port For ONU, set to 0
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.1", index = true)
    private Long deviceIndex;
    /**
     * 板卡索引号 For OLT, set to 0 For ONU, set to corresponding slot
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.2", index = true)
    private Long slotNo;
    /**
     * 端口索引号 For OLT, set to 0 For ONU, set to corresponding port
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.3", index = true)
    private Long portNo;
    private Long portIndex;
    /**
     * 接收字节数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.4")
    private String curStatsInOctets;
    private String curStatsInFlow;
    /**
     * 接收帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.5")
    private String curStatsInPkts;
    /**
     * 接收广播帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.6")
    private String curStatsInBroadcastPkts;
    /**
     * 接收组播帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.7")
    private String curStatsInMulticastPkts;
    /**
     * 接收64字节包数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.8")
    private String curStatsInPkts64Octets;
    /**
     * 接收65～127字节包数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.9")
    private String curStatsInPkts65to127Octets;
    /**
     * 接收128～255字节包数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.10")
    private String curStatsInPkts128to255Octets;
    /**
     * 接收256～511字节包数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.11")
    private String curStatsInPkts256to511Octets;
    /**
     * 接收512～1023字节包数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.12")
    private String curStatsInPkts512to1023Octets;
    /**
     * 接收1024～1518字节包数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.13")
    private String curStatsInPkts1024to1518Octets;
    /**
     * 接收1519～1522字节包数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.14")
    private String curStatsInPkts1519to1522Octets;
    /**
     * 接收超短帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.15")
    private String curStatsInUndersizePkts;
    /**
     * 接收超长帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.16")
    private String curStatsInOversizePkts;
    /**
     * 接收碎片数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.17")
    private String curStatsInFragments;
    /**
     * 接收MPCP帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.18")
    private String curStatsInMpcpFrames;
    /**
     * 接收MPCP字节数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.19")
    private String curStatsInMpcpOctets;
    /**
     * 接收OAM帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.20")
    private String curStatsInOAMFrames;
    /**
     * 接收OAM字节数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.21")
    private String curStatsInOAMOctets;
    /**
     * 接收CRC错误帧
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.22")
    private String curStatsInCRCErrorPkts;
    /**
     * 接收丢包事件次数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.23")
    private String curStatsInDropEvents;
    /**
     * 接收超长错帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.24")
    private String curStatsInJabbers;
    /**
     * 接收碰撞帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.25")
    private String curStatsInCollision;
    /**
     * 发送字节数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.26")
    private String curStatsOutOctets;
    private String curStatsOutFlow;
    /**
     * 发送帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.27")
    private String curStatsOutPkts;
    /**
     * 发送广播帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.28")
    private String curStatsOutBroadcastPkts;
    /**
     * 发送组播帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.29")
    private String curStatsOutMulticastPkts;
    /**
     * 发送64字节包数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.30")
    private String curStatsOutPkts64Octets;
    /**
     * 发送65～127字节包数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.31")
    private String curStatsOutPkts65to127Octets;
    /**
     * 发送128～255字节包数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.32")
    private String curStatsOutPkts128to255Octets;
    /**
     * 发送256～511字节包数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.33")
    private String curStatsOutPkts256to511Octets;
    /**
     * 发送512～1023字节包数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.34")
    private String curStatsOutPkts512to1023Octets;
    /**
     * 发送1024～1518字节包数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.35")
    private String curStatsOutPkts1024to1518Octets;
    /**
     * 发送1519～1522字节包数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.36")
    private String curStatsOutPkts1519to1522Octets;
    /**
     * 发送超短帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.37")
    private String curStatsOutUndersizePkts;
    /**
     * 发送超长帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.38")
    private String curStatsOutOversizePkts;
    /**
     * 发送碎片数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.39")
    private String curStatsOutFragments;
    /**
     * 发送MPCP帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.40")
    private String curStatsOutMpcpFrames;
    /**
     * 发送MPCP字节数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.41")
    private String curStatsOutMpcpOctets;
    /**
     * 发送OAM帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.42")
    private String curStatsOutOAMFrames;
    /**
     * 发送OAM字节数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.43")
    private String curStatsOutOAMOctets;
    /**
     * 发送CRC错误帧
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.44")
    private String curStatsOutCRCErrorPkts;
    /**
     * 发送丢包事件次数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.45")
    private String curStatsOutDropEvents;
    /**
     * 发送超长错帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.46")
    private String curStatsOutJabbers;
    /**
     * 发送碰撞帧数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.47")
    private String curStatsOutCollision;

    /**
     * 当前统计状态及操作 clean(2) 统计量清零
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.10.1.1.48", writable = true, type = "Integer32")
    private Integer curStatsStatusAndAction;

    private Timestamp curStatsEndTime;
    private String curStatsEndTimeString;

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

    public Long getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(Long slotNo) {
        this.slotNo = slotNo;
    }

    public Long getDeviceIndex() {
        return deviceIndex;
    }

    public void setDeviceIndex(Long deviceIndex) {
        this.deviceIndex = deviceIndex;
    }

    public String getCurStatsInBroadcastPkts() {
        return curStatsInBroadcastPkts;
    }

    public void setCurStatsInBroadcastPkts(String curStatsInBroadcastPkts) {
        this.curStatsInBroadcastPkts = curStatsInBroadcastPkts;
    }

    public String getCurStatsInCollision() {
        return curStatsInCollision;
    }

    public void setCurStatsInCollision(String curStatsInCollision) {
        this.curStatsInCollision = curStatsInCollision;
    }

    public String getCurStatsInCRCErrorPkts() {
        return curStatsInCRCErrorPkts;
    }

    public void setCurStatsInCRCErrorPkts(String curStatsInCRCErrorPkts) {
        this.curStatsInCRCErrorPkts = curStatsInCRCErrorPkts;
    }

    public String getCurStatsInDropEvents() {
        return curStatsInDropEvents;
    }

    public void setCurStatsInDropEvents(String curStatsInDropEvents) {
        this.curStatsInDropEvents = curStatsInDropEvents;
    }

    public String getCurStatsInFragments() {
        return curStatsInFragments;
    }

    public void setCurStatsInFragments(String curStatsInFragments) {
        this.curStatsInFragments = curStatsInFragments;
    }

    public String getCurStatsInJabbers() {
        return curStatsInJabbers;
    }

    public void setCurStatsInJabbers(String curStatsInJabbers) {
        this.curStatsInJabbers = curStatsInJabbers;
    }

    public String getCurStatsInMpcpFrames() {
        return curStatsInMpcpFrames;
    }

    public void setCurStatsInMpcpFrames(String curStatsInMpcpFrames) {
        this.curStatsInMpcpFrames = curStatsInMpcpFrames;
    }

    public String getCurStatsInMpcpOctets() {
        return curStatsInMpcpOctets;
    }

    public void setCurStatsInMpcpOctets(String curStatsInMpcpOctets) {
        this.curStatsInMpcpOctets = curStatsInMpcpOctets;
    }

    public String getCurStatsInMulticastPkts() {
        return curStatsInMulticastPkts;
    }

    public void setCurStatsInMulticastPkts(String curStatsInMulticastPkts) {
        this.curStatsInMulticastPkts = curStatsInMulticastPkts;
    }

    public String getCurStatsInOAMFrames() {
        return curStatsInOAMFrames;
    }

    public void setCurStatsInOAMFrames(String curStatsInOAMFrames) {
        this.curStatsInOAMFrames = curStatsInOAMFrames;
    }

    public String getCurStatsInOAMOctets() {
        return curStatsInOAMOctets;
    }

    public void setCurStatsInOAMOctets(String curStatsInOAMOctets) {
        this.curStatsInOAMOctets = curStatsInOAMOctets;
    }

    public String getCurStatsInOctets() {
        return curStatsInOctets;
    }

    public void setCurStatsInOctets(String curStatsInOctets) {
        curStatsInFlow = PortUtil.getIfRateString(Double.parseDouble(curStatsInOctets) * 8 / 30);
        this.curStatsInOctets = curStatsInOctets;
    }

    public String getCurStatsInOversizePkts() {
        return curStatsInOversizePkts;
    }

    public void setCurStatsInOversizePkts(String curStatsInOversizePkts) {
        this.curStatsInOversizePkts = curStatsInOversizePkts;
    }

    public String getCurStatsInPkts1024to1518Octets() {
        return curStatsInPkts1024to1518Octets;
    }

    public void setCurStatsInPkts1024to1518Octets(String curStatsInPkts1024to1518Octets) {
        this.curStatsInPkts1024to1518Octets = curStatsInPkts1024to1518Octets;
    }

    public String getCurStatsInPkts128to255Octets() {
        return curStatsInPkts128to255Octets;
    }

    public void setCurStatsInPkts128to255Octets(String curStatsInPkts128to255Octets) {
        this.curStatsInPkts128to255Octets = curStatsInPkts128to255Octets;
    }

    public String getCurStatsInPkts1519to1522Octets() {
        return curStatsInPkts1519to1522Octets;
    }

    public void setCurStatsInPkts1519to1522Octets(String curStatsInPkts1519to1522Octets) {
        this.curStatsInPkts1519to1522Octets = curStatsInPkts1519to1522Octets;
    }

    public String getCurStatsInPkts256to511Octets() {
        return curStatsInPkts256to511Octets;
    }

    public void setCurStatsInPkts256to511Octets(String curStatsInPkts256to511Octets) {
        this.curStatsInPkts256to511Octets = curStatsInPkts256to511Octets;
    }

    public String getCurStatsInPkts512to1023Octets() {
        return curStatsInPkts512to1023Octets;
    }

    public void setCurStatsInPkts512to1023Octets(String curStatsInPkts512to1023Octets) {
        this.curStatsInPkts512to1023Octets = curStatsInPkts512to1023Octets;
    }

    public String getCurStatsInPkts64Octets() {
        return curStatsInPkts64Octets;
    }

    public void setCurStatsInPkts64Octets(String curStatsInPkts64Octets) {
        this.curStatsInPkts64Octets = curStatsInPkts64Octets;
    }

    public String getCurStatsInPkts65to127Octets() {
        return curStatsInPkts65to127Octets;
    }

    public void setCurStatsInPkts65to127Octets(String curStatsInPkts65to127Octets) {
        this.curStatsInPkts65to127Octets = curStatsInPkts65to127Octets;
    }

    public String getCurStatsInPkts() {
        return curStatsInPkts;
    }

    public void setCurStatsInPkts(String curStatsInPkts) {
        this.curStatsInPkts = curStatsInPkts;
    }

    public String getCurStatsInUndersizePkts() {
        return curStatsInUndersizePkts;
    }

    public void setCurStatsInUndersizePkts(String curStatsInUndersizePkts) {
        this.curStatsInUndersizePkts = curStatsInUndersizePkts;
    }

    public String getCurStatsOutBroadcastPkts() {
        return curStatsOutBroadcastPkts;
    }

    public void setCurStatsOutBroadcastPkts(String curStatsOutBroadcastPkts) {
        this.curStatsOutBroadcastPkts = curStatsOutBroadcastPkts;
    }

    public String getCurStatsOutCollision() {
        return curStatsOutCollision;
    }

    public void setCurStatsOutCollision(String curStatsOutCollision) {
        this.curStatsOutCollision = curStatsOutCollision;
    }

    public String getCurStatsOutCRCErrorPkts() {
        return curStatsOutCRCErrorPkts;
    }

    public void setCurStatsOutCRCErrorPkts(String curStatsOutCRCErrorPkts) {
        this.curStatsOutCRCErrorPkts = curStatsOutCRCErrorPkts;
    }

    public String getCurStatsOutDropEvents() {
        return curStatsOutDropEvents;
    }

    public void setCurStatsOutDropEvents(String curStatsOutDropEvents) {
        this.curStatsOutDropEvents = curStatsOutDropEvents;
    }

    public String getCurStatsOutFragments() {
        return curStatsOutFragments;
    }

    public void setCurStatsOutFragments(String curStatsOutFragments) {
        this.curStatsOutFragments = curStatsOutFragments;
    }

    public String getCurStatsOutJabbers() {
        return curStatsOutJabbers;
    }

    public void setCurStatsOutJabbers(String curStatsOutJabbers) {
        this.curStatsOutJabbers = curStatsOutJabbers;
    }

    public String getCurStatsOutMpcpFrames() {
        return curStatsOutMpcpFrames;
    }

    public void setCurStatsOutMpcpFrames(String curStatsOutMpcpFrames) {
        this.curStatsOutMpcpFrames = curStatsOutMpcpFrames;
    }

    public String getCurStatsOutMpcpOctets() {
        return curStatsOutMpcpOctets;
    }

    public void setCurStatsOutMpcpOctets(String curStatsOutMpcpOctets) {
        this.curStatsOutMpcpOctets = curStatsOutMpcpOctets;
    }

    public String getCurStatsOutMulticastPkts() {
        return curStatsOutMulticastPkts;
    }

    public void setCurStatsOutMulticastPkts(String curStatsOutMulticastPkts) {
        this.curStatsOutMulticastPkts = curStatsOutMulticastPkts;
    }

    public String getCurStatsOutOAMFrames() {
        return curStatsOutOAMFrames;
    }

    public void setCurStatsOutOAMFrames(String curStatsOutOAMFrames) {
        this.curStatsOutOAMFrames = curStatsOutOAMFrames;
    }

    public String getCurStatsOutOAMOctets() {
        return curStatsOutOAMOctets;
    }

    public void setCurStatsOutOAMOctets(String curStatsOutOAMOctets) {
        this.curStatsOutOAMOctets = curStatsOutOAMOctets;
    }

    public String getCurStatsOutOctets() {
        return curStatsOutOctets;
    }

    public void setCurStatsOutOctets(String curStatsOutOctets) {
        curStatsOutFlow = PortUtil.getIfRateString(Double.parseDouble(curStatsOutOctets) * 8 / 30);
        this.curStatsOutOctets = curStatsOutOctets;
    }

    public String getCurStatsOutOversizePkts() {
        return curStatsOutOversizePkts;
    }

    public void setCurStatsOutOversizePkts(String curStatsOutOversizePkts) {
        this.curStatsOutOversizePkts = curStatsOutOversizePkts;
    }

    public String getCurStatsOutPkts1024to1518Octets() {
        return curStatsOutPkts1024to1518Octets;
    }

    public void setCurStatsOutPkts1024to1518Octets(String curStatsOutPkts1024to1518Octets) {
        this.curStatsOutPkts1024to1518Octets = curStatsOutPkts1024to1518Octets;
    }

    public String getCurStatsOutPkts128to255Octets() {
        return curStatsOutPkts128to255Octets;
    }

    public void setCurStatsOutPkts128to255Octets(String curStatsOutPkts128to255Octets) {
        this.curStatsOutPkts128to255Octets = curStatsOutPkts128to255Octets;
    }

    public String getCurStatsOutPkts1519to1522Octets() {
        return curStatsOutPkts1519to1522Octets;
    }

    public void setCurStatsOutPkts1519to1522Octets(String curStatsOutPkts1519to1522Octets) {
        this.curStatsOutPkts1519to1522Octets = curStatsOutPkts1519to1522Octets;
    }

    public String getCurStatsOutPkts256to511Octets() {
        return curStatsOutPkts256to511Octets;
    }

    public void setCurStatsOutPkts256to511Octets(String curStatsOutPkts256to511Octets) {
        this.curStatsOutPkts256to511Octets = curStatsOutPkts256to511Octets;
    }

    public String getCurStatsOutPkts512to1023Octets() {
        return curStatsOutPkts512to1023Octets;
    }

    public void setCurStatsOutPkts512to1023Octets(String curStatsOutPkts512to1023Octets) {
        this.curStatsOutPkts512to1023Octets = curStatsOutPkts512to1023Octets;
    }

    public String getCurStatsOutPkts64Octets() {
        return curStatsOutPkts64Octets;
    }

    public void setCurStatsOutPkts64Octets(String curStatsOutPkts64Octets) {
        this.curStatsOutPkts64Octets = curStatsOutPkts64Octets;
    }

    public String getCurStatsOutPkts65to127Octets() {
        return curStatsOutPkts65to127Octets;
    }

    public void setCurStatsOutPkts65to127Octets(String curStatsOutPkts65to127Octets) {
        this.curStatsOutPkts65to127Octets = curStatsOutPkts65to127Octets;
    }

    public String getCurStatsOutPkts() {
        return curStatsOutPkts;
    }

    public void setCurStatsOutPkts(String curStatsOutPkts) {
        this.curStatsOutPkts = curStatsOutPkts;
    }

    public String getCurStatsOutUndersizePkts() {
        return curStatsOutUndersizePkts;
    }

    public void setCurStatsOutUndersizePkts(String curStatsOutUndersizePkts) {
        this.curStatsOutUndersizePkts = curStatsOutUndersizePkts;
    }

    public Long getPortNo() {
        return portNo;
    }

    public void setPortNo(Long portNo) {
        this.portNo = portNo;
    }

    public Integer getCurStatsStatusAndAction() {
        return curStatsStatusAndAction;
    }

    public void setCurStatsStatusAndAction(Integer curStatsStatusAndAction) {
        this.curStatsStatusAndAction = curStatsStatusAndAction;
    }

    /**
     * @return the curStatsEndTime
     */
    public Timestamp getCurStatsEndTime() {
        return curStatsEndTime;
    }

    /**
     * @param curStatsEndTime
     *            the curStatsEndTime to set
     */
    public void setCurStatsEndTime(Timestamp curStatsEndTime) {
        curStatsEndTimeString = sdf.format(curStatsEndTime);
        this.curStatsEndTime = curStatsEndTime;
    }

    /**
     * @return the curStatsEndTimeString
     */
    public String getCurStatsEndTimeString() {
        return curStatsEndTimeString;
    }

    /**
     * @param curStatsEndTimeString
     *            the curStatsEndTimeString to set
     */
    public void setCurStatsEndTimeString(String curStatsEndTimeString) {
        try {
            curStatsEndTime = new Timestamp(sdf.parse(curStatsEndTimeString).getTime());
        } catch (ParseException e) {
        }
        this.curStatsEndTimeString = curStatsEndTimeString;
    }

    public String getCurStatsInFlow() {
        return curStatsInFlow;
    }

    public void setCurStatsInFlow(String curStatsInFlow) {
        this.curStatsInFlow = curStatsInFlow;
    }

    public String getCurStatsOutFlow() {
        return curStatsOutFlow;
    }

    public void setCurStatsOutFlow(String curStatsOutFlow) {
        this.curStatsOutFlow = curStatsOutFlow;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PerfCurStatsTable");
        sb.append("{slotNo=").append(slotNo);
        sb.append(", deviceIndex=").append(deviceIndex);
        sb.append(", portNo=").append(portNo);
        sb.append(", curStatsInOctets=").append(curStatsInOctets);
        sb.append(", curStatsInPkts=").append(curStatsInPkts);
        sb.append(", curStatsInBroadcastPkts=").append(curStatsInBroadcastPkts);
        sb.append(", curStatsInMulticastPkts=").append(curStatsInMulticastPkts);
        sb.append(", curStatsInPkts64Octets=").append(curStatsInPkts64Octets);
        sb.append(", curStatsInPkts65to127Octets=").append(curStatsInPkts65to127Octets);
        sb.append(", curStatsInPkts128to255Octets=").append(curStatsInPkts128to255Octets);
        sb.append(", curStatsInPkts256to511Octets=").append(curStatsInPkts256to511Octets);
        sb.append(", curStatsInPkts512to1023Octets=").append(curStatsInPkts512to1023Octets);
        sb.append(", curStatsInPkts1024to1518Octets=").append(curStatsInPkts1024to1518Octets);
        sb.append(", curStatsInPkts1519to1522Octets=").append(curStatsInPkts1519to1522Octets);
        sb.append(", curStatsInUndersizePkts=").append(curStatsInUndersizePkts);
        sb.append(", curStatsInOversizePkts=").append(curStatsInOversizePkts);
        sb.append(", curStatsInFragments=").append(curStatsInFragments);
        sb.append(", curStatsInMpcpFrames=").append(curStatsInMpcpFrames);
        sb.append(", curStatsInMpcpOctets=").append(curStatsInMpcpOctets);
        sb.append(", curStatsInOAMFrames=").append(curStatsInOAMFrames);
        sb.append(", curStatsInOAMOctets=").append(curStatsInOAMOctets);
        sb.append(", curStatsInCRCErrorPkts=").append(curStatsInCRCErrorPkts);
        sb.append(", curStatsInDropEvents=").append(curStatsInDropEvents);
        sb.append(", curStatsInJabbers=").append(curStatsInJabbers);
        sb.append(", curStatsInCollision=").append(curStatsInCollision);
        sb.append(", curStatsOutOctets=").append(curStatsOutOctets);
        sb.append(", curStatsOutPkts=").append(curStatsOutPkts);
        sb.append(", curStatsOutBroadcastPkts=").append(curStatsOutBroadcastPkts);
        sb.append(", curStatsOutMulticastPkts=").append(curStatsOutMulticastPkts);
        sb.append(", curStatsOutPkts64Octets=").append(curStatsOutPkts64Octets);
        sb.append(", curStatsOutPkts65to127Octets=").append(curStatsOutPkts65to127Octets);
        sb.append(", curStatsOutPkts128to255Octets=").append(curStatsOutPkts128to255Octets);
        sb.append(", curStatsOutPkts256to511Octets=").append(curStatsOutPkts256to511Octets);
        sb.append(", curStatsOutPkts512to1023Octets=").append(curStatsOutPkts512to1023Octets);
        sb.append(", curStatsOutPkts1024to1518Octets=").append(curStatsOutPkts1024to1518Octets);
        sb.append(", curStatsOutPkts1519o1522Octets=").append(curStatsOutPkts1519to1522Octets);
        sb.append(", curStatsOutUndersizePkts=").append(curStatsOutUndersizePkts);
        sb.append(", curStatsOutOversizePkts=").append(curStatsOutOversizePkts);
        sb.append(", curStatsOutFragments=").append(curStatsOutFragments);
        sb.append(", curStatsOutMpcpFrames=").append(curStatsOutMpcpFrames);
        sb.append(", curStatsOutMpcpOctets=").append(curStatsOutMpcpOctets);
        sb.append(", curStatsOutOAMFrames=").append(curStatsOutOAMFrames);
        sb.append(", curStatsOutOAMOctets=").append(curStatsOutOAMOctets);
        sb.append(", curStatsOutCRCErrorPkts=").append(curStatsOutCRCErrorPkts);
        sb.append(", curStatsOutDropEvents=").append(curStatsOutDropEvents);
        sb.append(", curStatsOutJabbers=").append(curStatsOutJabbers);
        sb.append(", curStatsOutCollision=").append(curStatsOutCollision);
        sb.append(", curStatsStatusAndAction=").append(curStatsStatusAndAction);
        sb.append('}');
        return sb.toString();
    }
}
