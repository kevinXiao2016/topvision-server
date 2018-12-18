/***********************************************************************
 * $Id: OltSniTrunkGroup.java,v1.0 2011-11-5 下午02:28:03 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.trunk.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author huqiao
 * @created @2011-11-5-下午02:28:03
 * 
 */
@Deprecated
public class OltSniTrunkGroup implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 5888203834790500973L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.2.2.2.1.1", index = true)
    private Integer sniTrunkGroupIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.2.2.2.1.2", writable = true, type = "Integer32")
    private Integer sniTrunkGroupOperationStatus;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.2.2.2.1.3", writable = true, type = "Integer32")
    private Integer sniTrunkGroupActualSpeed;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.2.2.2.1.4", writable = true, type = "Integer32")
    private Integer sniTrunkGroupAdminStatus;

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
     * @return the sniTrunkGroupIndex
     */
    public Integer getSniTrunkGroupIndex() {
        return sniTrunkGroupIndex;
    }

    /**
     * @param sniTrunkGroupIndex
     *            the sniTrunkGroupIndex to set
     */
    public void setSniTrunkGroupIndex(Integer sniTrunkGroupIndex) {
        this.sniTrunkGroupIndex = sniTrunkGroupIndex;
    }

    /**
     * @return the sniTrunkGroupOperationStatus
     */
    public Integer getSniTrunkGroupOperationStatus() {
        return sniTrunkGroupOperationStatus;
    }

    /**
     * @param sniTrunkGroupOperationStatus
     *            the sniTrunkGroupOperationStatus to set
     */
    public void setSniTrunkGroupOperationStatus(Integer sniTrunkGroupOperationStatus) {
        this.sniTrunkGroupOperationStatus = sniTrunkGroupOperationStatus;
    }

    /**
     * @return the sniTrunkGroupActualSpeed
     */
    public Integer getSniTrunkGroupActualSpeed() {
        return sniTrunkGroupActualSpeed;
    }

    /**
     * @param sniTrunkGroupActualSpeed
     *            the sniTrunkGroupActualSpeed to set
     */
    public void setSniTrunkGroupActualSpeed(Integer sniTrunkGroupActualSpeed) {
        this.sniTrunkGroupActualSpeed = sniTrunkGroupActualSpeed;
    }

    /**
     * @return the sniTrunkGroupAdminStatus
     */
    public Integer getSniTrunkGroupAdminStatus() {
        return sniTrunkGroupAdminStatus;
    }

    /**
     * @param sniTrunkGroupAdminStatus
     *            the sniTrunkGroupAdminStatus to set
     */
    public void setSniTrunkGroupAdminStatus(Integer sniTrunkGroupAdminStatus) {
        this.sniTrunkGroupAdminStatus = sniTrunkGroupAdminStatus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltSniTrunkGroup [sniTrunkGroupIndex=");
        builder.append(sniTrunkGroupIndex);
        builder.append(", sniTrunkGroupOperationStatus=");
        builder.append(sniTrunkGroupOperationStatus);
        builder.append(", sniTrunkGroupActualSpeed=");
        builder.append(sniTrunkGroupActualSpeed);
        builder.append(", sniTrunkGroupAdminStatus=");
        builder.append(sniTrunkGroupAdminStatus);
        builder.append("]");
        return builder.toString();
    }

}
