/***********************************************************************
 * $Id: OltPonStormSuppressionEntry.java,v1.0 2011-10-18 下午03:28:55 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.domain;

import java.io.Serializable;

import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author huqiao
 * @created @2011-10-18-下午03:28:55
 * 
 */
public class OltPonStormSuppressionEntry implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 536257125555471803L;
    private Long entityId;
    private Long ponId;
    private Long ponIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.3.1.1", index = true)
    private Integer bsDeviceIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.3.1.2", index = true)
    private Long bsCardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.3.1.3", index = true)
    private Integer bsPortIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.3.1.4", writable = true, type = "Integer32")
    private Integer unicastStormEnable;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.3.1.5", writable = true, type = "Integer32")
    private Integer unicastStormInPacketRate;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.3.1.6", writable = true, type = "Integer32")
    private Integer unicastStormOutPacketRate;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.3.1.7", writable = true, type = "Integer32")
    private Integer multicastStormEnable;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.3.1.8", writable = true, type = "Integer32")
    private Integer multicastStormInPacketRate;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.3.1.9", writable = true, type = "Integer32")
    private Integer multicastStormOutPacketRate;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.3.1.10", writable = true, type = "Integer32")
    private Integer broadcastStormEnable;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.3.1.11", writable = true, type = "Integer32")
    private Integer broadcastStormInPacketRate;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.3.3.1.12", writable = true, type = "Integer32")
    private Integer broadcastStormOutPacketRate;

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
     * @return the bsDeviceIndex
     */
    public Integer getBsDeviceIndex() {
        return bsDeviceIndex;
    }

    /**
     * @param bsDeviceIndex
     *            the bsDeviceIndex to set
     */
    public void setBsDeviceIndex(Integer bsDeviceIndex) {
        this.bsDeviceIndex = bsDeviceIndex;
    }

    /**
     * @return the bsCardIndex
     */
    public Long getBsCardIndex() {
        return bsCardIndex;
    }

    /**
     * @param bsCardIndex
     *            the bsCardIndex to set
     */
    public void setBsCardIndex(Long bsCardIndex) {
        this.bsCardIndex = bsCardIndex;
    }

    /**
     * @return the bsPortIndex
     */
    public Integer getBsPortIndex() {
        return bsPortIndex;
    }

    /**
     * @param bsPortIndex
     *            the bsPortIndex to set
     */
    public void setBsPortIndex(Integer bsPortIndex) {
        this.bsPortIndex = bsPortIndex;
    }

    /**
     * @return the unicastStormEnable
     */
    public Integer getUnicastStormEnable() {
        return unicastStormEnable;
    }

    /**
     * @param unicastStormEnable
     *            the unicastStormEnable to set
     */
    public void setUnicastStormEnable(Integer unicastStormEnable) {
        this.unicastStormEnable = unicastStormEnable;
    }

    /**
     * @return the unicastStormInPacketRate
     */
    public Integer getUnicastStormInPacketRate() {
        return unicastStormInPacketRate;
    }

    /**
     * @param unicastStormInPacketRate
     *            the unicastStormInPacketRate to set
     */
    public void setUnicastStormInPacketRate(Integer unicastStormInPacketRate) {
        this.unicastStormInPacketRate = unicastStormInPacketRate;
    }

    /**
     * @return the unicastStormOutPacketRate
     */
    public Integer getUnicastStormOutPacketRate() {
        return unicastStormOutPacketRate;
    }

    /**
     * @param unicastStormOutPacketRate
     *            the unicastStormOutPacketRate to set
     */
    public void setUnicastStormOutPacketRate(Integer unicastStormOutPacketRate) {
        this.unicastStormOutPacketRate = unicastStormOutPacketRate;
    }

    /**
     * @return the multicastStormEnable
     */
    public Integer getMulticastStormEnable() {
        return multicastStormEnable;
    }

    /**
     * @param multicastStormEnable
     *            the multicastStormEnable to set
     */
    public void setMulticastStormEnable(Integer multicastStormEnable) {
        this.multicastStormEnable = multicastStormEnable;
    }

    /**
     * @return the multicastStormInPacketRate
     */
    public Integer getMulticastStormInPacketRate() {
        return multicastStormInPacketRate;
    }

    /**
     * @param multicastStormInPacketRate
     *            the multicastStormInPacketRate to set
     */
    public void setMulticastStormInPacketRate(Integer multicastStormInPacketRate) {
        this.multicastStormInPacketRate = multicastStormInPacketRate;
    }

    /**
     * @return the multicastStormOutPacketRate
     */
    public Integer getMulticastStormOutPacketRate() {
        return multicastStormOutPacketRate;
    }

    /**
     * @param multicastStormOutPacketRate
     *            the multicastStormOutPacketRate to set
     */
    public void setMulticastStormOutPacketRate(Integer multicastStormOutPacketRate) {
        this.multicastStormOutPacketRate = multicastStormOutPacketRate;
    }

    /**
     * @return the broadcastStormEnable
     */
    public Integer getBroadcastStormEnable() {
        return broadcastStormEnable;
    }

    /**
     * @param broadcastStormEnable
     *            the broadcastStormEnable to set
     */
    public void setBroadcastStormEnable(Integer broadcastStormEnable) {
        this.broadcastStormEnable = broadcastStormEnable;
    }

    /**
     * @return the broadcastStormInPacketRate
     */
    public Integer getBroadcastStormInPacketRate() {
        return broadcastStormInPacketRate;
    }

    /**
     * @param broadcastStormInPacketRate
     *            the broadcastStormInPacketRate to set
     */
    public void setBroadcastStormInPacketRate(Integer broadcastStormInPacketRate) {
        this.broadcastStormInPacketRate = broadcastStormInPacketRate;
    }

    /**
     * @return the broadcastStormOutPacketRate
     */
    public Integer getBroadcastStormOutPacketRate() {
        return broadcastStormOutPacketRate;
    }

    /**
     * @param broadcastStormOutPacketRate
     *            the broadcastStormOutPacketRate to set
     */
    public void setBroadcastStormOutPacketRate(Integer broadcastStormOutPacketRate) {
        this.broadcastStormOutPacketRate = broadcastStormOutPacketRate;
    }

    /**
     * @return the ponId
     */
    public Long getPonId() {
        return ponId;
    }

    /**
     * @param ponId
     *            the ponId to set
     */
    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    /**
     * @return the ponIndex
     */
    public Long getPonIndex() {
        if (ponIndex == null) {
            ponIndex = EponIndex.getPonIndex(bsCardIndex.intValue(), bsPortIndex);
        }
        return ponIndex;
    }

    /**
     * @param ponIndex
     *            the ponIndex to set
     */
    public void setPonIndex(Long ponIndex) {
        this.ponIndex = ponIndex;
        bsDeviceIndex = 1;
        bsCardIndex = EponIndex.getSlotNo(ponIndex);
        bsPortIndex = EponIndex.getPonNo(ponIndex).intValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltPonStormSuppressionEntry [entityId=");
        builder.append(entityId);
        builder.append(", ponId=");
        builder.append(ponId);
        builder.append(", ponIndex=");
        builder.append(ponIndex);
        builder.append(", bsDeviceIndex=");
        builder.append(bsDeviceIndex);
        builder.append(", bsCardIndex=");
        builder.append(bsCardIndex);
        builder.append(", bsPortIndex=");
        builder.append(bsPortIndex);
        builder.append(", unicastStormEnable=");
        builder.append(unicastStormEnable);
        builder.append(", unicastStormInPacketRate=");
        builder.append(unicastStormInPacketRate);
        builder.append(", unicastStormOutPacketRate=");
        builder.append(unicastStormOutPacketRate);
        builder.append(", multicastStormEnable=");
        builder.append(multicastStormEnable);
        builder.append(", multicastStormInPacketRate=");
        builder.append(multicastStormInPacketRate);
        builder.append(", multicastStormOutPacketRate=");
        builder.append(multicastStormOutPacketRate);
        builder.append(", broadcastStormEnable=");
        builder.append(broadcastStormEnable);
        builder.append(", broadcastStormInPacketRate=");
        builder.append(broadcastStormInPacketRate);
        builder.append(", broadcastStormOutPacketRate=");
        builder.append(broadcastStormOutPacketRate);
        builder.append("]");
        return builder.toString();
    }

}
