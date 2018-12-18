/***********************************************************************
 * $Id: CcmtsFftMonitorScalar.java,v1.0 2013-12-3 上午10:09:39 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author haojie
 * @created @2013-12-3-上午10:09:39
 * 
 */
public class CcmtsFftMonitorScalar implements Serializable {
    private static final long serialVersionUID = 4986680671227808852L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.16.1.1.0", writable = true, type = "Integer32")
    private Integer fftMonitorGlbStatus;// 0 off 1 on

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getFftMonitorGlbStatus() {
        return fftMonitorGlbStatus;
    }

    public void setFftMonitorGlbStatus(Integer fftMonitorGlbStatus) {
        this.fftMonitorGlbStatus = fftMonitorGlbStatus;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CcmtsFftMonitorScalar [entityId=");
        builder.append(entityId);
        builder.append(", fftMonitorGlbStatus=");
        builder.append(fftMonitorGlbStatus);
        builder.append("]");
        return builder.toString();
    }

}
