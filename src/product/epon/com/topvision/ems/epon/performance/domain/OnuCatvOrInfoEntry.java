/***********************************************************************
 * $Id: OnuCatvOrInfoEntry.java,v1.0 2016-5-7 下午1:50:19 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Administrator
 * @created @2016-5-7-下午1:50:19
 *
 */
public class OnuCatvOrInfoEntry implements AliasesSuperType {
    private static final long serialVersionUID = 6125241969532711829L;
    private Long entityId;
    private Long onuId;
    private Long onuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.5.1.2.1.1", index = true)
    private Long onuCatvOrInfoDeviceIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.5.1.2.1.2", type = "Integer32")
    private Integer onuCatvOrInfoRxPower;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.5.1.2.1.3", type = "Integer32")
    private Integer onuCatvOrInfoRfOutVoltage;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.5.1.2.1.4", type = "Integer32")
    private Integer onuCatvOrInfoVoltage;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.5.1.2.1.5", type = "Integer32")
    private Integer onuCatvOrInfoTemperature;

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
     * @return the onuId
     */
    public Long getOnuId() {
        return onuId;
    }

    /**
     * @param onuId
     *            the onuId to set
     */
    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    /**
     * @return the onuIndex
     */
    public Long getOnuIndex() {
        return onuIndex;
    }

    /**
     * @param onuIndex
     *            the onuIndex to set
     */
    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        if (onuIndex != null) {
            onuCatvOrInfoDeviceIndex = EponIndex.getOnuMibIndexByIndex(onuIndex);
        }
    }

    /**
     * @return the onuCatvOrInfoDeviceIndex
     */
    public Long getOnuCatvOrInfoDeviceIndex() {
        return onuCatvOrInfoDeviceIndex;
    }

    /**
     * @param onuCatvOrInfoDeviceIndex
     *            the onuCatvOrInfoDeviceIndex to set
     */
    public void setOnuCatvOrInfoDeviceIndex(Long onuCatvOrInfoDeviceIndex) {
        this.onuCatvOrInfoDeviceIndex = onuCatvOrInfoDeviceIndex;
        if (onuCatvOrInfoDeviceIndex != null) {
            onuIndex = EponIndex.getOnuIndexByMibIndex(onuCatvOrInfoDeviceIndex);
        }
    }

    /**
     * @return the onuCatvOrInfoRxPower
     */
    public Integer getOnuCatvOrInfoRxPower() {
        return onuCatvOrInfoRxPower;
    }

    /**
     * @param onuCatvOrInfoRxPower
     *            the onuCatvOrInfoRxPower to set
     */
    public void setOnuCatvOrInfoRxPower(Integer onuCatvOrInfoRxPower) {
        this.onuCatvOrInfoRxPower = onuCatvOrInfoRxPower;
    }

    /**
     * @return the onuCatvOrInfoRfOutVoltage
     */
    public Integer getOnuCatvOrInfoRfOutVoltage() {
        return onuCatvOrInfoRfOutVoltage;
    }

    /**
     * @param onuCatvOrInfoRfOutVoltage
     *            the onuCatvOrInfoRfOutVoltage to set
     */
    public void setOnuCatvOrInfoRfOutVoltage(Integer onuCatvOrInfoRfOutVoltage) {
        this.onuCatvOrInfoRfOutVoltage = onuCatvOrInfoRfOutVoltage;
    }

    /**
     * @return the onuCatvOrInfoVoltage
     */
    public Integer getOnuCatvOrInfoVoltage() {
        return onuCatvOrInfoVoltage;
    }

    /**
     * @param onuCatvOrInfoVoltage
     *            the onuCatvOrInfoVoltage to set
     */
    public void setOnuCatvOrInfoVoltage(Integer onuCatvOrInfoVoltage) {
        this.onuCatvOrInfoVoltage = onuCatvOrInfoVoltage;
    }

    /**
     * @return the onuCatvOrInfoTemperature
     */
    public Integer getOnuCatvOrInfoTemperature() {
        return onuCatvOrInfoTemperature;
    }

    /**
     * @param onuCatvOrInfoTemperature
     *            the onuCatvOrInfoTemperature to set
     */
    public void setOnuCatvOrInfoTemperature(Integer onuCatvOrInfoTemperature) {
        this.onuCatvOrInfoTemperature = onuCatvOrInfoTemperature;
    }

}
