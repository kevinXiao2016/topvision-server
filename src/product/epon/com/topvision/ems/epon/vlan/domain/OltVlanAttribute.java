/***********************************************************************
 * $ VlanAttribute.java,v1.0 2011-10-25 9:56:54 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2011-10-25-9:56:54
 */
public class OltVlanAttribute implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 1408633852669023618L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.1.1.1", index = true)
    private Long deviceNo = 1L;
    /**
     * 最大VLAN ID
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.1.1.2")
    private Integer maxVlanId;
    /**
     * 最大支持的VLAN个数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.1.1.3")
    private Integer maxSupportVlans;
    /**
     * 已创建的VLAN个数
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.7.1.1.4")
    private Integer createdVlanNumber;

    public Long getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(Long deviceNo) {
        this.deviceNo = deviceNo;
    }

    public Integer getCreatedVlanNumber() {
        return createdVlanNumber;
    }

    public void setCreatedVlanNumber(Integer createdVlanNumber) {
        this.createdVlanNumber = createdVlanNumber;
    }

    public Integer getMaxSupportVlans() {
        return maxSupportVlans;
    }

    public void setMaxSupportVlans(Integer maxSupportVlans) {
        this.maxSupportVlans = maxSupportVlans;
    }

    public Integer getMaxVlanId() {
        return maxVlanId;
    }

    public void setMaxVlanId(Integer maxVlanId) {
        this.maxVlanId = maxVlanId;
    }

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OltVlanAttribute");
        sb.append("{createdVlanNumber='").append(createdVlanNumber).append('\'');
        sb.append(", deviceNo=").append(deviceNo);
        sb.append(", maxVlanId='").append(maxVlanId).append('\'');
        sb.append(", maxSupportVlans='").append(maxSupportVlans).append('\'');
        sb.append('}');
        return sb.toString();
    }
}