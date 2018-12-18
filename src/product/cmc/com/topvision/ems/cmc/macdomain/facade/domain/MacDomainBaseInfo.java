/***********************************************************************
 * $Id: MacDomainBaseInfo.java,v1.0 2011-10-26 下午04:05:32 $
 * 
 * @author: xionghao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.macdomain.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.ems.cmc.util.CmcUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author xionghao
 * @created @2011-10-26-下午04:05:32
 * 
 */
@Alias("macDomainBaseInfo")
public class MacDomainBaseInfo implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -6507019041821703804L;
    private Long cmcId;
    public static final String[] macStorage = { "", "other", "volatile", "nonVolatile", "permanent", "readOnly" };
    public static final String[] cmtsCapabilities = { "atmCells", "concatenation" };
    private String docsIfCmtsCapabilitiesString;
    private String docsIfCmtsMacStorageTypeString;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long cmcIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.1.1.1")
    private String docsIfCmtsCapabilities;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.1.1.2", writable = true, type = "Integer32")
    private Integer docsIfCmtsSyncInterval;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.1.1.3", writable = true, type = "Integer32")
    private Integer docsIfCmtsUcdInterval;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.1.1.4")
    private Integer docsIfCmtsMaxServiceIds;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.1.1.5", writable = true, type = "Integer32")
    private Long docsIfCmtsInsertionInterval;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.1.1.6", writable = true, type = "Integer32")
    private Integer invitedRangingAttempts;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.1.1.7", writable = true, type = "Integer32")
    private Long docsIfCmtsInsertInterval;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.1.1.8")
    private Integer docsIfCmtsMacStorageType;

    /**
     * @return the cmcId
     */
    public Long getCmcId() {
        return cmcId;
    }

    /**
     * @param cmcId
     *            the cmcId to set
     */
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    /**
     * @return the docsIfCmtsCapabilities
     */
    public String getDocsIfCmtsCapabilities() {
        return docsIfCmtsCapabilities;
    }

    /**
     * @param docsIfCmtsCapabilities
     *            the docsIfCmtsCapabilities to set
     */
    public void setDocsIfCmtsCapabilities(String docsIfCmtsCapabilities) {
        this.docsIfCmtsCapabilities = docsIfCmtsCapabilities;
        this.docsIfCmtsCapabilitiesString = CmcUtil.turnBitsToString(docsIfCmtsCapabilities, cmtsCapabilities);
    }

    /**
     * @return the docsIfCmtsSyncInterval
     */
    public Integer getDocsIfCmtsSyncInterval() {
        return docsIfCmtsSyncInterval;
    }

    /**
     * @param docsIfCmtsSyncInterval
     *            the docsIfCmtsSyncInterval to set
     */
    public void setDocsIfCmtsSyncInterval(Integer docsIfCmtsSyncInterval) {
        this.docsIfCmtsSyncInterval = docsIfCmtsSyncInterval;
    }

    /**
     * @return the docsIfCmtsUcdInterval
     */
    public Integer getDocsIfCmtsUcdInterval() {
        return docsIfCmtsUcdInterval;
    }

    /**
     * @param docsIfCmtsUcdInterval
     *            the docsIfCmtsUcdInterval to set
     */
    public void setDocsIfCmtsUcdInterval(Integer docsIfCmtsUcdInterval) {
        this.docsIfCmtsUcdInterval = docsIfCmtsUcdInterval;
    }

    /**
     * @return the docsIfCmtsMaxServiceIds
     */
    public Integer getDocsIfCmtsMaxServiceIds() {
        return docsIfCmtsMaxServiceIds;
    }

    /**
     * @param docsIfCmtsMaxServiceIds
     *            the docsIfCmtsMaxServiceIds to set
     */
    public void setDocsIfCmtsMaxServiceIds(Integer docsIfCmtsMaxServiceIds) {
        this.docsIfCmtsMaxServiceIds = docsIfCmtsMaxServiceIds;
    }

    /**
     * @return the docsIfCmtsInsertionInterval
     */
    public Long getDocsIfCmtsInsertionInterval() {
        return docsIfCmtsInsertionInterval;
    }

    /**
     * @param docsIfCmtsInsertionInterval
     *            the docsIfCmtsInsertionInterval to set
     */
    public void setDocsIfCmtsInsertionInterval(Long docsIfCmtsInsertionInterval) {
        this.docsIfCmtsInsertionInterval = docsIfCmtsInsertionInterval;
    }

    /**
     * @return the docsIfCmtsInvitedRangingAttempts
     */
    public Integer getInvitedRangingAttempts() {
        return invitedRangingAttempts;
    }

    /**
     * @param invitedRangingAttempts
     *            the docsIfCmtsInvitedRangingAttempts to set
     */
    public void setInvitedRangingAttempts(Integer invitedRangingAttempts) {
        this.invitedRangingAttempts = invitedRangingAttempts;
    }

    /**
     * @return the docsIfCmtsInsertInterval
     */
    public Long getDocsIfCmtsInsertInterval() {
        return docsIfCmtsInsertInterval;
    }

    /**
     * @param docsIfCmtsInsertInterval
     *            the docsIfCmtsInsertInterval to set
     */
    public void setDocsIfCmtsInsertInterval(Long docsIfCmtsInsertInterval) {
        this.docsIfCmtsInsertInterval = docsIfCmtsInsertInterval;
    }

    /**
     * @return the docsIfCmtsMacStorageType
     */
    public Integer getDocsIfCmtsMacStorageType() {
        return docsIfCmtsMacStorageType;
    }

    /**
     * @param docsIfCmtsMacStorageType
     *            the docsIfCmtsMacStorageType to set
     */
    public void setDocsIfCmtsMacStorageType(Integer docsIfCmtsMacStorageType) {
        this.docsIfCmtsMacStorageType = docsIfCmtsMacStorageType;
        this.docsIfCmtsMacStorageTypeString = macStorage[docsIfCmtsMacStorageType];
    }

    /**
     * @return the docsIfCmtsCapabilitiesString
     */
    public String getDocsIfCmtsCapabilitiesString() {
        return docsIfCmtsCapabilitiesString;
    }

    /**
     * @param docsIfCmtsCapabilitiesString
     *            the docsIfCmtsCapabilitiesString to set
     */
    public void setDocsIfCmtsCapabilitiesString(String docsIfCmtsCapabilitiesString) {
        this.docsIfCmtsCapabilitiesString = docsIfCmtsCapabilitiesString;
    }

    /**
     * @return the docsIfCmtsMacStorageTypeString
     */
    public String getDocsIfCmtsMacStorageTypeString() {
        return docsIfCmtsMacStorageTypeString;
    }

    /**
     * @param docsIfCmtsMacStorageTypeString
     *            the docsIfCmtsMacStorageTypeString to set
     */
    public void setDocsIfCmtsMacStorageTypeString(String docsIfCmtsMacStorageTypeString) {
        this.docsIfCmtsMacStorageTypeString = docsIfCmtsMacStorageTypeString;
    }

    /**
     * @return the cmcIndex
     */
    public Long getCmcIndex() {
        return cmcIndex;
    }

    /**
     * @param cmcIndex
     *            the cmcIndex to set
     */
    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MacDomainBaseInfo [cmcId=");
        builder.append(cmcId);
        builder.append(", docsIfCmtsCapabilitiesString=");
        builder.append(docsIfCmtsCapabilitiesString);
        builder.append(", docsIfCmtsMacStorageTypeString=");
        builder.append(docsIfCmtsMacStorageTypeString);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", docsIfCmtsCapabilities=");
        builder.append(docsIfCmtsCapabilities);
        builder.append(", docsIfCmtsSyncInterval=");
        builder.append(docsIfCmtsSyncInterval);
        builder.append(", docsIfCmtsUcdInterval=");
        builder.append(docsIfCmtsUcdInterval);
        builder.append(", docsIfCmtsMaxServiceIds=");
        builder.append(docsIfCmtsMaxServiceIds);
        builder.append(", docsIfCmtsInsertionInterval=");
        builder.append(docsIfCmtsInsertionInterval);
        builder.append(", invitedRangingAttempts=");
        builder.append(invitedRangingAttempts);
        builder.append(", docsIfCmtsInsertInterval=");
        builder.append(docsIfCmtsInsertInterval);
        builder.append(", docsIfCmtsMacStorageType=");
        builder.append(docsIfCmtsMacStorageType);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
