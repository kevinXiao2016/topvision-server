/***********************************************************************
 * $Id: PerfTempEntityRelation.java,v1.0 2013-9-3 下午1:53:41 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author fanzidong
 * @created @2013-9-3-下午1:53:41
 *
 */
public class PerfTempEntityRelation implements AliasesSuperType {
    private static final long serialVersionUID = -7125128775830866253L;
    public static final Integer ON = 1;
    public static final Integer OFF = 0;

    private Long entityId;
    private Integer templateId;
    private Integer isPerfThreshold;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public Integer getIsPerfThreshold() {
        return isPerfThreshold;
    }

    public void setIsPerfThreshold(Integer isPerfThreshold) {
        this.isPerfThreshold = isPerfThreshold;
    }

    @Override
    public String toString() {
        return "PerfTempEntityRelation [entityId=" + entityId + ", templateId=" + templateId + ", isPerfThreshold="
                + isPerfThreshold + "]";
    }
}
