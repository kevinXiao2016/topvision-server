/***********************************************************************
 * $Id: CmcReplaceInfo.java,v1.0 2016-4-20 下午4:10:16 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Rod John
 * @created @2016-4-20-下午4:10:16
 *
 */
public class CmcReplaceInfo implements AliasesSuperType {

    private static final long serialVersionUID = 999731583068955910L;
    public Long entityId;
    public String name;
    public String entityIp;
    public String mac;
    public String entityName;
    public Integer status;
    public String softVersion;
    public String createTime;

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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the entityIp
     */
    public String getEntityIp() {
        return entityIp;
    }

    /**
     * @param entityIp the entityIp to set
     */
    public void setEntityIp(String entityIp) {
        this.entityIp = entityIp;
    }

    /**
     * @return the mac
     */
    public String getMac() {
        return mac;
    }

    /**
     * @param mac the mac to set
     */
    public void setMac(String mac) {
        this.mac = mac;
    }

    /**
     * @return the entityName
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * @param entityName the entityName to set
     */
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return the softVersion
     */
    public String getSoftVersion() {
        return softVersion;
    }

    /**
     * @param softVersion the softVersion to set
     */
    public void setSoftVersion(String softVersion) {
        this.softVersion = softVersion;
    }

    /**
     * @return the createTime
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

}
