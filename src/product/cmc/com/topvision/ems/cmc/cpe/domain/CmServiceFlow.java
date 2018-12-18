/***********************************************************************
 * $Id: CmLocateInfo.java,v1.0 2015年2月10日 上午11:08:45 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cpe.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

import java.io.Serializable;

/**
 * @author jay
 * @created @2015年2月10日-上午11:08:45
 *
 */
public class CmServiceFlow implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -4493242165379207556L;

    private Long cmId;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.1", index = true)
    private Long cmIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.18.1.1", index = true)
    private Long serviceFlowId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.18.1.2")
    private Integer serviceFlowType;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.2.18.1.3")
    private Long serviceFlowRate;

    public Long getCmId() {
        return cmId;
    }

    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getCmIndex() {
        return cmIndex;
    }

    public void setCmIndex(Long cmIndex) {
        this.cmIndex = cmIndex;
    }

    public Long getServiceFlowId() {
        return serviceFlowId;
    }

    public void setServiceFlowId(Long serviceFlowId) {
        this.serviceFlowId = serviceFlowId;
    }

    public Integer getServiceFlowType() {
        return serviceFlowType;
    }

    public void setServiceFlowType(Integer serviceFlowType) {
        this.serviceFlowType = serviceFlowType;
    }

    public Long getServiceFlowRate() {
        return serviceFlowRate;
    }

    public void setServiceFlowRate(Long serviceFlowRate) {
        this.serviceFlowRate = serviceFlowRate;
    }
}
