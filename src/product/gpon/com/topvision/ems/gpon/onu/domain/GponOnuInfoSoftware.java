/***********************************************************************
 * $Id: GponOnuInfoSoftware.java,v1.0 2016年10月15日 下午3:30:53 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onu.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Rod John
 * @created @2016年10月15日-下午3:30:53
 *
 */
public class GponOnuInfoSoftware implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = -4407138089407311149L;

    private Long entityId;
    private Long onuId;
    private Long onuIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.2.1.1", index = true)
    private Long onuSoftwareDeviceIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.2.1.2")
    private String onuSoftware0Version;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.2.1.3")
    private Integer onuSoftware0Valid;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.2.1.4", writable = true, type = "Integer")
    private Integer onuSoftware0Active;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.2.1.5", writable = true, type = "Integer")
    private Integer onuSoftware0Commited;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.2.1.6")
    private String onuSoftware1Version;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.2.1.7")
    private Integer onuSoftware1Valid;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.2.1.8", writable = true, type = "Integer")
    private Integer onuSoftware1Active;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.8.4.2.1.9", writable = true, type = "Integer")
    private Integer onuSoftware1Commited;

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the onuId
     */
    public Long getOnuId() {
        return onuId;
    }

    /**
     * @param onuId the onuId to set
     */
    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    /**
     * @return the onuIndex
     */
    public Long getOnuIndex() {
        return onuIndex;
    }

    /**
     * @param onuIndex the onuIndex to set
     */
    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
        onuSoftwareDeviceIndex = EponIndex.getOnuMibIndexByIndex(onuIndex);
    }

    /**
     * @return the onuSoftwareDeviceIndex
     */
    public Long getOnuSoftwareDeviceIndex() {
        return onuSoftwareDeviceIndex;
    }

    /**
     * @param onuSoftwareDeviceIndex the onuSoftwareDeviceIndex to set
     */
    public void setOnuSoftwareDeviceIndex(Long onuSoftwareDeviceIndex) {
        this.onuSoftwareDeviceIndex = onuSoftwareDeviceIndex;
        onuIndex = EponIndex.getOnuIndexByMibIndex(onuSoftwareDeviceIndex);
    }

    /**
     * @return the onuSoftware0Version
     */
    public String getOnuSoftware0Version() {
        return onuSoftware0Version;
    }

    /**
     * @param onuSoftware0Version the onuSoftware0Version to set
     */
    public void setOnuSoftware0Version(String onuSoftware0Version) {
        this.onuSoftware0Version = onuSoftware0Version;
    }

    /**
     * @return the onuSoftware0Valid
     */
    public Integer getOnuSoftware0Valid() {
        return onuSoftware0Valid;
    }

    /**
     * @param onuSoftware0Valid the onuSoftware0Valid to set
     */
    public void setOnuSoftware0Valid(Integer onuSoftware0Valid) {
        this.onuSoftware0Valid = onuSoftware0Valid;
    }

    /**
     * @return the onuSoftware0Active
     */
    public Integer getOnuSoftware0Active() {
        return onuSoftware0Active;
    }

    /**
     * @param onuSoftware0Active the onuSoftware0Active to set
     */
    public void setOnuSoftware0Active(Integer onuSoftware0Active) {
        this.onuSoftware0Active = onuSoftware0Active;
    }

    /**
     * @return the onuSoftware0Commited
     */
    public Integer getOnuSoftware0Commited() {
        return onuSoftware0Commited;
    }

    /**
     * @param onuSoftware0Commited the onuSoftware0Commited to set
     */
    public void setOnuSoftware0Commited(Integer onuSoftware0Commited) {
        this.onuSoftware0Commited = onuSoftware0Commited;
    }

    /**
     * @return the onuSoftware1Version
     */
    public String getOnuSoftware1Version() {
        return onuSoftware1Version;
    }

    /**
     * @param onuSoftware1Version the onuSoftware1Version to set
     */
    public void setOnuSoftware1Version(String onuSoftware1Version) {
        this.onuSoftware1Version = onuSoftware1Version;
    }

    /**
     * @return the onuSoftware1Valid
     */
    public Integer getOnuSoftware1Valid() {
        return onuSoftware1Valid;
    }

    /**
     * @param onuSoftware1Valid the onuSoftware1Valid to set
     */
    public void setOnuSoftware1Valid(Integer onuSoftware1Valid) {
        this.onuSoftware1Valid = onuSoftware1Valid;
    }

    /**
     * @return the onuSoftware1Active
     */
    public Integer getOnuSoftware1Active() {
        return onuSoftware1Active;
    }

    /**
     * @param onuSoftware1Active the onuSoftware1Active to set
     */
    public void setOnuSoftware1Active(Integer onuSoftware1Active) {
        this.onuSoftware1Active = onuSoftware1Active;
    }

    /**
     * @return the onuSoftware1Commited
     */
    public Integer getOnuSoftware1Commited() {
        return onuSoftware1Commited;
    }

    /**
     * @param onuSoftware1Commited the onuSoftware1Commited to set
     */
    public void setOnuSoftware1Commited(Integer onuSoftware1Commited) {
        this.onuSoftware1Commited = onuSoftware1Commited;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "GponOnuInfoSoftware [entityId=" + entityId + ", onuId=" + onuId + ", onuIndex=" + onuIndex
                + ", onuSoftwareDeviceIndex=" + onuSoftwareDeviceIndex + ", onuSoftware0Version=" + onuSoftware0Version
                + ", onuSoftware0Valid=" + onuSoftware0Valid + ", onuSoftware0Active=" + onuSoftware0Active
                + ", onuSoftware0Commited=" + onuSoftware0Commited + ", onuSoftware1Version=" + onuSoftware1Version
                + ", onuSoftware1Valid=" + onuSoftware1Valid + ", onuSoftware1Active=" + onuSoftware1Active
                + ", onuSoftware1Commited=" + onuSoftware1Commited + "]";
    }

}
