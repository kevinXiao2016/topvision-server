/***********************************************************************
 * $Id: OltSubTotalInfo.java,v1.0 2014-7-12 下午4:13:21 $
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
 * @created @2014-7-12-下午4:13:21
 *
 */
public class OltSubTotalInfo implements AliasesSuperType {
    private static final long serialVersionUID = 5939366841395620944L;

    private Long entityId;

    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.1", type = "Integer32", index = true)
    private Integer onuDeviceIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.4.1.1.8", type = "Integer32")
    private Integer operationStatus;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getOnuDeviceIndex() {
        return onuDeviceIndex;
    }

    public void setOnuDeviceIndex(Integer onuDeviceIndex) {
        this.onuDeviceIndex = onuDeviceIndex;
    }

    public Integer getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(Integer operationStatus) {
        this.operationStatus = operationStatus;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltSubTotalInfo [onuDeviceIndex=");
        builder.append(onuDeviceIndex);
        builder.append(", operationStatus=");
        builder.append(operationStatus);
        builder.append("]");
        return builder.toString();
    }

}
