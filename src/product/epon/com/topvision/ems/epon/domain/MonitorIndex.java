/***********************************************************************
 * $Id: MonitorIndex.java,v1.0 2012-5-27 下午05:43:34 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import java.io.Serializable;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2012-5-27-下午05:43:34
 * 
 */
public class MonitorIndex implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -7470396130794712836L;
    private Long monitorId;
    private Integer monitorIndex;

    public Long getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(Long monitorId) {
        this.monitorId = monitorId;
    }

    public Integer getMonitorIndex() {
        return monitorIndex;
    }

    public void setMonitorIndex(Integer monitorIndex) {
        this.monitorIndex = monitorIndex;
    }

}
