/***********************************************************************
 * $ com.topvision.ems.facade.domain.PerformanceResult,v1.0 2012-5-6 9:08:46 $
 *
 * @author: Administrator
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.domain;

import com.topvision.ems.facade.util.FileData;

/**
 * @author Administrator
 * @created @2012-5-6-9:08:46
 */
public abstract class PerformanceResult<T extends OperClass> extends FileData {
    private static final long serialVersionUID = -7564827834606671896L;
    private T domain;
    private Long entityId;

    protected PerformanceResult(T domain) {
        super();
        this.domain = domain;
    }

    public T getDomain() {
        return domain;
    }

    public void setDomain(T domain) {
        this.domain = domain;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
}
