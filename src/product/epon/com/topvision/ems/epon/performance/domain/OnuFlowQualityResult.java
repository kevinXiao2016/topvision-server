/***********************************************************************
 * $Id: OnuFlowQualityResult.java,v1.0 2015-5-7 上午11:33:01 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.domain;

import java.util.List;

import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;

/**
 * @author flack
 * @created @2015-5-7-上午11:33:01
 *
 */
public class OnuFlowQualityResult extends PerformanceResult<OperClass> {
    private static final long serialVersionUID = -7535733194571055751L;

    private Long entityId;
    private Long onuId;
    private Long onuIndex;
    private List<OnuFlowCollectInfo> onuFlowPerfs;

    public OnuFlowQualityResult(OperClass domain) {
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

    public List<OnuFlowCollectInfo> getOnuFlowPerfs() {
        return onuFlowPerfs;
    }

    public void setOnuFlowPerfs(List<OnuFlowCollectInfo> onuFlowPerfs) {
        this.onuFlowPerfs = onuFlowPerfs;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OnuFlowQualityResult [entityId=");
        builder.append(entityId);
        builder.append(", onuId=");
        builder.append(onuId);
        builder.append(", onuIndex=");
        builder.append(onuIndex);
        builder.append(", onuFlowPerfs=");
        builder.append(onuFlowPerfs);
        builder.append("]");
        return builder.toString();
    }
}
