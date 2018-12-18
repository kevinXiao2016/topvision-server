/***********************************************************************
 * $Id: Vertex.java,v1.0 2011-9-26 下午01:17:34 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.topvision.ems.epon.olt.domain.OltSlotAttribute;
import com.topvision.ems.epon.olt.domain.OltSlotStatus;

/**
 * slot关系
 * 
 * @author lizongtian
 * 
 */
@SuppressWarnings("rawtypes")
public class Slot implements Serializable, Comparable {
    private static final long serialVersionUID = -445471800563591334L;

    private Long slotId;
    private Long slotNum;// 槽位号
    private Long slotLogicNum;// 槽位号
    private Long slotIndex;
    private Long slotRealIndex;
    private Integer type;
    private String bName; // 板卡名称
    private String bSerialNumber; // 板卡序列号
    private Long bUpTime; // 板卡在线时间
    private String bSoftwareVersion; // 软件版本
    private String bFirmwareVersion; // 固件版本
    private String bHardwareVersion; // 硬件版本
    private Integer bAttribute; // 板卡属性
    private Integer bAdminStatus; // 板卡状态
    private Integer bPresenceStatus; // 插入状态
    private Integer bAlarmStatus; // 板卡告警状态
    private Integer bOperationStatus;// 板卡可操作状态
    private Long changeTime; // 更新时间
    private Integer topSysBdCurrentTemperature;// 板卡温度
    private Integer topSysBdLampStatus; // led灯状态
    private Integer topSysBdCPUUseRatio; // 板卡CPU利用率
    private Long topSysBdlMemSize; // 板卡内存大小
    private Long topSysBdFreeMemSize; // 空闲内存大小
    private Long topTotalFlashOctets; // 总共FLASH容量
    private Long topSysBdFreeFlashOctets; // 剩余Flash容量
    private Integer topSysBdPreConfigType; // 期望板卡类型
    private Integer topSysBdActualType; // 实际板卡类型
    private Integer flag;// 期望板卡与实际板卡是否一致
    private Integer topSysBdTempDetectEnable;
    private List<EponPort> portList = new ArrayList<EponPort>();// 板卡端口列表
    //温度展示
    private Integer slotDisplayTemp;

    public void setAttribute(OltSlotAttribute oltSlotAttribute, OltSlotStatus oltSlotStatus) {
        this.slotRealIndex = this.slotNum = oltSlotAttribute.getSlotNo();
        this.slotLogicNum = oltSlotAttribute.getBmapSlotLogNo();
        this.type = oltSlotAttribute.getTopSysBdActualType();
        this.bName = oltSlotAttribute.getbName(); // 板卡名称
        this.bSerialNumber = oltSlotAttribute.getbSerialNumber(); // 板卡序列号
        this.bUpTime = oltSlotAttribute.getbUpTime(); // 板卡在线时间
        this.bSoftwareVersion = oltSlotAttribute.getbSoftwareVersion(); // 软件版本
        this.bFirmwareVersion = oltSlotAttribute.getbFirmwareVersion(); // 固件版本
        this.bHardwareVersion = oltSlotAttribute.getbHardwareVersion(); // 硬件版本
        this.bAttribute = oltSlotAttribute.getbAttribute(); // 板卡属性
        this.bAdminStatus = oltSlotAttribute.getbAdminStatus(); // 板卡状态
        this.bPresenceStatus = oltSlotAttribute.getbPresenceStatus(); // 插入状态
        this.bAlarmStatus = oltSlotStatus.getbAlarmStatus(); // 板卡告警状态
        this.changeTime = oltSlotStatus.getChangeTime().getTime(); // 更新时
        this.topSysBdTempDetectEnable = oltSlotAttribute.getTopSysBdTempDetectEnable();// 板卡温度检查使能
        this.topSysBdCurrentTemperature = oltSlotStatus.getTopSysBdCurrentTemperature();// 板卡温度
        this.topSysBdLampStatus = oltSlotStatus.getTopSysBdLampStatus(); // led灯状态
        this.topSysBdCPUUseRatio = oltSlotStatus.getTopSysBdCpuUseRatio(); // 板卡CPU利用率
        this.topSysBdlMemSize = oltSlotStatus.getTopSysBdlMemSize(); // 板卡内存大小
        this.topSysBdFreeMemSize = oltSlotStatus.getTopSysBdFreeMemSize(); // 空闲内存大小
        this.topTotalFlashOctets = oltSlotStatus.getTopTotalFlashOctets(); // 总共FLASH容量
        this.topSysBdFreeFlashOctets = oltSlotStatus.getTopSysBdFreeFlashOctets(); // 剩余Flash容量
        this.topSysBdPreConfigType = oltSlotAttribute.getTopSysBdPreConfigType(); // 期望板卡类型
        this.topSysBdActualType = oltSlotAttribute.getTopSysBdActualType(); // 实际板卡类型
        this.bOperationStatus = oltSlotAttribute.getbOperationStatus(); // 板卡可操作状态
        this.slotDisplayTemp = oltSlotStatus.getSlotDisplayTemp(); //板卡温度展示
    }

