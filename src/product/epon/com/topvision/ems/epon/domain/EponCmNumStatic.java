/***********************************************************************
 * $Id: CmcCmNumStatic.java,v1.0 2012-7-25 下午02:36:57 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import org.apache.ibatis.type.Alias;

/**
 * @author loyal
 * @created @2012-7-25-下午02:36:57
 * 
 */
@Alias("eponCmNumStatic")
public class EponCmNumStatic implements AliasesSuperType {
    private static final long serialVersionUID = 3238121575035101426L;
    private Long entityId;
    private Integer cmNumTotal;
    private Integer cmNumOnline;
    private Integer cmNumActive;
    private Integer cmNumOffline;

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the cmNumTotal
     */
    public Integer getCmNumTotal() {
        return cmNumTotal;
    }

    /**
     * @param cmNumTotal
     *            the cmNumTotal to set
     */
    public void setCmNumTotal(Integer cmNumTotal) {
        this.cmNumTotal = cmNumTotal;
    }

    /**
     * @return the cmNumOnline
     */
    public Integer getCmNumOnline() {
        return cmNumOnline;
    }

    /**
     * @param cmNumOnline
     *            the cmNumOnline to set
     */
    public void setCmNumOnline(Integer cmNumOnline) {
        this.cmNumOnline = cmNumOnline;
    }

    /**
     * @return the cmNumActive
     */
    public Integer getCmNumActive() {
        return cmNumActive;
    }

    /**
     * @param cmNumActive
     *            the cmNumActive to set
     */
    public void setCmNumActive(Integer cmNumActive) {
        this.cmNumActive = cmNumActive;
    }

    /**
     * @return the cmNumOffline
     */
    public Integer getCmNumOffline() {
        return cmNumOffline;
    }

    /**
     * @param cmNumOffline
     *            the cmNumOffline to set
     */
    public void setCmNumOffline(Integer cmNumOffline) {
        this.cmNumOffline = cmNumOffline;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcCmNumStatic [entityId=");
        builder.append(entityId);
        builder.append(", cmNumTotal=");
        builder.append(cmNumTotal);
        builder.append(", cmNumOnline=");
        builder.append(cmNumOnline);
        builder.append(", cmNumActive=");
        builder.append(cmNumActive);
        builder.append(", cmNumOffline=");
        builder.append(cmNumOffline);
        return builder.toString();
    }
    
    
}
