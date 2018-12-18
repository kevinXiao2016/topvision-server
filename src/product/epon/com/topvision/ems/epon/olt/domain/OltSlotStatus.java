/***********************************************************************
 * $Id: OltSlotStatus.java,v1.0 2011-9-26 上午09:05:12 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * SLOT实时状态
 * 
 * @author zhanglongyang
 * 
 */
@TableProperty(tables = { "default", "top", "bmap" })
public class OltSlotStatus implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 3356739335102291631L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.3.1.1.1", index = true)
    private Long deviceNo = 1L;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.3.1.1.2,V1.10.0.2:1.3.6.1.4.1.32285.11.2.3.1.3.3.1.2", index = true)
    private Long slotNo;
    // @SnmpProperty(table="bmap", oid = "1.3.6.1.4.1.32285.11.2.3.1.3.2.1.1", index = true)
    private Long bmapSlotPhyNo;
    // @SnmpProperty(table="bmap", oid = "1.3.6.1.4.1.32285.11.2.3.1.3.2.1.2")
    private Long bmapSlotLogNo;
    private Long slotId;
    private Long slotIndex;
    @SnmpProperty(table = "top", oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.1", index = true)
    private Long topSysBdSlotNo;
    @SnmpProperty(table = "top", oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.4")
    private Integer topSysBdCpuUseRatio;

    // modify by victor @2012/2/12 16:43设备修改为MB的单位，需要处理转换为Byte。
    @SnmpProperty(table = "top", oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.5")
    private Long topSysBdlMemSizeMB;
    private Long topSysBdlMemSize;

    @SnmpProperty(table = "top", oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.6")
    private Long topSysBdFreeMemSize;
    private Long topSysBdFreeMemSizeMB;
    @SnmpProperty(table = "top", oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.7")
    private Long topTotalFlashOctetsKB;
    private Long topTotalFlashOctets;
    @SnmpProperty(table = "top", oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.8")
    private Long topSysBdFreeFlashOctetsKB;
    private Long topSysBdFreeFlashOctets;
    @SnmpProperty(table = "top", oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.9")
    private Integer topSysBdCurrentTemperature;
    @SnmpProperty(table = "top", oid = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.11")
    private Integer topSysBdLampStatus;
    private List<Integer> lampStatusList;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.3.1.1.4,V1.10.0.2:1.3.6.1.4.1.32285.11.2.3.1.3.3.1.4")
    private Integer attribute;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.3.1.1.11,V1.10.0.2:1.3.6.1.4.1.32285.11.2.3.1.3.3.1.11")
    private String alarmStatus;
    private Integer bAlarmStatus;
    private Timestamp changeTime;

    // 温度展示
    private Integer slotDisplayTemp;

    public Long getBmapSlotLogNo() {
        switch (attribute) {
        case 1: {
            bmapSlotLogNo = 0L;
            break;
        }
        case 2: {
            bmapSlotLogNo = 255L;
            break;
        }
        default: {
            bmapSlotLogNo = slotNo;
        }
        }
        return bmapSlotLogNo;
    }

    public void setBmapSlotLogNo(Long bmapSlotLogNo) {
        this.bmapSlotLogNo = bmapSlotLogNo;
    }

    public Long getBmapSlotPhyNo() {
        return bmapSlotPhyNo;
    }

    public void setBmapSlotPhyNo(Long bmapSlotPhyNo) {
        this.bmapSlotPhyNo = bmapSlotPhyNo;
    }

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the slotId
     */
    public Long getSlotId() {
        return slotId;
    }

    /**
     * @param slotId
     *            the slotId to set
     */
    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    /**
     * @return the slotIndex
     */
    public Long getSlotIndex() {
        slotIndex = EponIndex.getSlotIndex(getBmapSlotLogNo().intValue());
        return slotIndex;
    }

    /**
     * @param slotIndex
     *            the slotIndex to set
     */
    public void setSlotIndex(Long slotIndex) {
        this.slotIndex = slotIndex;
        bmapSlotLogNo = EponIndex.getSlotNo(slotIndex);
        if (bmapSlotLogNo != 0) {
            slotNo = topSysBdSlotNo = bmapSlotPhyNo = bmapSlotLogNo;
        }
    }

    /**
     * @return the topSysBdCpuUseRatio
     */
    public Integer getTopSysBdCpuUseRatio() {
        return topSysBdCpuUseRatio;
    }

    /**
     * @param topSysBdCpuUseRatio
     *            the topSysBdCpuUseRatio to set
     */
    public void setTopSysBdCpuUseRatio(Integer topSysBdCpuUseRatio) {
        this.topSysBdCpuUseRatio = topSysBdCpuUseRatio;
    }

    // modify by victor @2012/2/12 16:43设备修改为MB的单位，需要处理转换为Byte。
    /**
     * @return the topSysBdlMemSize
     */
    public Long getTopSysBdlMemSize() {
        return topSysBdlMemSize;
    }

    /**
     * @param topSysBdlMemSize
     *            the topSysBdlMemSize to set
     */
    public void setTopSysBdlMemSize(Long topSysBdlMemSize) {
        this.topSysBdlMemSize = topSysBdlMemSize;
        if (topSysBdlMemSize != null) {
            this.topSysBdlMemSizeMB = topSysBdlMemSize / 1024 / 1024;
        }
    }

    /**
     * @return the topSysBdlMemSizeMB
     */
    public Long getTopSysBdlMemSizeMB() {
        return topSysBdlMemSizeMB;
    }

    /**
     * @param topSysBdlMemSizeMB
     *            the topSysBdlMemSizeMB to set
     */
    public void setTopSysBdlMemSizeMB(Long topSysBdlMemSizeMB) {
        this.topSysBdlMemSizeMB = topSysBdlMemSizeMB;
        this.topSysBdlMemSize = topSysBdlMemSizeMB * 1024 * 1024L;
    }

    /**
     * @return the topSysBdFreeMemSize
     */
    public Long getTopSysBdFreeMemSize() {
        return topSysBdFreeMemSize;
    }

    /**
     * @param topSysBdFreeMemSize
     *            the topSysBdFreeMemSize to set
     */
    public void setTopSysBdFreeMemSize(Long topSysBdFreeMemSize) {
        this.topSysBdFreeMemSize = topSysBdFreeMemSize;
        if (topSysBdFreeMemSize != null) {
            this.topSysBdFreeMemSizeMB = topSysBdFreeMemSize / 1024 / 1024;
        }
    }

    /**
     * @return the topTotalFlashOctets
     */
    public Long getTopTotalFlashOctets() {
        return topTotalFlashOctets;
    }

    /**
     * @param topTotalFlashOctets
     *            the topTotalFlashOctets to set
     */
    public void setTopTotalFlashOctets(Long topTotalFlashOctets) {
        this.topTotalFlashOctets = topTotalFlashOctets;
        if (topTotalFlashOctets != null) {
            this.topTotalFlashOctetsKB = topTotalFlashOctets / 1024;
        }
    }

    /**
     * @return the topTotalFlashOctetsKB
     */
    public Long getTopTotalFlashOctetsKB() {
        return topTotalFlashOctetsKB;
    }

    /**
     * @param topTotalFlashOctetsKB
     *            the topTotalFlashOctetsKB to set
     */
    public void setTopTotalFlashOctetsKB(Long topTotalFlashOctetsKB) {
        this.topTotalFlashOctetsKB = topTotalFlashOctetsKB;
        this.topTotalFlashOctets = topTotalFlashOctetsKB * 1024;
    }

    /**
     * @return the topSysBdFreeFlashOctets
     */
    public Long getTopSysBdFreeFlashOctets() {
        return topSysBdFreeFlashOctets;
    }

    /**
     * @param topSysBdFreeFlashOctets
     *            the topSysBdFreeFlashOctets to set
     */
    public void setTopSysBdFreeFlashOctets(Long topSysBdFreeFlashOctets) {
        this.topSysBdFreeFlashOctets = topSysBdFreeFlashOctets;
        if (topSysBdFreeFlashOctets != null) {
            this.topSysBdFreeFlashOctetsKB = topSysBdFreeFlashOctets / 1024;
        }
    }

    /**
     * @return the topSysBdCurrentTemperature
     */
    public Integer getTopSysBdCurrentTemperature() {
        return topSysBdCurrentTemperature;
    }

    /**
     * @param topSysBdCurrentTemperature
     *            the topSysBdCurrentTemperature to set
     */
    public void setTopSysBdCurrentTemperature(Integer topSysBdCurrentTemperature) {
        this.topSysBdCurrentTemperature = topSysBdCurrentTemperature;
        if (topSysBdCurrentTemperature != null) {
            this.slotDisplayTemp = UnitConfigConstant.translateTemperature(topSysBdCurrentTemperature);
        }
    }

    /**
     * @return the topSysBdLampStatus
     */
    public Integer getTopSysBdLampStatus() {
        return topSysBdLampStatus;
    }

    /**
     * @param topSysBdLampStatus
     *            the topSysBdLampStatus to set
     */
    public void setTopSysBdLampStatus(Integer topSysBdLampStatus) {
        this.topSysBdLampStatus = topSysBdLampStatus;
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
     * @return the alarmStatus
     */
    public String getAlarmStatus() {
        return alarmStatus;
    }

    // critical(0), major(1), minor(2), warning(3) , nowarning(4)
    /**
     * @param alarmStatus
     *            the alarmStatus to set
     */
    public void setAlarmStatus(String alarmStatus) {
        this.alarmStatus = alarmStatus;
        if (alarmStatus == null || alarmStatus.length() == 0) {
            bAlarmStatus = -1;
        } else if (Integer.parseInt(alarmStatus, 16) >= Integer.parseInt("80", 16)) {
            bAlarmStatus = 0;
        } else if (Integer.parseInt(alarmStatus, 16) < Integer.parseInt("80", 16)
                && Integer.parseInt(alarmStatus, 16) >= Integer.parseInt("40", 16)) {
            bAlarmStatus = 1;
        } else if (Integer.parseInt(alarmStatus, 16) < Integer.parseInt("40", 16)
                && Integer.parseInt(alarmStatus, 16) >= Integer.parseInt("20", 16)) {
            bAlarmStatus = 2;
        } else if (Integer.parseInt(alarmStatus, 16) < Integer.parseInt("20", 16)
                && Integer.parseInt(alarmStatus, 16) >= Integer.parseInt("10", 16)) {
            bAlarmStatus = 3;
        } else {
            bAlarmStatus = -1;
        }
    }

    /**
     * @return the topSysBdFreeFlashOctetsKB
     */
    public Long getTopSysBdFreeFlashOctetsKB() {
        return topSysBdFreeFlashOctetsKB;
    }

    /**
     * @param topSysBdFreeFlashOctetsKB
     *            the topSysBdFreeFlashOctetsKB to set
     */
    public void setTopSysBdFreeFlashOctetsKB(Long topSysBdFreeFlashOctetsKB) {
        this.topSysBdFreeFlashOctetsKB = topSysBdFreeFlashOctetsKB;
        this.topSysBdFreeFlashOctets = topSysBdFreeFlashOctetsKB * 1024;
    }

    /**
     * @return the attribute
     */
    public Integer getAttribute() {
        return attribute;
    }

    /**
     * @param attribute
     *            the attribute to set
     */
    public void setAttribute(Integer attribute) {
        this.attribute = attribute;
    }

    /**
     * @return the changeTime
     */
    public Timestamp getChangeTime() {
        return changeTime;
    }

    /**
     * @param changeTime
     *            the changeTime to set
     */
    public void setChangeTime(Timestamp changeTime) {
        this.changeTime = changeTime;
    }

    public Long getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(Long deviceNo) {
        this.deviceNo = deviceNo;
    }

    public Long getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(Long slotNo) {
        this.slotNo = this.topSysBdSlotNo = this.bmapSlotPhyNo = slotNo;
    }

    public Long getTopSysBdSlotNo() {
        return topSysBdSlotNo;
    }

    public void setTopSysBdSlotNo(Long topSysBdSlotNo) {
        this.topSysBdSlotNo = topSysBdSlotNo;
    }

    public Integer getBAlarmStatus() {
        return bAlarmStatus;
    }

    public void setBAlarmStatus(Integer bAlarmStatus) {
        this.bAlarmStatus = bAlarmStatus;
    }/*
      * (non-Javadoc)
      * 
      * @see java.lang.Object#toString()
      */

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OltSlotStatus");
        sb.append("{bAlarmStatus=").append(bAlarmStatus);
        sb.append(", bAttribute=").append(attribute);
        sb.append(", entityId=").append(entityId);
        sb.append(", deviceNo=").append(deviceNo);
        sb.append(", slotNo=").append(slotNo);
        sb.append(", slotId=").append(slotId);
        sb.append(", slotIndex=").append(slotIndex);
        sb.append(", topSysBdSlotNo=").append(topSysBdSlotNo);
        sb.append(", topSysBdCpuUseRatio=").append(topSysBdCpuUseRatio);
        sb.append(", topSysBdlMemSize=").append(topSysBdlMemSize);
        sb.append(", topSysBdFreeMemSize=").append(topSysBdFreeMemSize);
        sb.append(", topTotalFlashOctets=").append(topTotalFlashOctets);
        sb.append(", topSysBdFreeFlashOctets=").append(topSysBdFreeFlashOctets);
        sb.append(", topSysBdCurrentTemperature=").append(topSysBdCurrentTemperature);
        sb.append(", topSysBdLampStatus=").append(topSysBdLampStatus);
        sb.append(", changeTime=").append(changeTime);
        sb.append('}');
        return sb.toString();
    }

    /**
     * @return the topSysBdFreeMemSizeMB
     */
    public Long getTopSysBdFreeMemSizeMB() {
        return topSysBdFreeMemSizeMB;
    }

    /**
     * @param topSysBdFreeMemSizeMB
     *            the topSysBdFreeMemSizeMB to set
     */
    public void setTopSysBdFreeMemSizeMB(Long topSysBdFreeMemSizeMB) {
        this.topSysBdFreeMemSizeMB = topSysBdFreeMemSizeMB;
        this.topSysBdFreeMemSize = topSysBdFreeMemSizeMB * 1024 * 1024L;
    }

    /**
     * @return the lampStatusList
     */
    public List<Integer> getLampStatusList() {
        return lampStatusList;
    }

    /**
     * @param lampStatusList
     *            the lampStatusList to set
     */
    public void setLampStatusList(List<Integer> lampStatusList) {
        this.lampStatusList = lampStatusList;
    }

    public Integer getSlotDisplayTemp() {
        return slotDisplayTemp;
    }

    public void setSlotDisplayTemp(Integer slotDisplayTemp) {
        this.slotDisplayTemp = slotDisplayTemp;
    }

}
