/***********************************************************************
 * $Id: OltVlanInterface.java,v1.0 2013-3-21 下午7:15:17 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;


/**
 * @author Bravin
 * @created @2013-3-21-下午7:15:17
 *
 */
public class OltVlanInterface implements AliasesSuperType {
    private static final long serialVersionUID = -8753930303390849133L;
    private Integer vlanIndex;
    private String vlanIpAddress;
    private String vlanIpMask;
    private Long entityId;

    /**
     * @return the vlanIndex
     */
    public Integer getVlanIndex() {
        return vlanIndex;
    }

    /**
     * @param vlanIndex the vlanIndex to set
     */
    public void setVlanIndex(Integer vlanIndex) {
        this.vlanIndex = vlanIndex;
    }

    /**
     * @return the vlanIpAddress
     */
    public String getVlanIpAddress() {
        return vlanIpAddress;
    }

    /**
     * @param vlanIpAddress the vlanIpAddress to set
     */
    public void setVlanIpAddress(String vlanIpAddress) {
        this.vlanIpAddress = vlanIpAddress;
    }

    /**
     * @return the vlanIpMask
     */
    public String getVlanIpMask() {
        return vlanIpMask;
    }

    /**
     * @param vlanIpMask the vlanIpMask to set
     */
    public void setVlanIpMask(String vlanIpMask) {
        this.vlanIpMask = vlanIpMask;
    }

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

}
