/***********************************************************************
 * $Id: OltOnuUpgradeEx.java,v1.0 2012-12-3 下午04:48:54 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * 下级设备升级(ONU和CC8800A)
 * 
 * @author lizongtian
 * @created @2012-12-3-下午04:48:54
 * 
 */
// 临时用来获取topOnuUpgradeTransactionIndex使用的一个domain
public class OltOnuUpgradeEx implements Serializable {
    private static final long serialVersionUID = -5147408248991026310L;
    private Long entityId;
    private Long onuUpgradeId;
    private Long slotIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.6.1.1.1", index = true)
    private Integer topOnuUpgradeTransactionIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.6.1.1.10", type = "OctetString")
    private String topOnuUpgradeStatus;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.4.6.1.1.11", writable = true, type = "Integer32")
    private Integer topOnuUpgradeRowStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getOnuUpgradeId() {
        return onuUpgradeId;
    }

    public void setOnuUpgradeId(Long onuUpgradeId) {
        this.onuUpgradeId = onuUpgradeId;
    }

    public Long getSlotIndex() {
        return slotIndex;
    }

    public void setSlotIndex(Long slotIndex) {
        this.slotIndex = slotIndex;
    }

    public Integer getTopOnuUpgradeTransactionIndex() {
        return topOnuUpgradeTransactionIndex;
    }

    public void setTopOnuUpgradeTransactionIndex(Integer topOnuUpgradeTransactionIndex) {
        this.topOnuUpgradeTransactionIndex = topOnuUpgradeTransactionIndex;
    }

    public String getTopOnuUpgradeStatus() {
        return topOnuUpgradeStatus;
    }

    public void setTopOnuUpgradeStatus(String topOnuUpgradeStatus) {
        this.topOnuUpgradeStatus = topOnuUpgradeStatus;
    }

    public Integer getTopOnuUpgradeRowStatus() {
        return topOnuUpgradeRowStatus;
    }

    public void setTopOnuUpgradeRowStatus(Integer topOnuUpgradeRowStatus) {
        this.topOnuUpgradeRowStatus = topOnuUpgradeRowStatus;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OltOnuUpgrade{");
        sb.append("entityId=").append(entityId);
        sb.append(", onuUpgradeId=").append(onuUpgradeId);
        sb.append(", slotIndex=").append(slotIndex);
        sb.append(", topOnuUpgradeTransactionIndex=").append(topOnuUpgradeTransactionIndex);
        sb.append(", topOnuUpgradeStatus='").append(topOnuUpgradeStatus).append('\'');
        sb.append(", topOnuUpgradeRowStatus=").append(topOnuUpgradeRowStatus);
        sb.append('}');
        return sb.toString();
    }
}
