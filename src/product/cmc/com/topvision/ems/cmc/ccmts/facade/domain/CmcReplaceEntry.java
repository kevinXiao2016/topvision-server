/***********************************************************************
 * $Id: CmcReplaceEntry.java,v1.0 2016-4-18 下午1:53:01 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.facade.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Rod John
 * @created @2016-4-18-下午1:53:01
 *
 */
public class CmcReplaceEntry implements AliasesSuperType {
    
    private static final long serialVersionUID = 4087240865829959556L;
    private Long entityId;
    private Long cmcId;
    private Long cmcIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long ifIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.7.1.1", writable = true, type = "OctetString")
    private String topNewCcmtsMacAddr;

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
     * @return the cmcIndex
     */
    public Long getCmcIndex() {
        return cmcIndex;
    }

    /**
     * @param cmcIndex the cmcIndex to set
     */
    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    /**
     * @return the ifIndex
     */
    public Long getIfIndex() {
        return ifIndex;
    }

    /**
     * @param ifIndex the ifIndex to set
     */
    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    /**
     * @return the topNewCcmtsMacAddr
     */
    public String getTopNewCcmtsMacAddr() {
        return topNewCcmtsMacAddr;
    }

    /**
     * @param topNewCcmtsMacAddr the topNewCcmtsMacAddr to set
     */
    public void setTopNewCcmtsMacAddr(String topNewCcmtsMacAddr) {
        this.topNewCcmtsMacAddr = topNewCcmtsMacAddr;
    }

}
