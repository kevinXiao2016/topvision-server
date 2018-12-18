/***********************************************************************
 * $Id: OnuOnlineResult.java,v1.0 2015-4-22 下午4:24:40 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.domain;

import java.sql.Timestamp;

import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2015-4-22-下午4:24:40
 *
 */
public class OnuOnlineResult extends PerformanceResult<OperClass> implements AliasesSuperType {
    private static final long serialVersionUID = 1946755983938249508L;

    private Long entityId;
    private Long onuId;
    private Long onuIndex;
    private Integer onuOnlneStatus;
    private Timestamp collectTime;

    public OnuOnlineResult(OperClass domain) {
        super(domain);
    }

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
    }

    public Integer getOnuOnlneStatus() {
        return onuOnlneStatus;
    }

    public void setOnuOnlneStatus(Integer onuOnlneStatus) {
        this.onuOnlneStatus = onuOnlneStatus;
    }

    public Timestamp getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OnuOnlineResult [entityId=");
        builder.append(entityId);
        builder.append(", onuId=");
        builder.append(onuId);
        builder.append(", onuIndex=");
        builder.append(onuIndex);
        builder.append(", onuOnlneStatus=");
        builder.append(onuOnlneStatus);
        builder.append(", collectTime=");
        builder.append(collectTime);
        builder.append("]");
        return builder.toString();
    }

}
