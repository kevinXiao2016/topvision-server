/***********************************************************************
 * $Id: OnuOnlineCollectInfo.java,v1.0 2015-4-22 下午3:48:43 $
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
 * @created @2015-4-22-下午3:48:43
 *
 */
public class OnuOnlineCollectInfo implements AliasesSuperType {
    private static final long serialVersionUID = 2335284542619789320L;

    private Long entityId;
    private Long onuId;
    private Long onuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.1", index = true)
    private Long onuDeviceIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.8", type = "Integer32")
    private Integer onuOperationStatus;

    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.1.1.1", index = true)
    private Long gponOnuDeviceIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.1.1.7", type = "Integer32")
    private Integer gponOnuOperationStatus;

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
        this.onuDeviceIndex = this.gponOnuDeviceIndex = EponIndex.getOnuMibIndexByIndex(onuIndex);
    }

    public Long getOnuDeviceIndex() {
        return onuDeviceIndex;
    }

    public void setOnuDeviceIndex(Long onuDeviceIndex) {
        this.onuDeviceIndex = this.gponOnuDeviceIndex = onuDeviceIndex;
        this.onuIndex = EponIndex.getOnuIndexByMibIndex(onuDeviceIndex);
    }

    public Integer getOnuOperationStatus() {
        return onuOperationStatus;
    }

    public void setOnuOperationStatus(Integer onuOperationStatus) {
        this.onuOperationStatus = onuOperationStatus;
    }

    /**
     * @return the gponOnuDeviceIndex
     */
    public Long getGponOnuDeviceIndex() {
        return gponOnuDeviceIndex;
    }

    /**
     * @param gponOnuDeviceIndex the gponOnuDeviceIndex to set
     */
    public void setGponOnuDeviceIndex(Long gponOnuDeviceIndex) {
        this.gponOnuDeviceIndex = this.onuDeviceIndex = gponOnuDeviceIndex;
        this.onuIndex = EponIndex.getOnuIndexByMibIndex(gponOnuDeviceIndex);
    }

    /**
     * @return the gponOnuOperationStatus
     */
    public Integer getGponOnuOperationStatus() {
        return gponOnuOperationStatus;
    }

    /**
     * @param gponOnuOperationStatus the gponOnuOperationStatus to set
     */
    public void setGponOnuOperationStatus(Integer gponOnuOperationStatus) {
        this.gponOnuOperationStatus = this.onuOperationStatus = gponOnuOperationStatus;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OnuOnlineCollectInfo [entityId=");
        builder.append(entityId);
        builder.append(", onuId=");
        builder.append(onuId);
        builder.append(", onuIndex=");
        builder.append(onuIndex);
        builder.append(", onuDeviceIndex=");
        builder.append(onuDeviceIndex);
        builder.append(", onuOperationStatus=");
        builder.append(onuOperationStatus);
        builder.append("]");
        return builder.toString();
    }

}
