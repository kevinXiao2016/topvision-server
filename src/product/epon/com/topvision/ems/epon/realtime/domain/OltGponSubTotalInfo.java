/***********************************************************************
 * $Id: OltGponSubTotalInfo.java,v1.0 2017年2月16日 下午2:25:18 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.realtime.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author lizongtian
 * @created @2017年2月16日-下午2:25:18
 *
 */
public class OltGponSubTotalInfo implements AliasesSuperType {

    private static final long serialVersionUID = 3411821025751644792L;
    private Long entityId;

    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.1.1.1", index = true)
    private Integer onuDeviceIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.1.1.7")
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

}
