/***********************************************************************
 * $Id: OltSniInfo.java,v1.0 2014-7-12 下午3:14:42 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.realtime.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author flack
 * @created @2014-7-12-下午3:14:42
 *
 */
@TableProperty(tables = { "default", "sniAttribute" })
public class OltSniInfo implements AliasesSuperType {
    private static final long serialVersionUID = 6591199870889367600L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.2.1.1.1", type = "Integer32", index = true)
    private Integer cardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.2.1.1.2", type = "Integer32", index = true)
    private Integer sniIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.2.1.1.7", type = "Integer32")
    private Integer sniPortType;
    private String sniLocation;

    @SnmpProperty(table = "sniAttribute", oid = "1.3.6.1.4.1.17409.2.3.2.1.1.1", type = "Integer32", index = true)
    private Integer deviceIndex;
    @SnmpProperty(table = "sniAttribute", oid = "1.3.6.1.4.1.17409.2.3.2.1.1.2", type = "Integer32", index = true)
    private Integer slotIndex;
    @SnmpProperty(table = "sniAttribute", oid = "1.3.6.1.4.1.17409.2.3.2.1.1.3", type = "Integer32", index = true)
    private Integer portIndex;
    @SnmpProperty(table = "sniAttribute", oid = "1.3.6.1.4.1.17409.2.3.2.1.1.4", type = "OctetString")
    private String portName;
    @SnmpProperty(table = "sniAttribute", oid = "1.3.6.1.4.1.17409.2.3.2.1.1.6", type = "Integer32")
    private Integer operationStatus;
    @SnmpProperty(table = "sniAttribute", oid = "1.3.6.1.4.1.17409.2.3.2.1.1.9", type = "Integer32")
    private Integer autoNegoMode;
    @SnmpProperty(table = "sniAttribute", oid = "1.3.6.1.4.1.17409.2.3.2.1.1.10", type = "Integer32")
    private Integer perfStatsEnable;

    private Long portInSpeed;
    private Long portOutSpeed;
    private Long sniPortIndex;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getCardIndex() {
        return cardIndex;
    }

    public void setCardIndex(Integer cardIndex) {
        this.cardIndex = cardIndex;
    }

    public Integer getSniIndex() {
        return sniIndex;
    }

    public void setSniIndex(Integer sniIndex) {
        this.sniIndex = sniIndex;
        this.sniLocation = this.cardIndex + "/" + sniIndex;
    }

    public Integer getSniPortType() {
        return sniPortType;
    }

    public void setSniPortType(Integer sniPortType) {
        this.sniPortType = sniPortType;
    }

    public String getSniLocation() {
        return sniLocation;
    }

    public void setSniLocation(String sniLocation) {
        this.sniLocation = sniLocation;
    }

    public Integer getDeviceIndex() {
        return deviceIndex;
    }

    public void setDeviceIndex(Integer deviceIndex) {
        this.deviceIndex = deviceIndex;
    }

    public Integer getSlotIndex() {
        return slotIndex;
    }

    public void setSlotIndex(Integer slotIndex) {
        this.slotIndex = slotIndex;
    }

    public Integer getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Integer portIndex) {
        this.portIndex = portIndex;
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public Integer getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(Integer operationStatus) {
        this.operationStatus = operationStatus;
    }

    public Integer getAutoNegoMode() {
        return autoNegoMode;
    }

    public void setAutoNegoMode(Integer autoNegoMode) {
        this.autoNegoMode = autoNegoMode;
    }

    public Long getPortInSpeed() {
        return portInSpeed;
    }

    public void setPortInSpeed(Long portInSpeed) {
        this.portInSpeed = portInSpeed;
    }

    public Long getPortOutSpeed() {
        return portOutSpeed;
    }

    public void setPortOutSpeed(Long portOutSpeed) {
        this.portOutSpeed = portOutSpeed;
    }

    public Integer getPerfStatsEnable() {
        return perfStatsEnable;
    }

    public void setPerfStatsEnable(Integer perfStatsEnable) {
        this.perfStatsEnable = perfStatsEnable;
    }
    
    

    public Long getSniPortIndex() {
        if (sniPortIndex == null) {
            sniPortIndex = new EponIndex(cardIndex, sniIndex).getSniIndex();
        }
        return sniPortIndex;
    }

    public void setSniPortIndex(Long sniPortIndex) {
        this.sniPortIndex = sniPortIndex;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltSniInfo [entityId=");
        builder.append(entityId);
        builder.append(", cardIndex=");
        builder.append(cardIndex);
        builder.append(", sniIndex=");
        builder.append(sniIndex);
        builder.append(", sniPortType=");
        builder.append(sniPortType);
        builder.append(", sniLocation=");
        builder.append(sniLocation);
        builder.append(", deviceIndex=");
        builder.append(deviceIndex);
        builder.append(", slotIndex=");
        builder.append(slotIndex);
        builder.append(", portIndex=");
        builder.append(portIndex);
        builder.append(", portName=");
        builder.append(portName);
        builder.append(", operationStatus=");
        builder.append(operationStatus);
        builder.append(", autoNegoMode=");
        builder.append(autoNegoMode);
        builder.append(", perfStatsEnable=");
        builder.append(perfStatsEnable);
        builder.append(", portInSpeed=");
        builder.append(portInSpeed);
        builder.append(", portOutSpeed=");
        builder.append(portOutSpeed);
        builder.append("]");
        return builder.toString();
    }
}