    public Long getSlotLogicNum() {
        return slotLogicNum;
    }

    public void setSlotLogicNum(Long slotLogicNum) {
        this.slotLogicNum = slotLogicNum;
    }

    public Integer getBAlarmStatus() {
        return bAlarmStatus;
    }

    public void setBAlarmStatus(Integer bAlarmStatus) {
        this.bAlarmStatus = bAlarmStatus;
    }

    public Integer getBAttribute() {
        return bAttribute;
    }

    public void setBAttribute(Integer bAttribute) {
        this.bAttribute = bAttribute;
    }

    public String getBFirmwareVersion() {
        return bFirmwareVersion;
    }

    public void setBFirmwareVersion(String bFirmwareVersion) {
        this.bFirmwareVersion = bFirmwareVersion;
    }

    public String getBHardwareVersion() {
        return bHardwareVersion;
    }

    public void setBHardwareVersion(String bHardwareVersion) {
        this.bHardwareVersion = bHardwareVersion;
    }

    public String getBName() {
        return bName;
    }

    public void setBName(String bName) {
        this.bName = bName;
    }

    public Integer getBPresenceStatus() {
        return bPresenceStatus;
    }

    public void setBPresenceStatus(Integer bPresenceStatus) {
        this.bPresenceStatus = bPresenceStatus;
    }

    public String getBSerialNumber() {
        return bSerialNumber;
    }

    public void setBSerialNumber(String bSerialNumber) {
        this.bSerialNumber = bSerialNumber;
    }

    public String getBSoftwareVersion() {
        return bSoftwareVersion;
    }

    public void setBSoftwareVersion(String bSoftwareVersion) {
        this.bSoftwareVersion = bSoftwareVersion;
    }

    public Long getBUpTime() {
        return bUpTime;
    }

    public void setBUpTime(Long bUpTime) {
        this.bUpTime = bUpTime;
    }

