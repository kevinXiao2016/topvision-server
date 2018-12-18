/***********************************************************************
 * $Id: TopCcmtsSingleCmUpgradeObject.java,v1.0 2016年12月5日 下午1:31:46 $
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
 * @created @2016年12月5日-下午1:31:46
 *
 */
public class TopCcmtsSingleCmUpgradeObject implements AliasesSuperType {

    private static final long serialVersionUID = 6960067992144575946L;
    // Syntax: [UNIVERSAL 2] INTEGER { noOperation(0), upgrade(1) }
    public static final Integer UPGRADE = 1;
    
    
    private Long entityId;
    private Long cmcId;
    private Long cmId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.28.1.1.0", writable = true, type = "OctetString")
    private String topCcmtsSingleCmUpgradeCmMac;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.28.1.2.0", writable = true, type = "OctetString")
    private String topCcmtsSingleCmUpgradeImageFileName;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.28.1.3.0", writable = true, type = "Integer32")
    private Integer topCcmtsSingleCmUpgradeAction;

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
     * @return the topCcmtsSingleCmUpgradeCmMac
     */
    public String getTopCcmtsSingleCmUpgradeCmMac() {
        return topCcmtsSingleCmUpgradeCmMac;
    }

    /**
     * @param topCcmtsSingleCmUpgradeCmMac the topCcmtsSingleCmUpgradeCmMac to set
     */
    public void setTopCcmtsSingleCmUpgradeCmMac(String topCcmtsSingleCmUpgradeCmMac) {
        this.topCcmtsSingleCmUpgradeCmMac = topCcmtsSingleCmUpgradeCmMac;
    }

    /**
     * @return the topCcmtsSingleCmUpgradeImageFileName
     */
    public String getTopCcmtsSingleCmUpgradeImageFileName() {
        return topCcmtsSingleCmUpgradeImageFileName;
    }

    /**
     * @param topCcmtsSingleCmUpgradeImageFileName the topCcmtsSingleCmUpgradeImageFileName to set
     */
    public void setTopCcmtsSingleCmUpgradeImageFileName(String topCcmtsSingleCmUpgradeImageFileName) {
        this.topCcmtsSingleCmUpgradeImageFileName = topCcmtsSingleCmUpgradeImageFileName;
    }

    /**
     * @return the topCcmtsSingleCmUpgradeAction
     */
    public Integer getTopCcmtsSingleCmUpgradeAction() {
        return topCcmtsSingleCmUpgradeAction;
    }

    /**
     * @param topCcmtsSingleCmUpgradeAction the topCcmtsSingleCmUpgradeAction to set
     */
    public void setTopCcmtsSingleCmUpgradeAction(Integer topCcmtsSingleCmUpgradeAction) {
        this.topCcmtsSingleCmUpgradeAction = topCcmtsSingleCmUpgradeAction;
    }

}
