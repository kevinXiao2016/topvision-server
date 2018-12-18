/***********************************************************************
 * $Id: TopCcmtsCmAutoUpgradeCfgTable.java,v1.0 2016年12月5日 下午1:43:57 $
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
 * @created @2016年12月5日-下午1:43:57
 *
 */
public class TopCcmtsCmAutoUpgradeCfgTable implements AliasesSuperType {

    private static final long serialVersionUID = -6086533293755285331L;
    private Long entityId;
    private Long cmcId;
    private Long cmId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.28.2.2.1.1", index = true)
    private String topCcmtsCmAutoUpgradeCmModelNum;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.28.2.2.1.2", writable = true, type = "OctetString")
    private String topCcmtsCmAutoUpgradeSwVersion;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.28.2.2.1.3", writable = true, type = "OctetString")
    private String topCcmtsCmAutoUpgradeImageFileName;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.28.2.2.1.4", writable = true, type = "Integer32")
    private Integer topCcmtsCmAutoUpgradeRowStatus;

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
     * @return the topCcmtsCmAutoUpgradeCmModelNum
     */
    public String getTopCcmtsCmAutoUpgradeCmModelNum() {
        return topCcmtsCmAutoUpgradeCmModelNum;
    }

    /**
     * @param topCcmtsCmAutoUpgradeCmModelNum the topCcmtsCmAutoUpgradeCmModelNum to set
     */
    public void setTopCcmtsCmAutoUpgradeCmModelNum(String topCcmtsCmAutoUpgradeCmModelNum) {
        this.topCcmtsCmAutoUpgradeCmModelNum = topCcmtsCmAutoUpgradeCmModelNum;
    }

    /**
     * @return the topCcmtsCmAutoUpgradeSwVersion
     */
    public String getTopCcmtsCmAutoUpgradeSwVersion() {
        return topCcmtsCmAutoUpgradeSwVersion;
    }

    /**
     * @param topCcmtsCmAutoUpgradeSwVersion the topCcmtsCmAutoUpgradeSwVersion to set
     */
    public void setTopCcmtsCmAutoUpgradeSwVersion(String topCcmtsCmAutoUpgradeSwVersion) {
        this.topCcmtsCmAutoUpgradeSwVersion = topCcmtsCmAutoUpgradeSwVersion;
    }

    /**
     * @return the topCcmtsCmAutoUpgradeImageFileName
     */
    public String getTopCcmtsCmAutoUpgradeImageFileName() {
        return topCcmtsCmAutoUpgradeImageFileName;
    }

    /**
     * @param topCcmtsCmAutoUpgradeImageFileName the topCcmtsCmAutoUpgradeImageFileName to set
     */
    public void setTopCcmtsCmAutoUpgradeImageFileName(String topCcmtsCmAutoUpgradeImageFileName) {
        this.topCcmtsCmAutoUpgradeImageFileName = topCcmtsCmAutoUpgradeImageFileName;
    }

    /**
     * @return the topCcmtsCmAutoUpgradeRowStatus
     */
    public Integer getTopCcmtsCmAutoUpgradeRowStatus() {
        return topCcmtsCmAutoUpgradeRowStatus;
    }

    /**
     * @param topCcmtsCmAutoUpgradeRowStatus the topCcmtsCmAutoUpgradeRowStatus to set
     */
    public void setTopCcmtsCmAutoUpgradeRowStatus(Integer topCcmtsCmAutoUpgradeRowStatus) {
        this.topCcmtsCmAutoUpgradeRowStatus = topCcmtsCmAutoUpgradeRowStatus;
    }

}
