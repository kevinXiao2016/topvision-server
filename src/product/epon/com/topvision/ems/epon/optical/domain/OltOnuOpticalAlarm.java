/***********************************************************************
 * $Id: OltOnuOpticalAlarm.java,v1.0 2013-8-28 下午3:26:44 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.optical.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author flack
 * @created @2013-8-28-下午3:26:44
 *
 */
public class OltOnuOpticalAlarm implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = -5164567338676254978L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.12.1.1.1", index = true)
    private Integer topOnuOptCardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.12.1.1.2", index = true)
    private Integer topOnuOptPonIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.12.1.1.3", index = true)
    private Integer topOnuOptOnuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.12.1.1.4", index = true)
    private Integer topOnuOptAlarmIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.12.1.1.5", writable = true, type = "Integer32")
    private Integer topOnuOptThresholdValue;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.12.1.1.6", writable = true, type = "Integer32")
    private Integer topOnuOptAlarmState;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.12.1.1.7", type = "Integer32")
    private Integer topOnuOptAlarmCount;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.12.1.1.8", writable = true, type = "Integer32")
    private Integer topOnuOptAlarmRowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getTopOnuOptCardIndex() {
        return topOnuOptCardIndex;
    }

    public void setTopOnuOptCardIndex(Integer topOnuOptCardIndex) {
        this.topOnuOptCardIndex = topOnuOptCardIndex;
    }

    public Integer getTopOnuOptPonIndex() {
        return topOnuOptPonIndex;
    }

    public void setTopOnuOptPonIndex(Integer topOnuOptPonIndex) {
        this.topOnuOptPonIndex = topOnuOptPonIndex;
    }

    public Integer getTopOnuOptOnuIndex() {
        return topOnuOptOnuIndex;
    }

    public void setTopOnuOptOnuIndex(Integer topOnuOptOnuIndex) {
        this.topOnuOptOnuIndex = topOnuOptOnuIndex;
    }

    public Integer getTopOnuOptAlarmIndex() {
        return topOnuOptAlarmIndex;
    }

    public void setTopOnuOptAlarmIndex(Integer topOnuOptAlarmIndex) {
        this.topOnuOptAlarmIndex = topOnuOptAlarmIndex;
    }

    public Integer getTopOnuOptThresholdValue() {
        return topOnuOptThresholdValue;
    }

    public void setTopOnuOptThresholdValue(Integer topOnuOptThresholdValue) {
        this.topOnuOptThresholdValue = topOnuOptThresholdValue;
    }

    public Integer getTopOnuOptAlarmState() {
        return topOnuOptAlarmState;
    }

    public void setTopOnuOptAlarmState(Integer topOnuOptAlarmState) {
        this.topOnuOptAlarmState = topOnuOptAlarmState;
    }

    public Integer getTopOnuOptAlarmCount() {
        return topOnuOptAlarmCount;
    }

    public void setTopOnuOptAlarmCount(Integer topOnuOptAlarmCount) {
        this.topOnuOptAlarmCount = topOnuOptAlarmCount;
    }

    public Integer getTopOnuOptAlarmRowStatus() {
        return topOnuOptAlarmRowStatus;
    }

    public void setTopOnuOptAlarmRowStatus(Integer topOnuOptAlarmRowStatus) {
        this.topOnuOptAlarmRowStatus = topOnuOptAlarmRowStatus;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltOnuOpticalAlarm [entityId=");
        builder.append(entityId);
        builder.append(", topOnuOptCardIndex=");
        builder.append(topOnuOptCardIndex);
        builder.append(", topOnuOptPonIndex=");
        builder.append(topOnuOptPonIndex);
        builder.append(", topOnuOptOnuIndex=");
        builder.append(topOnuOptOnuIndex);
        builder.append(", topOnuOptAlarmIndex=");
        builder.append(topOnuOptAlarmIndex);
        builder.append(", topOnuOptThresholdValue=");
        builder.append(topOnuOptThresholdValue);
        builder.append(", topOnuOptAlarmState=");
        builder.append(topOnuOptAlarmState);
        builder.append(", topOnuOptAlarmCount=");
        builder.append(topOnuOptAlarmCount);
        builder.append(", topOnuOptAlarmRowStatus=");
        builder.append(topOnuOptAlarmRowStatus);
        builder.append("]");
        return builder.toString();
    }

    public void setPortIndex(Long portIndex) {
        this.topOnuOptCardIndex = EponIndex.getSlotNo(portIndex).intValue();
        this.topOnuOptPonIndex = EponIndex.getPonNo(portIndex).intValue();
        this.topOnuOptOnuIndex = EponIndex.getOnuNo(portIndex).intValue();
    }

}
