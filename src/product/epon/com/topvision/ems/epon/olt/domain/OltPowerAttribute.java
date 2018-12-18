/***********************************************************************
 * $Id: OltPowerAttribute.java,v1.0 2011-9-26 上午09:10:39 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.domain;

import java.io.Serializable;

import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * 电源属性
 * 
 * @author zhanglongyang
 * 
 */
@TableProperty(tables = { "default", "power" })
public class OltPowerAttribute implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -3999493465612096705L;
    private Long entityId;
    private Long powerCardId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.4.1.1.1", index = true)
    private Long deviceNo = 1L;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.4.1.1.2", index = true)
    private Long powerNo;
    // TODO 电源属性中对私有MIB暂时不支持，故注释对私有MIB中电源属性的采集
    // @SnmpProperty(table = "power",oid = "1.3.6.1.4.1.32285.11.2.3.1.4.1.1.1", index = true)
    private Long topSysPowerCardNo;
    private Long powerCardIndex;
    // @SnmpProperty(table = "power",oid = "1.3.6.1.4.1.32285.11.2.3.1.4.1.1.2")
    private Integer topSysPowerSupplyType = 1;
    // @SnmpProperty(table = "power",oid = "1.3.6.1.4.1.32285.11.2.3.1.4.1.1.3")
    private Integer topSysPowerSupplyACVoltage;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.1.4.1.1.6")
    private String powerCardName;

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
     * @return the powerCardId
     */
    public Long getPowerCardId() {
        return powerCardId;
    }

    /**
     * @param powerCardId
     *            the powerCardId to set
     */
    public void setPowerCardId(Long powerCardId) {
        this.powerCardId = powerCardId;
    }

    /**
     * @return the powerCardIndex
     */
    public Long getPowerCardIndex() {
        powerCardIndex = new EponIndex(powerNo.intValue()).getSlotIndex();
        return powerCardIndex;
    }

    /**
     * @param powerCardIndex
     *            the powerCardIndex to set
     */
    public void setPowerCardIndex(Long powerCardIndex) {
        powerNo = topSysPowerCardNo = EponIndex.getSlotNo(powerCardIndex);
        this.powerCardIndex = powerCardIndex;
    }

    /**
     * @return the topSysPowerSupplyType
     */
    public Integer getTopSysPowerSupplyType() {
        return topSysPowerSupplyType;
    }

    /**
     * @param topSysPowerSupplyType
     *            the topSysPowerSupplyType to set
     */
    public void setTopSysPowerSupplyType(Integer topSysPowerSupplyType) {
        this.topSysPowerSupplyType = topSysPowerSupplyType;
    }

    /**
     * @return the topSysPowerSupplyACVoltage
     */
    public Integer getTopSysPowerSupplyACVoltage() {
        return topSysPowerSupplyACVoltage;
    }

    /**
     * @param topSysPowerSupplyACVoltage
     *            the topSysPowerSupplyACVoltage to set
     */
    public void setTopSysPowerSupplyACVoltage(Integer topSysPowerSupplyACVoltage) {
        this.topSysPowerSupplyACVoltage = topSysPowerSupplyACVoltage;
    }

    /**
     * @return the powerCardName
     */
    public String getPowerCardName() {
        return powerCardName;
    }

    /**
     * @param powerCardName
     *            the powerCardName to set
     */
    public void setPowerCardName(String powerCardName) {
        this.powerCardName = powerCardName;
    }

    public Long getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(Long deviceNo) {
        this.deviceNo = deviceNo;
    }

    public Long getPowerNo() {
        return powerNo;
    }

    public void setPowerNo(Long powerNo) {
        this.powerNo = powerNo;
    }

    public Long getTopSysPowerCardNo() {
        return topSysPowerCardNo;
    }

    public void setTopSysPowerCardNo(Long topSysPowerCardNo) {
        this.topSysPowerCardNo = topSysPowerCardNo;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OltPowerAttribute");
        sb.append("{deviceNo=").append(deviceNo);
        sb.append(", entityId=").append(entityId);
        sb.append(", powerCardId=").append(powerCardId);
        sb.append(", powerNo=").append(powerNo);
        sb.append(", topSysPowerCardNo=").append(topSysPowerCardNo);
        sb.append(", powerCardIndex=").append(powerCardIndex);
        sb.append(", topSysPowerSupplyType=").append(topSysPowerSupplyType);
        sb.append(", topSysPowerSupplyACVoltage=").append(topSysPowerSupplyACVoltage);
        sb.append(", powerCardName='").append(powerCardName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
