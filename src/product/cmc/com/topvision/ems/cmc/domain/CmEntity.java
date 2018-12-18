/***********************************************************************
 * $Id: CmEntity.java,v1.0 2011-11-1 下午01:45:53 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2011-11-1-下午01:45:53
 * 
 */
@Alias("cmEntity")
public class CmEntity implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7748094710976294573L;
    private Long cmId;
    private String statusMacAddress;
    private String statusIpAddress;

    /**
     * @return the cmId
     */
    public Long getCmId() {
        return cmId;
    }

    /**
     * @param cmId
     *            the cmId to set
     */
    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    /**
     * @return the docsIfCmtsCmStatusMacAddress
     */
    public String getStatusMacAddress() {
        return statusMacAddress;
    }

    /**
     * @param statusMacAddress
     *            the docsIfCmtsCmStatusMacAddress to set
     */
    public void setStatusMacAddress(String statusMacAddress) {
        this.statusMacAddress = statusMacAddress;
    }

    /**
     * @return the docsIfCmtsCmStatusIpAddress
     */
    public String getStatusIpAddress() {
        return statusIpAddress;
    }

    /**
     * @param statusIpAddress
     *            the docsIfCmtsCmStatusIpAddress to set
     */
    public void setStatusIpAddress(String statusIpAddress) {
        this.statusIpAddress = statusIpAddress;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmEntity [cmId=");
        builder.append(cmId);
        builder.append(", statusMacAddress=");
        builder.append(statusMacAddress);
        builder.append(", statusIpAddress=");
        builder.append(statusIpAddress);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }
}
