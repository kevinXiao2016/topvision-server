/***********************************************************************
 * $Id: OnuFlowCollectInfo.java,v1.0 2015-5-7 上午11:39:13 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.domain;

import java.sql.Timestamp;

import com.topvision.ems.facade.nbi.NbiSnmpProperty;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2015-5-7-上午11:39:13
 *
 */
public class OnuFlowCollectInfo implements AliasesSuperType {
    private static final long serialVersionUID = -4866841368695146118L;

    private Long entityId;
    private Long onuId;
    private Long onuIndex;
    private Long portIndex;
    private String portType;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.9.5.1.1", type = "Integer32", index = true)
    private Long deviceIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.9.5.1.2", type = "Integer32", index = true)
    private Integer cardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.9.5.1.3", type = "Integer32", index = true)
    private Integer devicePortIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.9.5.1.4", type = "Counter64")
    private Long portInOctets;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.9.5.1.26", type = "Counter64")
    private Long portOutOctets;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.9.5.1.49", type = "Integer32")
    private Integer inUseRatio;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.9.5.1.50", type = "Integer32")
    private Integer outUserRatio;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.9.5.1.51", type = "Integer32")
    private Integer inBindWidth;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.9.5.1.52", type = "Integer32")
    private Integer outBindWidth;
    //入方向速率
    @NbiSnmpProperty(oid = "1.3.6.1.4.1.32285.12.2.3.9.5.1.1")
    private Float portInSpeed;
    //出方向速率
    @NbiSnmpProperty(oid = "1.3.6.1.4.1.32285.12.2.3.9.5.1.2")
    private Float portOutSpeed;
    //采集时间
    private Timestamp collectTime;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    public Long getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
    }

    public String getPortType() {
        return portType;
    }

    public void setPortType(String portType) {
        this.portType = portType;
    }

    public Long getDeviceIndex() {
        return deviceIndex;
    }

    public void setDeviceIndex(Long deviceIndex) {
        this.deviceIndex = deviceIndex;
    }

    public Integer getCardIndex() {
        return cardIndex;
    }

    public void setCardIndex(Integer cardIndex) {
        this.cardIndex = cardIndex;
    }

    public Integer getDevicePortIndex() {
        return devicePortIndex;
    }

    public void setDevicePortIndex(Integer devicePortIndex) {
        this.devicePortIndex = devicePortIndex;
    }

    public Long getPortInOctets() {
        return portInOctets;
    }

    public void setPortInOctets(Long portInOctets) {
        this.portInOctets = portInOctets;
    }

    public Long getPortOutOctets() {
        return portOutOctets;
    }

    public void setPortOutOctets(Long portOutOctets) {
        this.portOutOctets = portOutOctets;
    }

    public Integer getInUseRatio() {
        return inUseRatio;
    }

    public void setInUseRatio(Integer inUseRatio) {
        this.inUseRatio = inUseRatio;
    }

    public Integer getOutUserRatio() {
        return outUserRatio;
    }

    public void setOutUserRatio(Integer outUserRatio) {
        this.outUserRatio = outUserRatio;
    }

    public Integer getInBindWidth() {
        return inBindWidth;
    }

    public void setInBindWidth(Integer inBindWidth) {
        this.inBindWidth = inBindWidth;
    }

    public Integer getOutBindWidth() {
        return outBindWidth;
    }

    public void setOutBindWidth(Integer outBindWidth) {
        this.outBindWidth = outBindWidth;
    }

    public Float getPortInSpeed() {
        return portInSpeed;
    }

    public void setPortInSpeed(Float portInSpeed) {
        this.portInSpeed = portInSpeed;
    }

    public Float getPortOutSpeed() {
        return portOutSpeed;
    }

    public void setPortOutSpeed(Float portOutSpeed) {
        this.portOutSpeed = portOutSpeed;
    }

    public Timestamp getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OnuFlowCollectInfo [entityId=");
        builder.append(entityId);
        builder.append(", onuId=");
        builder.append(onuId);
        builder.append(", onuIndex=");
        builder.append(onuIndex);
        builder.append(", portIndex=");
        builder.append(portIndex);
        builder.append(", portType=");
        builder.append(portType);
        builder.append(", deviceIndex=");
        builder.append(deviceIndex);
        builder.append(", cardIndex=");
        builder.append(cardIndex);
        builder.append(", devicePortIndex=");
        builder.append(devicePortIndex);
        builder.append(", portInOctets=");
        builder.append(portInOctets);
        builder.append(", portOutOctets=");
        builder.append(portOutOctets);
        builder.append(", inUseRatio=");
        builder.append(inUseRatio);
        builder.append(", outUserRatio=");
        builder.append(outUserRatio);
        builder.append(", inBindWidth=");
        builder.append(inBindWidth);
        builder.append(", outBindWidth=");
        builder.append(outBindWidth);
        builder.append(", portInSpeed=");
        builder.append(portInSpeed);
        builder.append(", portOutSpeed=");
        builder.append(portOutSpeed);
        builder.append("]");
        return builder.toString();
    }

}
