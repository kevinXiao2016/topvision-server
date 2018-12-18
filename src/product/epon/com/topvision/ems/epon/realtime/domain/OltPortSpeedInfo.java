/***********************************************************************
 * $Id: OltSniSpeedInfo.java,v1.0 2014-7-12 下午3:58:25 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.realtime.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2014-7-12-下午3:58:25
 *
 */
public class OltPortSpeedInfo implements AliasesSuperType {
    private static final long serialVersionUID = 254018171474681274L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.9.5.1.1", type = "Integer32", index = true)
    private Long deviceIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.9.5.1.2", type = "Integer32", index = true)
    private Integer cardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.9.5.1.3", type = "Integer32", index = true)
    private Integer portIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.9.5.1.51", type = "Counter64")
    private Long inBindWidth;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.9.5.1.52", type = "Counter64")
    private Long outBindWidth;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.9.5.1.49", type = "Counter64")
    private Long inBindUseRatio;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.9.5.1.50", type = "Counter64")
    private Long outBindUseRatio;

    private Long emsPortIndex;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
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

    public Integer getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Integer portIndex) {
        this.portIndex = portIndex;
    }

    public Long getInBindWidth() {
        return inBindWidth;
    }

    public void setInBindWidth(Long inBindWidth) {
        this.inBindWidth = inBindWidth;
    }

    public Long getOutBindWidth() {
        return outBindWidth;
    }

    public void setOutBindWidth(Long outBindWidth) {
        this.outBindWidth = outBindWidth;
    }

    public Long getInBindUseRatio() {
        return inBindUseRatio;
    }

    public void setInBindUseRatio(Long inBindUseRatio) {
        this.inBindUseRatio = inBindUseRatio;
    }

    public Long getOutBindUseRatio() {
        return outBindUseRatio;
    }

    public void setOutBindUseRatio(Long outBindUseRatio) {
        this.outBindUseRatio = outBindUseRatio;
    }

    public Long getEmsPortIndex() {
        return emsPortIndex;
    }

    public void setEmsPortIndex(Long emsPortIndex) {
        this.emsPortIndex = emsPortIndex;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltSniSpeedInfo [deviceIndex=");
        builder.append(deviceIndex);
        builder.append(", cardIndex=");
        builder.append(cardIndex);
        builder.append(", portIndex=");
        builder.append(portIndex);
        builder.append(", inBindWidth=");
        builder.append(inBindWidth);
        builder.append(", outBindWidth=");
        builder.append(outBindWidth);
        builder.append(", inBindUseRatio=");
        builder.append(inBindUseRatio);
        builder.append(", outBindUseRatio=");
        builder.append(outBindUseRatio);
        builder.append("]");
        return builder.toString();
    }

}
