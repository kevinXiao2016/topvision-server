/***********************************************************************
 * $Id: OltPonOpticalAlarm.java,v1.0 2013-8-28 下午3:37:53 $
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
 * @created @2013-8-28-下午3:37:53
 *
 */
public class OltPonOpticalAlarm implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -6874594018035570104L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.9.1.1.1", index = true)
    private Integer topPonOptCardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.9.1.1.2", index = true)
    private Integer topPonOptPonIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.9.1.1.3", index = true)
    private Integer topPonOptAlarmIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.9.1.1.4", writable = true, type = "Integer32")
    private Integer topPonOptThresholdValue;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.9.1.1.5", writable = true, type = "Integer32")
    private Integer topPonOptAlarmState;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.9.1.1.6", type = "Integer32")
    private Integer topPonOptAlarmCount;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.9.1.1.7", writable = true, type = "Integer32")
    private Integer topPonOptAlarmRowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getTopPonOptCardIndex() {
        return topPonOptCardIndex;
    }

    public void setTopPonOptCardIndex(Integer topPonOptCardIndex) {
        this.topPonOptCardIndex = topPonOptCardIndex;
    }

    public Integer getTopPonOptPonIndex() {
        return topPonOptPonIndex;
    }

    public void setTopPonOptPonIndex(Integer topPonOptPonIndex) {
        this.topPonOptPonIndex = topPonOptPonIndex;
    }

    public Integer getTopPonOptAlarmIndex() {
        return topPonOptAlarmIndex;
    }

    public void setTopPonOptAlarmIndex(Integer topPonOptAlarmIndex) {
        this.topPonOptAlarmIndex = topPonOptAlarmIndex;
    }

    public Integer getTopPonOptThresholdValue() {
        return topPonOptThresholdValue;
    }

    public void setTopPonOptThresholdValue(Integer topPonOptThresholdValue) {
        this.topPonOptThresholdValue = topPonOptThresholdValue;
    }

    public Integer getTopPonOptAlarmState() {
        return topPonOptAlarmState;
    }

    public void setTopPonOptAlarmState(Integer topPonOptAlarmState) {
        this.topPonOptAlarmState = topPonOptAlarmState;
    }

    public Integer getTopPonOptAlarmCount() {
        return topPonOptAlarmCount;
    }

    public void setTopPonOptAlarmCount(Integer topPonOptAlarmCount) {
        this.topPonOptAlarmCount = topPonOptAlarmCount;
    }

    public Integer getTopPonOptAlarmRowStatus() {
        return topPonOptAlarmRowStatus;
    }

    public void setTopPonOptAlarmRowStatus(Integer topPonOptAlarmRowStatus) {
        this.topPonOptAlarmRowStatus = topPonOptAlarmRowStatus;
    }

    public void setPortIndex(Long portIndex) {
        this.topPonOptCardIndex = EponIndex.getSlotNo(portIndex).intValue();
        this.topPonOptPonIndex = EponIndex.getPonNo(portIndex).intValue();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltPonOpticalAlarm [entityId=");
        builder.append(entityId);
        builder.append(", topPonOptCardIndex=");
        builder.append(topPonOptCardIndex);
        builder.append(", topPonOptPonIndex=");
        builder.append(topPonOptPonIndex);
        builder.append(", topPonOptAlarmIndex=");
        builder.append(topPonOptAlarmIndex);
        builder.append(", topPonOptThresholdValue=");
        builder.append(topPonOptThresholdValue);
        builder.append(", topPonOptAlarmState=");
        builder.append(topPonOptAlarmState);
        builder.append(", topPonOptAlarmCount=");
        builder.append(topPonOptAlarmCount);
        builder.append(", topPonOptAlarmRowStatus=");
        builder.append(topPonOptAlarmRowStatus);
        builder.append("]");
        return builder.toString();
    }

}
