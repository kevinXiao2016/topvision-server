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
import com.topvision.ems.facade.nbi.NbiSnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2015-4-22-下午4:24:40
 *
 */
public class OnuLinkQualityResult extends PerformanceResult<OperClass> implements AliasesSuperType {
    private static final long serialVersionUID = 8164842345824508360L;

    private Long entityId;
    private Long onuId;
    private Long onuIndex;
    @NbiSnmpProperty(oid = "1.3.6.1.4.1.32285.12.2.3.9.1.1.1")
    private Float onuPonTransPower;
    @NbiSnmpProperty(oid = "1.3.6.1.4.1.32285.12.2.3.9.1.1.2")
    private Float onuPonRevPower;
    @NbiSnmpProperty(oid = "1.3.6.1.4.1.32285.12.2.3.9.1.1.3")
    private Float oltPonRevPower;
    private Timestamp collectTime;

    public OnuLinkQualityResult(OperClass domain) {
        super(domain);
    }

    @Override
    public Long getEntityId() {
        return entityId;
    }

    @Override
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

    public Float getOnuPonRevPower() {
        return onuPonRevPower;
    }

    public void setOnuPonRevPower(Float onuPonRevPower) {
        this.onuPonRevPower = onuPonRevPower;
    }

    public Float getOnuPonTransPower() {
        return onuPonTransPower;
    }

    public void setOnuPonTransPower(Float onuPonTransPower) {
        this.onuPonTransPower = onuPonTransPower;
    }

    public Float getOltPonRevPower() {
        return oltPonRevPower;
    }

    public void setOltPonRevPower(Float oltPonRevPower) {
        this.oltPonRevPower = oltPonRevPower;
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
        builder.append("OnuLinkQualityResult [entityId=");
        builder.append(entityId);
        builder.append(", onuId=");
        builder.append(onuId);
        builder.append(", onuIndex=");
        builder.append(onuIndex);
        builder.append(", onuPonRevPower=");
        builder.append(onuPonRevPower);
        builder.append(", onuPonTransPower=");
        builder.append(onuPonTransPower);
        builder.append(", oltLlidRevPower=");
        builder.append(oltPonRevPower);
        builder.append(", collectTime=");
        builder.append(collectTime);
        builder.append("]");
        return builder.toString();
    }

}
