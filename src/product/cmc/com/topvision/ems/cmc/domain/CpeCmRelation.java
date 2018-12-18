/***********************************************************************
 * $Id: CpeCmRelation.java,v1.0 2011-12-14 上午10:21:15 $
 * 
 * @author: bryan
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author bryan
 * @created @2011-12-14-上午10:21:15
 * 
 */
@Alias("cpeCmRelation")
public class CpeCmRelation implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = 5291246608633182687L;
    private Long cpeId;
    private Long cmId;
    private Integer docsSubMgtCpeIpIndex;

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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CpeCmRelation [cpeId=");
        builder.append(cpeId);
        builder.append(", cmId=");
        builder.append(cmId);
        builder.append(", docsSubMgtCpeIpIndex=");
        builder.append(docsSubMgtCpeIpIndex);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
