/***********************************************************************
 * $Id: CpeAttribute.java,v1.0 2011-12-13 上午11:30:09 $
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
 * @created @2011-12-13-上午11:30:09
 * 
 */
@Alias("cpeAttribute")
public class CpeAttribute implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -6412481552138749948L;
    private Long cpeId;

    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.3.3.1.1", index = true)
    private Long docsIfCmtsCmStatusIndex;
    @SnmpProperty(oid = "1.3.6.1.3.83.4.1.5.1.1", index = true)
    private Integer docsSubMgtCpeIpIndex;
    @SnmpProperty(oid = "1.3.6.1.3.83.4.1.5.1.2")
    private String docsSubMgtCpeIpAddr;
    @SnmpProperty(oid = "1.3.6.1.3.83.4.1.5.1.3")
    private Integer docsSubMgtCpeIpLearned;
    @SnmpProperty(oid = "1.3.6.1.3.83.4.1.5.1.4")
    private Integer docsSubMgtCpeType;

    private String docsSubMgtCpeIpLearnedName;
    private String docsSubMgtCpeTypeName;
    public static final String[] LEARNEDTYPES = { "", "ON", "OFF" };
    public static final String[] CPETYPES = { "", "CPE", "PS", "MTA", "STB" };

    /**
     * @return the cpeId
     */
    public Long getCpeId() {
        return cpeId;
    }

    /**
     * @param cpeId
     *            the cpeId to set
     */
    public void setCpeId(Long cpeId) {
        this.cpeId = cpeId;
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
     * @return the docsSubMgtCpeIpIndex
     */
    public Integer getDocsSubMgtCpeIpIndex() {
        return docsSubMgtCpeIpIndex;
    }

    /**
     * @param docsSubMgtCpeIpIndex
     *            the docsSubMgtCpeIpIndex to set
     */
    public void setDocsSubMgtCpeIpIndex(Integer docsSubMgtCpeIpIndex) {
        this.docsSubMgtCpeIpIndex = docsSubMgtCpeIpIndex;
    }

    /**
     * @return the docsSubMgtCpeIpAddr
     */
    public String getDocsSubMgtCpeIpAddr() {
        return docsSubMgtCpeIpAddr;
    }

    /**
     * @param docsSubMgtCpeIpAddr
     *            the docsSubMgtCpeIpAddr to set
     */
    public void setDocsSubMgtCpeIpAddr(String docsSubMgtCpeIpAddr) {
        this.docsSubMgtCpeIpAddr = docsSubMgtCpeIpAddr;
    }

    /**
     * @return the docsSubMgtCpeIpLearned
     */
    public Integer getDocsSubMgtCpeIpLearned() {
        return docsSubMgtCpeIpLearned;
    }

    /**
     * @param docsSubMgtCpeIpLearned
     *            the docsSubMgtCpeIpLearned to set
     */
    public void setDocsSubMgtCpeIpLearned(Integer docsSubMgtCpeIpLearned) {
        this.docsSubMgtCpeIpLearned = docsSubMgtCpeIpLearned;
    }

    /**
     * @return the docsSubMgtCpeType
     */
    public Integer getDocsSubMgtCpeType() {
        return docsSubMgtCpeType;
    }

    /**
     * @param docsSubMgtCpeType
     *            the docsSubMgtCpeType to set
     */
    public void setDocsSubMgtCpeType(Integer docsSubMgtCpeType) {
        this.docsSubMgtCpeType = docsSubMgtCpeType;
    }

    /**
     * @return the docsSubMgtCpeIpLearnedName
     */
    public String getDocsSubMgtCpeIpLearnedName() {
        Integer ipLearned = this.getDocsSubMgtCpeIpLearned();
        if (ipLearned != null) {
            this.setDocsSubMgtCpeIpLearnedName(LEARNEDTYPES[ipLearned]);
        }
        return docsSubMgtCpeIpLearnedName;
    }

    /**
     * @param docsSubMgtCpeIpLearnedName
     *            the docsSubMgtCpeIpLearnedName to set
     */
    public void setDocsSubMgtCpeIpLearnedName(String docsSubMgtCpeIpLearnedName) {
        this.docsSubMgtCpeIpLearnedName = docsSubMgtCpeIpLearnedName;
    }

    /**
     * @return the docsSubMgtCpeTypeName
     */
    public String getDocsSubMgtCpeTypeName() {
        Integer type = this.getDocsSubMgtCpeType();
        if (type != null) {
            this.setDocsSubMgtCpeTypeName(CPETYPES[type]);
        }
        return docsSubMgtCpeTypeName;
    }

    /**
     * @param docsSubMgtCpeTypeName
     *            the docsSubMgtCpeTypeName to set
     */
    public void setDocsSubMgtCpeTypeName(String docsSubMgtCpeTypeName) {
        this.docsSubMgtCpeTypeName = docsSubMgtCpeTypeName;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CpeAttribute [cpeId=");
        builder.append(cpeId);
        builder.append(", docsIfCmtsCmStatusIndex=");
        builder.append(docsIfCmtsCmStatusIndex);
        builder.append(", docsSubMgtCpeIpIndex=");
        builder.append(docsSubMgtCpeIpIndex);
        builder.append(", docsSubMgtCpeIpAddr=");
        builder.append(docsSubMgtCpeIpAddr);
        builder.append(", docsSubMgtCpeIpLearned=");
        builder.append(docsSubMgtCpeIpLearned);
        builder.append(", docsSubMgtCpeType=");
        builder.append(docsSubMgtCpeType);
        builder.append(", docsSubMgtCpeIpLearnedName=");
        builder.append(docsSubMgtCpeIpLearnedName);
        builder.append(", docsSubMgtCpeTypeName=");
        builder.append(docsSubMgtCpeTypeName);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
