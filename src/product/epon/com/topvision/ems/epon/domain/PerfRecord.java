/***********************************************************************
 * $ PerfRecord.java,v1.0 2012-2-2 17:24:44 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topvision.framework.common.DateUtils;
import com.topvision.framework.common.NumberUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author jay
 * @created @2012-2-2-17:24:44
 */
public class PerfRecord implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    private Long entityId;
    private String name;
    private String ip;
    private Long portIndex;
    private Double value;
    private Timestamp collectTime;
    private String recentUpdateTime;
    private Double tempValue;
    private String inSpeed;
    private String outSpeed;
    private Double portInSpeed;
    private Double portOutSpeed;
    private String portName;

    public Timestamp getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
        this.inSpeed = NumberUtils.getIfSpeedStr(value);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public String getPortName() {
        if (portName == null && portIndex != null) {
            portName = EponIndex.getPortStringByIndex(portIndex).toString();
        }
        return portName;
    }

    public Double getTempValue() {
        return tempValue;
    }

    public void setTempValue(Double tempValue) {
        this.tempValue = tempValue;
        this.outSpeed = NumberUtils.getIfSpeedStr(tempValue);
    }

    public void setInSpeed(String inSpeed) {
        this.inSpeed = inSpeed;
    }

    public void setOutSpeed(String outSpeed) {
        this.outSpeed = outSpeed;
    }

    public String getInSpeed() {
        return inSpeed;
    }

    public String getOutSpeed() {
        return outSpeed;
    }

    public String getDtStr() {
        return DateUtils.format(this.collectTime);
    }

    public Double getPortInSpeed() {
        return portInSpeed;
    }

    public void setPortInSpeed(Double portInSpeed) {
        this.portInSpeed = portInSpeed;
    }

    public Double getPortOutSpeed() {
        return portOutSpeed;
    }

    public void setPortOutSpeed(Double portOutSpeed) {
        this.portOutSpeed = portOutSpeed;
    }

    public String getRecentUpdateTime() {
        return recentUpdateTime;
    }

    public void setRecentUpdateTime(String recentUpdateTime) {
        this.recentUpdateTime = recentUpdateTime;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PerfRecord [entityId=");
        builder.append(entityId);
        builder.append(", name=");
        builder.append(name);
        builder.append(", ip=");
        builder.append(ip);
        builder.append(", portIndex=");
        builder.append(portIndex);
        builder.append(", value=");
        builder.append(value);
        builder.append(", collectTime=");
        builder.append(collectTime);
        builder.append(", recentUpdateTime=");
        builder.append(recentUpdateTime);
        builder.append(", tempValue=");
        builder.append(tempValue);
        builder.append(", inSpeed=");
        builder.append(inSpeed);
        builder.append(", outSpeed=");
        builder.append(outSpeed);
        builder.append(", portInSpeed=");
        builder.append(portInSpeed);
        builder.append(", portOutSpeed=");
        builder.append(portOutSpeed);
        builder.append(", portName=");
        builder.append(portName);
        builder.append("]");
        return builder.toString();
    }

}
