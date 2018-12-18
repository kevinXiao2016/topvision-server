/***********************************************************************
 * $Id: OltPonTotalInfo.java,v1.0 2014-7-12 下午4:09:00 $
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
 * @created @2014-7-12-下午4:09:00
 *
 */
public class OltPonTotalInfo implements AliasesSuperType {
    private static final long serialVersionUID = -8421603232564536663L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.1.1.1", type = "Integer32", index = true)
    private Integer deviceIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.1.1.2", type = "Integer32", index = true)
    private Integer cardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.1.1.3", type = "Integer32", index = true)
    private Integer portIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.1.1.5", type = "Integer32")
    private Integer operationStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getDeviceIndex() {
        return deviceIndex;
    }

    public void setDeviceIndex(Integer deviceIndex) {
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

    public Integer getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(Integer operationStatus) {
        this.operationStatus = operationStatus;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltPonTotalInfo [deviceIndex=");
        builder.append(deviceIndex);
        builder.append(", cardIndex=");
        builder.append(cardIndex);
        builder.append(", portIndex=");
        builder.append(portIndex);
        builder.append(", operationStatus=");
        builder.append(operationStatus);
        builder.append("]");
        return builder.toString();
    }

}
