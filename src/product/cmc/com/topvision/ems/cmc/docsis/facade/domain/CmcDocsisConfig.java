/***********************************************************************
 * $Id: CmcDocsisConfig.java,v1.0 2013-4-28 下午1:53:46 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.docsis.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2013-4-28-下午1:53:46
 *
 */
@Alias("cmcDocsisConfig")
public class CmcDocsisConfig implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -5558308181403601901L;

    private Long cmcId;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long ifIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.20.1.16.1.1", writable = true, type = "Gauge32")
    private Integer ccmtsMddInterval;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.20.1.16.1.5", writable = true, type = "Integer32")
    private Integer ccmtsMdfEnabled;
    private String cmcType;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the ifIndex
     */
    public Long getIfIndex() {
        return ifIndex;
    }

    /**
     * @param ifIndex
     *            the ifIndex to set
     */
    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    public Integer getCcmtsMddInterval() {
        return ccmtsMddInterval;
    }

    public void setCcmtsMddInterval(Integer ccmtsMddInterval) {
        this.ccmtsMddInterval = ccmtsMddInterval;
    }

    public Integer getCcmtsMdfEnabled() {
        return ccmtsMdfEnabled;
    }

    public void setCcmtsMdfEnabled(Integer ccmtsMdfEnabled) {
        this.ccmtsMdfEnabled = ccmtsMdfEnabled;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public String getCmcType() {
        return cmcType;
    }

    public void setCmcType(String cmcType) {
        this.cmcType = cmcType;
    }

}
