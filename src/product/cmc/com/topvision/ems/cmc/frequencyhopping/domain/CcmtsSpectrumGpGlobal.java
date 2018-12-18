/***********************************************************************
 * $Id: CcmtsSpectrumGpGlobal.java,v1.0 2013-8-2 上午9:09:41 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.frequencyhopping.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author haojie
 * @created @2013-8-2-上午9:09:41
 * 
 */
public class CcmtsSpectrumGpGlobal implements Serializable {
    private static final long serialVersionUID = 1574083316104860169L;

    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.1.1.0", writable = true, type = "Integer32")
    private Integer globalAdminStatus;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.1.2.0", writable = true, type = "Integer32")
    private Integer snrQueryPeriod;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.17.2.1.3.0", writable = true, type = "Integer32")
    private Integer hopHisMaxCount;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getGlobalAdminStatus() {
        return globalAdminStatus;
    }

    public void setGlobalAdminStatus(Integer globalAdminStatus) {
        this.globalAdminStatus = globalAdminStatus;
    }

    public Integer getSnrQueryPeriod() {
        return snrQueryPeriod;
    }

    public void setSnrQueryPeriod(Integer snrQueryPeriod) {
        this.snrQueryPeriod = snrQueryPeriod;
    }

    public Integer getHopHisMaxCount() {
        return hopHisMaxCount;
    }

    public void setHopHisMaxCount(Integer hopHisMaxCount) {
        this.hopHisMaxCount = hopHisMaxCount;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CcmtsSpectrumGpGlobal [entityId=");
        builder.append(entityId);
        builder.append(", globalAdminStatus=");
        builder.append(globalAdminStatus);
        builder.append(", snrQueryPeriod=");
        builder.append(snrQueryPeriod);
        builder.append(", hopHisMaxCount=");
        builder.append(hopHisMaxCount);
        builder.append("]");
        return builder.toString();
    }

}
