/***********************************************************************
 * $Id: OnuCatvOrInfoResult.java,v1.0 2016-5-9 下午2:29:13 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.domain;

import java.sql.Timestamp;

import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;
import com.topvision.framework.common.NumberUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Rod John
 * @created @2016-5-9-下午2:29:13
 *
 */
public class OnuCatvOrInfoResult extends PerformanceResult<OperClass> implements AliasesSuperType {
    private static final long serialVersionUID = -2302563333500338829L;
    private Long entityId;
    private Long onuId;
    private Long onuIndex;
    private Integer onuCatvOrInfoRxPower;
    private Float rxPower;
    private Integer onuCatvOrInfoRfOutVoltage;
    private Float outVoltage;
    private Integer onuCatvOrInfoVoltage;
    private Float voltage;
    private Integer onuCatvOrInfoTemperature;
    private Float temperature;
    private Timestamp collectTime;

    /**
     * @param domain
     */
    public OnuCatvOrInfoResult(OperClass domain) {
        super(domain);
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
    }

    /**
     * @return the collectTime
     */
    public Timestamp getCollectTime() {
        return collectTime;
    }

    /**
     * @param collectTime
     *            the collectTime to set
     */
    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
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

    /**
     * @return the rxPower
     */
    public Float getRxPower() {
        if (onuCatvOrInfoRxPower != null) {
            rxPower = Float.parseFloat(NumberUtils.ONEDOT_FORMAT.format(onuCatvOrInfoRxPower / 10F));
        }
        return rxPower;
    }

    /**
     * @param rxPower
     *            the rxPower to set
     */
    public void setRxPower(Float rxPower) {
        this.rxPower = rxPower;
    }

    /**
     * @return the outVoltage
     */
    public Float getOutVoltage() {
        if (onuCatvOrInfoRfOutVoltage != null) {
            outVoltage = Float.parseFloat(NumberUtils.ONEDOT_FORMAT.format(onuCatvOrInfoRfOutVoltage / 10F));
        }
        return outVoltage;
    }

    /**
     * @param outVoltage
     *            the outVoltage to set
     */
    public void setOutVoltage(Float outVoltage) {
        this.outVoltage = outVoltage;
    }

    /**
     * @return the voltage
     */
    public Float getVoltage() {
        if (onuCatvOrInfoVoltage != null) {
            voltage = Float.parseFloat(NumberUtils.ONEDOT_FORMAT.format(onuCatvOrInfoVoltage / 10F));
        }
        return voltage;
    }

    /**
     * @param voltage
     *            the voltage to set
     */
    public void setVoltage(Float voltage) {
        this.voltage = voltage;
    }

    /**
     * @return the temperature
     */
    public Float getTemperature() {
        if (onuCatvOrInfoTemperature != null) {
            temperature = Float.parseFloat(NumberUtils.ONEDOT_FORMAT.format(onuCatvOrInfoTemperature / 10F));
        }
        return temperature;
    }

    /**
     * @param temperature
     *            the temperature to set
     */
    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    /**
     * 
     * @param onuCatvOrInfoEntry
     */
    public void setOnuCatvOrInfoEntry(OnuCatvOrInfoEntry onuCatvOrInfoEntry) {
        this.onuCatvOrInfoRxPower = onuCatvOrInfoEntry.getOnuCatvOrInfoRxPower();
        this.onuCatvOrInfoRfOutVoltage = onuCatvOrInfoEntry.getOnuCatvOrInfoRfOutVoltage();
        this.onuCatvOrInfoTemperature = onuCatvOrInfoEntry.getOnuCatvOrInfoTemperature();
        this.onuCatvOrInfoVoltage = onuCatvOrInfoEntry.getOnuCatvOrInfoVoltage();
    }

}
