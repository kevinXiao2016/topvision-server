/***********************************************************************
 * $Id: OnuQualityInfo.java,v1.0 2015年4月24日 上午8:59:26 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponConstants;

/**
 * @author Bravin
 * @created @2015年4月24日-上午8:59:26
 *
 */
@TableProperty(tables = { "default", "onuPonOpticalTransmit" })
public class OnuQualityInfo implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -8445820849195996734L;
    private Long entityId;
    private Long onuId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.1", index = true)
    private Long onuDeviceIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.13", type = "OctetString")
    private String onuSoftwareVersion;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.8")
    private Integer onuOperationStatus;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.12.2.1.1", table = "onuPonOpticalTransmit", type = "Integer32", index = true)
    private Long cardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.12.2.1.2", table = "onuPonOpticalTransmit", type = "Integer32", index = true)
    private Integer portIndex = 0;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.12.2.1.3", table = "onuPonOpticalTransmit", type = "Integer32", index = true)
    private Long onuIndex = 0l;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.12.2.1.4", table = "onuPonOpticalTransmit", type = "Integer32")
    private Integer recvOpticalPower;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.12.2.1.5", table = "onuPonOpticalTransmit", type = "Integer32")
    private Integer transOpticalPower;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.12.2.1.9", table = "onuPonOpticalTransmit", writable = true, type = "Integer32")
    private Integer oltReceivedOpticalPower;

    private Float onuPonRevPower;
    private Float onuPonTransPower;
    private Float oltPonRevPower;

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

    public Long getOnuDeviceIndex() {
        return onuDeviceIndex;
    }

    public void setOnuDeviceIndex(Long onuDeviceIndex) {
        this.onuDeviceIndex = onuDeviceIndex;
        this.cardIndex = onuDeviceIndex;
    }

    public String getOnuSoftwareVersion() {
        return onuSoftwareVersion;
    }

    public void setOnuSoftwareVersion(String onuSoftwareVersion) {
        this.onuSoftwareVersion = onuSoftwareVersion;
    }

    public Integer getOnuOperationStatus() {
        return onuOperationStatus;
    }

    public void setOnuOperationStatus(Integer onuOperationStatus) {
        this.onuOperationStatus = onuOperationStatus;
    }

    public Long getCardIndex() {
        return cardIndex;
    }

    public void setCardIndex(Long cardIndex) {
        this.cardIndex = cardIndex;
    }

    public Integer getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Integer portIndex) {
        this.portIndex = portIndex;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public void setOnuIndex(Long cardIndex) {
        this.onuIndex = cardIndex;
        this.cardIndex = cardIndex;
    }

    public Integer getRecvOpticalPower() {
        return recvOpticalPower;
    }

    public void setRecvOpticalPower(Integer recvOpticalPower) {
        if (EponConstants.RE_POWER != recvOpticalPower) {
            this.recvOpticalPower = recvOpticalPower;
            if (recvOpticalPower != null) {
                this.onuPonRevPower = (float) recvOpticalPower / 100;
            }
        }
    }

    public Integer getTransOpticalPower() {
        return transOpticalPower;
    }

    public void setTransOpticalPower(Integer transOpticalPower) {
        if (EponConstants.TX_POWER != transOpticalPower) {
            this.transOpticalPower = transOpticalPower;
            if (transOpticalPower != null) {
                this.onuPonTransPower = (float) transOpticalPower / 100;
            }
        }
    }

    public Integer getOltReceivedOpticalPower() {
        return oltReceivedOpticalPower;
    }

    public void setOltReceivedOpticalPower(Integer oltReceivedOpticalPower) {
        if (EponConstants.RE_POWER != oltReceivedOpticalPower) {
            this.oltReceivedOpticalPower = oltReceivedOpticalPower;
            if (oltReceivedOpticalPower != null) {
                this.oltPonRevPower = (float) oltReceivedOpticalPower / 100;
            }
        }
    }

    public Float getOnuPonRevPower() {
        return onuPonRevPower;
    }

    public void setOnuPonRevPower(Float onuPonRevPower) {
        this.onuPonRevPower = onuPonRevPower;
    }

    public Float getOnuPonTransPower() {
        return onuPonTransPower;
    }

    public void setOnuPonTransPower(Float onuPonTransPower) {
        this.onuPonTransPower = onuPonTransPower;
    }

    public Float getOltPonRevPower() {
        return oltPonRevPower;
    }

    public void setOltPonRevPower(Float oltPonRevPower) {
        this.oltPonRevPower = oltPonRevPower;
    }

    @Override
    public String toString() {
        return "OnuQualityInfo [entityId=" + entityId + ", onuId=" + onuId + ", onuDeviceIndex=" + onuDeviceIndex
                + ", onuSoftwareVersion=" + onuSoftwareVersion + ", onuOperationStatus=" + onuOperationStatus
                + ", cardIndex=" + cardIndex + ", portIndex=" + portIndex + ", onuIndex=" + onuIndex
                + ", recvOpticalPower=" + recvOpticalPower + ", transOpticalPower=" + transOpticalPower
                + ", oltReceivedOpticalPower=" + oltReceivedOpticalPower + "]";
    }

}