    public Long getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Long changeTime) {
        this.changeTime = changeTime;
    }

    public List<EponPort> getPortList() {
        return portList;
    }

    public void setPortList(List<EponPort> portList) {
        this.portList = portList;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public Long getSlotIndex() {
        return slotIndex;
    }

    public void setSlotIndex(Long slotIndex) {
        this.slotIndex = slotIndex;
    }

    public Integer getTopSysBdActualType() {
        return topSysBdActualType;
    }

    public void setTopSysBdActualType(Integer topSysBdActualType) {
        this.topSysBdActualType = topSysBdActualType;
    }

    public Integer getTopSysBdCPUUseRatio() {
        return topSysBdCPUUseRatio;
    }

    public void setTopSysBdCPUUseRatio(Integer topSysBdCPUUseRatio) {
        this.topSysBdCPUUseRatio = topSysBdCPUUseRatio;
    }

    public Long getTopSysBdFreeFlashOctets() {
        return topSysBdFreeFlashOctets;
    }

    public void setTopSysBdFreeFlashOctets(Long topSysBdFreeFlashOctets) {
        this.topSysBdFreeFlashOctets = topSysBdFreeFlashOctets;
    }

    public Long getTopSysBdFreeMemSize() {
        return topSysBdFreeMemSize;
    }

    public void setTopSysBdFreeMemSize(Long topSysBdFreeMemSize) {
        this.topSysBdFreeMemSize = topSysBdFreeMemSize;
    }

    public Integer getTopSysBdLampStatus() {
        return topSysBdLampStatus;
    }

    public void setTopSysBdLampStatus(Integer topSysBdLampStatus) {
        this.topSysBdLampStatus = topSysBdLampStatus;
    }

    public Long getTopSysBdlMemSize() {
        return topSysBdlMemSize;
    }

    public void setTopSysBdlMemSize(Long topSysBdlMemSize) {
        this.topSysBdlMemSize = topSysBdlMemSize;
    }

    public Integer getTopSysBdPreConfigType() {
        return topSysBdPreConfigType;
    }

    public void setTopSysBdPreConfigType(Integer topSysBdPreConfigType) {
        this.topSysBdPreConfigType = topSysBdPreConfigType;
    }

    public Long getTopTotalFlashOctets() {
        return topTotalFlashOctets;
    }

    public void setTopTotalFlashOctets(Long topTotalFlashOctets) {
        this.topTotalFlashOctets = topTotalFlashOctets;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Long getSlotNum() {
        return slotNum;
    }

    public void setSlotNum(Long slotNum) {
        this.slotNum = slotNum;
    }

    public Long getSlotRealIndex() {
        return slotRealIndex;
    }

    public void setSlotRealIndex(Long slotRealIndex) {
        this.slotRealIndex = slotRealIndex;
    }

    public Integer getTopSysBdCurrentTemperature() {
        return topSysBdCurrentTemperature;
    }

    public void setTopSysBdCurrentTemperature(Integer topSysBdCurrentTemperature) {
        this.topSysBdCurrentTemperature = topSysBdCurrentTemperature;
    }

    /**
     * @return the topSysBdTempDetectEnable
     */
    public Integer getTopSysBdTempDetectEnable() {
        return topSysBdTempDetectEnable;
    }

    /**
     * @param topSysBdTempDetectEnable
     *            the topSysBdTempDetectEnable to set
     */
    public void setTopSysBdTempDetectEnable(Integer topSysBdTempDetectEnable) {
        this.topSysBdTempDetectEnable = topSysBdTempDetectEnable;
    }

    /**
     * @return the bName
     */
    public String getbName() {
        return bName;
    }

    /**
     * @param bName
     *            the bName to set
     */
    public void setbName(String bName) {
        this.bName = bName;
    }

    /**
     * @return the bUpTime
     */
    public Long getbUpTime() {
        return bUpTime;
    }

    /**
     * @param bUpTime
     *            the bUpTime to set
     */
    public void setbUpTime(Long bUpTime) {
        this.bUpTime = bUpTime;
    }

    /**
     * @return the bSoftwareVersion
     */
    public String getbSoftwareVersion() {
        return bSoftwareVersion;
    }

    /**
     * @param bSoftwareVersion
     *            the bSoftwareVersion to set
     */
    public void setbSoftwareVersion(String bSoftwareVersion) {
        this.bSoftwareVersion = bSoftwareVersion;
    }

    /**
     * @return the bFirmwareVersion
     */
    public String getbFirmwareVersion() {
        return bFirmwareVersion;
    }

    /**
     * @param bFirmwareVersion
     *            the bFirmwareVersion to set
     */
    public void setbFirmwareVersion(String bFirmwareVersion) {
        this.bFirmwareVersion = bFirmwareVersion;
    }

    /**
     * @return the bHardwareVersion
     */
    public String getbHardwareVersion() {
        return bHardwareVersion;
    }

    /**
     * @param bHardwareVersion
     *            the bHardwareVersion to set
     */
    public void setbHardwareVersion(String bHardwareVersion) {
        this.bHardwareVersion = bHardwareVersion;
    }

    /**
     * @return the bAttribute
     */
    public Integer getbAttribute() {
        return bAttribute;
    }

    /**
     * @param bAttribute
     *            the bAttribute to set
     */
    public void setbAttribute(Integer bAttribute) {
        this.bAttribute = bAttribute;
    }

    /**
     * @return the bAdminStatus
     */
    public Integer getbAdminStatus() {
        return bAdminStatus;
    }

    /**
     * @param bAdminStatus
     *            the bAdminStatus to set
     */
    public void setbAdminStatus(Integer bAdminStatus) {
        this.bAdminStatus = bAdminStatus;
    }

    /**
     * @return the bAdminStatus
     */
    public Integer getBAdminStatus() {
        return bAdminStatus;
    }

    /**
     * @param bAdminStatus
     *            the bAdminStatus to set
     */
    public void setBAdminStatus(Integer bAdminStatus) {
        this.bAdminStatus = bAdminStatus;
    }

    /**
     * @return the bPresenceStatus
     */
    public Integer getbPresenceStatus() {
        return bPresenceStatus;
    }

    /**
     * @param bPresenceStatus
     *            the bPresenceStatus to set
     */
    public void setbPresenceStatus(Integer bPresenceStatus) {
        this.bPresenceStatus = bPresenceStatus;
    }

    /**
     * @return the bAlarmStatus
     */
    public Integer getbAlarmStatus() {
        return bAlarmStatus;
    }

    /**
     * @param bAlarmStatus
     *            the bAlarmStatus to set
     */
    public void setbAlarmStatus(Integer bAlarmStatus) {
        this.bAlarmStatus = bAlarmStatus;
    }

    /**
     * @return the bOperationStatus
     */
    public Integer getbOperationStatus() {
        return bOperationStatus;
    }

    /**
     * @param bOperationStatus
     *            the bOperationStatus to set
     */
    public void setbOperationStatus(Integer bOperationStatus) {
        this.bOperationStatus = bOperationStatus;
    }

    /**
     * @return the bSerialNumber
     */
    public String getbSerialNumber() {
        return bSerialNumber;
    }

    /**
     * @param bSerialNumber
     *            the bSerialNumber to set
     */
    public void setbSerialNumber(String bSerialNumber) {
        this.bSerialNumber = bSerialNumber;
    }

    public Integer getSlotDisplayTemp() {
        return slotDisplayTemp;
    }

    public void setSlotDisplayTemp(Integer slotDisplayTemp) {
        this.slotDisplayTemp = slotDisplayTemp;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Slot");
        sb.append("{bAlarmStatus=").append(bAlarmStatus);
        sb.append(", slotId=").append(slotId);
        sb.append(", slotNum=").append(slotNum);
        sb.append(", slotIndex=").append(slotIndex);
        sb.append(", slotRealIndex=").append(slotRealIndex);
        sb.append(", type=").append(type);
        sb.append(", bName='").append(bName).append('\'');
        sb.append(", bSerialNumber='").append(bSerialNumber).append('\'');
        sb.append(", bUpTime=").append(bUpTime);
        sb.append(", bSoftwareVersion='").append(bSoftwareVersion).append('\'');
        sb.append(", bFirmwareVersion='").append(bFirmwareVersion).append('\'');
        sb.append(", bHardwareVersion='").append(bHardwareVersion).append('\'');
        sb.append(", bAttribute=").append(bAttribute);
        sb.append(", bPresenceStatus=").append(bPresenceStatus);
        sb.append(", changeTime=").append(changeTime);
        sb.append(", topSysBdLampStatus=").append(topSysBdLampStatus);
        sb.append(", topSysBdCPUUseRatio=").append(topSysBdCPUUseRatio);
        sb.append(", topSysBdlMemSize=").append(topSysBdlMemSize);
        sb.append(", topSysBdFreeMemSize=").append(topSysBdFreeMemSize);
        sb.append(", topTotalFlashOctets=").append(topTotalFlashOctets);
        sb.append(", topSysBdFreeFlashOctets=").append(topSysBdFreeFlashOctets);
        sb.append(", topSysBdPreConfigType=").append(topSysBdPreConfigType);
        sb.append(", topSysBdActualType=").append(topSysBdActualType);
        sb.append(", flag=").append(flag);
        sb.append(", portList=").append(portList);
        sb.append('}');
        return sb.toString();
    }

    /**
     * Compares this object with the specified object for order. Returns a negative integer, zero,
     * or a positive integer as this object is less than, equal to, or greater than the specified
     * object.
     * <p/>
     * <p>
     * The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>. (This implies that
     * <tt>x.compareTo(y)</tt> must throw an exception iff <tt>y.compareTo(x)</tt> throws an
     * exception.)
     * <p/>
     * <p>
     * The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     * <p/>
     * <p>
     * Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt> implies that
     * <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for all <tt>z</tt>.
     * <p/>
     * <p>
     * It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>. Generally speaking, any class that implements
     * the <tt>Comparable</tt> interface and violates this condition should clearly indicate this
     * fact. The recommended language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     * <p/>
     * <p>
     * In the foregoing description, the notation <tt>sgn(</tt><i>expression</i><tt>)</tt>
     * designates the mathematical <i>signum</i> function, which is defined to return one of
     * <tt>-1</tt>, <tt>0</tt>, or <tt>1</tt> according to whether the value of <i>expression</i> is
     * negative, zero or positive.
     * 
     * @param o
     *            the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object is less than, equal
     *         to, or greater than the specified object.
     * @throws ClassCastException
     *             if the specified object's type prevents it from being compared to this object.
     */
    public int compareTo(Object o) {
        Slot slot = (Slot) o;
        if (this.getSlotNum() > slot.getSlotNum()) {
            return 1;
        } else if (this.getSlotNum().equals(slot.getSlotNum())) {
            return 0;
        } else {
            return -1;
        }
    }
}
