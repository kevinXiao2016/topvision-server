/***********************************************************************
 * $Id: CmattributeExtCpeControl.java,v1.0 2011-12-13 上午11:58:47 $
 * 
 * @author: bryan
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author bryan
 * @created @2011-12-13-上午11:58:47
 * 
 */
@Alias("cmattributeExtCpeControl")
public class CmattributeExtCpeControl implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 1782247360752675043L;
    private Long cmId;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.1", index = true)
    private Long docsIfCmtsCmStatusIndex;
    @SnmpProperty(oid = "1.3.6.1.3.83.4.1.1.1.1")
    private Integer docsSubMgtCpeControlMaxCpeIp;
    @SnmpProperty(oid = "1.3.6.1.3.83.4.1.1.1.2")
    private Integer docsSubMgtCpeControlActive;
    @SnmpProperty(oid = "1.3.6.1.3.83.4.1.1.1.3")
    private Integer docsSubMgtCpeControlLearnable;
    @SnmpProperty(oid = "1.3.6.1.3.83.4.1.1.1.4")
    private Integer docsSubMgtCpeControlReset;

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
     * @return the docsIfCmtsCmStatusIndex
     */
    public Long getDocsIfCmtsCmStatusIndex() {
        return docsIfCmtsCmStatusIndex;
    }

    /**
     * @param docsIfCmtsCmStatusIndex
     *            the docsIfCmtsCmStatusIndex to set
     */
    public void setDocsIfCmtsCmStatusIndex(Long docsIfCmtsCmStatusIndex) {
        this.docsIfCmtsCmStatusIndex = docsIfCmtsCmStatusIndex;
    }

    /**
     * @return the docsSubMgtCpeControlMaxCpeIp
     */
    public Integer getDocsSubMgtCpeControlMaxCpeIp() {
        return docsSubMgtCpeControlMaxCpeIp;
    }

    /**
     * @param docsSubMgtCpeControlMaxCpeIp
     *            the docsSubMgtCpeControlMaxCpeIp to set
     */
    public void setDocsSubMgtCpeControlMaxCpeIp(Integer docsSubMgtCpeControlMaxCpeIp) {
        this.docsSubMgtCpeControlMaxCpeIp = docsSubMgtCpeControlMaxCpeIp;
    }

    /**
     * @return the docsSubMgtCpeControlActive
     */
    public Integer getDocsSubMgtCpeControlActive() {
        return docsSubMgtCpeControlActive;
    }

    /**
     * @param docsSubMgtCpeControlActive
     *            the docsSubMgtCpeControlActive to set
     */
    public void setDocsSubMgtCpeControlActive(Integer docsSubMgtCpeControlActive) {
        this.docsSubMgtCpeControlActive = docsSubMgtCpeControlActive;
    }

    /**
     * @return the docsSubMgtCpeControlLearnable
     */
    public Integer getDocsSubMgtCpeControlLearnable() {
        return docsSubMgtCpeControlLearnable;
    }

    /**
     * @param docsSubMgtCpeControlLearnable
     *            the docsSubMgtCpeControlLearnable to set
     */
    public void setDocsSubMgtCpeControlLearnable(Integer docsSubMgtCpeControlLearnable) {
        this.docsSubMgtCpeControlLearnable = docsSubMgtCpeControlLearnable;
    }

    /**
     * @return the docsSubMgtCpeControlReset
     */
    public Integer getDocsSubMgtCpeControlReset() {
        return docsSubMgtCpeControlReset;
    }

    /**
     * @param docsSubMgtCpeControlReset
     *            the docsSubMgtCpeControlReset to set
     */
    public void setDocsSubMgtCpeControlReset(Integer docsSubMgtCpeControlReset) {
        this.docsSubMgtCpeControlReset = docsSubMgtCpeControlReset;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmattributeExtCpeControl [cmId=");
        builder.append(cmId);
        builder.append(", docsIfCmtsCmStatusIndex=");
        builder.append(docsIfCmtsCmStatusIndex);
        builder.append(", docsSubMgtCpeControlMaxCpeIp=");
        builder.append(docsSubMgtCpeControlMaxCpeIp);
        builder.append(", docsSubMgtCpeControlActive=");
        builder.append(docsSubMgtCpeControlActive);
        builder.append(", docsSubMgtCpeControlLearnable=");
        builder.append(docsSubMgtCpeControlLearnable);
        builder.append(", docsSubMgtCpeControlReset=");
        builder.append(docsSubMgtCpeControlReset);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
