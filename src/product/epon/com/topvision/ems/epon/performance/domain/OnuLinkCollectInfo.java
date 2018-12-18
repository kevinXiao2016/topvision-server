/***********************************************************************
 * $Id: OnuLinkCollectInfo.java,v1.0 2015-4-22 下午4:40:56 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author flack
 * @created @2015-4-22-下午4:40:56
 *
 */
public class OnuLinkCollectInfo implements AliasesSuperType {
    private static final long serialVersionUID = -680595028881696530L;

    private Long entityId;
    private Long onuId;
    private Long onuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.12.2.1.1", index = true)
    private Long onuDeviceIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.12.2.1.2", index = true)
    private Integer cardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.12.2.1.3", index = true)
    private Integer portIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.12.2.1.4", type = "Integer32")
    private Integer onuPonRevPower;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.12.2.1.5", type = "Integer32")
    private Integer onuPonTransPower;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.12.2.1.9", type = "Integer32")
    private Integer oltPonRevPower;

    // 存储网管转换后的值
    private Float onuRevResult;
    private Float onuTransResult;
    private Float oltRevResult;

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
        this.onuDeviceIndex = EponIndex.getOnuMibIndexByIndex(onuIndex);
    }

    public Long getOnuDeviceIndex() {
        return onuDeviceIndex;
    }

    public void setOnuDeviceIndex(Long onuDeviceIndex) {
        this.onuDeviceIndex = onuDeviceIndex;
        this.onuIndex = EponIndex.getOnuIndexByMibIndex(onuDeviceIndex);
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

    public Integer getOnuPonRevPower() {
        return onuPonRevPower;
    }

    public void setOnuPonRevPower(Integer onuPonRevPower) {
        this.onuPonRevPower = onuPonRevPower;
    }

    public Integer getOnuPonTransPower() {
        return onuPonTransPower;
    }

    public void setOnuPonTransPower(Integer onuPonTransPower) {
        this.onuPonTransPower = onuPonTransPower;
    }

    public Integer getOltPonRevPower() {
        return oltPonRevPower;
    }

    public void setOltPonRevPower(Integer oltPonRevPower) {
        this.oltPonRevPower = oltPonRevPower;
    }

    public Float getOnuRevResult() {
        return onuRevResult;
    }

    public void setOnuRevResult(Float onuRevResult) {
        this.onuRevResult = onuRevResult;
    }

    public Float getOnuTransResult() {
        return onuTransResult;
    }

    public void setOnuTransResult(Float onuTransResult) {
        this.onuTransResult = onuTransResult;
    }

    public Float getOltRevResult() {
        return oltRevResult;
    }

    public void setOltRevResult(Float oltRevResult) {
        this.oltRevResult = oltRevResult;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OnuLinkCollectInfo [entityId=");
        builder.append(entityId);
        builder.append(", onuId=");
        builder.append(onuId);
        builder.append(", onuIndex=");
        builder.append(onuIndex);
        builder.append(", onuDeviceIndex=");
        builder.append(onuDeviceIndex);
        builder.append(", cardIndex=");
        builder.append(cardIndex);
        builder.append(", portIndex=");
        builder.append(portIndex);
        builder.append(", onuPonRevPower=");
        builder.append(onuPonRevPower);
        builder.append(", onuPonTransPower=");
        builder.append(onuPonTransPower);
        builder.append(", oltPonRevPower=");
        builder.append(oltPonRevPower);
        builder.append("]");
        return builder.toString();
    }

}
