/***********************************************************************
 * $Id: TopCcmtsCmSwVersionTable.java,v1.0 2016年12月5日 下午2:02:30 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.upgrade.facade.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Rod John
 * @created @2016年12月5日-下午2:02:30
 *
 */
public class TopCcmtsCmSwVersionTable implements AliasesSuperType {

    private static final long serialVersionUID = 3863682377986245015L;
    private Long entityId;
    private Long cmcId;
    private Long cmId;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.1", index = true)
    private Long statusIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.29.1.1.1")
    private String topCcmtsCmModelNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.29.1.1.2")
    private String topCcmtsCmSwVersion;

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the cmcId
     */
    public Long getCmcId() {
        return cmcId;
    }

    /**
     * @param cmcId the cmcId to set
     */
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    /**
     * @return the cmId
     */
    public Long getCmId() {
        return cmId;
    }

    /**
     * @param cmId the cmId to set
     */
    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    /**
     * @return the statusIndex
     */
    public Long getStatusIndex() {
        return statusIndex;
    }

    /**
     * @param statusIndex the statusIndex to set
     */
    public void setStatusIndex(Long statusIndex) {
        this.statusIndex = statusIndex;
    }

    /**
     * @return the topCcmtsCmModelNum
     */
    public String getTopCcmtsCmModelNum() {
        return topCcmtsCmModelNum;
    }

    /**
     * @param topCcmtsCmModelNum the topCcmtsCmModelNum to set
     */
    public void setTopCcmtsCmModelNum(String topCcmtsCmModelNum) {
        this.topCcmtsCmModelNum = topCcmtsCmModelNum;
    }

    /**
     * @return the topCcmtsCmSwVersion
     */
    public String getTopCcmtsCmSwVersion() {
        return topCcmtsCmSwVersion;
    }

    /**
     * @param topCcmtsCmSwVersion the topCcmtsCmSwVersion to set
     */
    public void setTopCcmtsCmSwVersion(String topCcmtsCmSwVersion) {
        this.topCcmtsCmSwVersion = topCcmtsCmSwVersion;
    }

}
