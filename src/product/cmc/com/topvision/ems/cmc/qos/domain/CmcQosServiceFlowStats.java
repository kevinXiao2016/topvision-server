/***********************************************************************
 * $Id: CmcQosServiceFlowStats.java,v1.0 2011-10-31 下午07:27:29 $
 * 
 * @author: Dosion_Huang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.qos.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Dosion_Huang
 * @created @2011-10-31-下午07:27:29
 * 
 */
@Alias("cmcQosServiceFlowStats")
public class CmcQosServiceFlowStats implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 7945210529697287639L;
    private Long cmcPortId;
    private Long cmcId;
    private Long cmcIndex;// 所属CMC Macdomain ifIndex
    private Integer docsQosIfDirection;// 方向 1:DownStream 2:UpStream
    private Integer currentServiceFlowNum;// 服务流数

    /**
     * @return the cmcPortId
     */
    public Long getCmcPortId() {
        return cmcPortId;
    }

    /**
     * @param cmcPortId
     *            the cmcPortId to set
     */
    public void setCmcPortId(Long cmcPortId) {
        this.cmcPortId = cmcPortId;
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
     * @return the docsQosIfDirection
     */
    public Integer getDocsQosIfDirection() {
        return docsQosIfDirection;
    }

    /**
     * @param docsQosIfDirection
     *            the docsQosIfDirection to set
     */
    public void setDocsQosIfDirection(Integer docsQosIfDirection) {
        this.docsQosIfDirection = docsQosIfDirection;
    }

    /**
     * @return the currentServiceFlowNum
     */
    public Integer getCurrentServiceFlowNum() {
        return currentServiceFlowNum;
    }

    /**
     * @param currentServiceFlowNum
     *            the currentServiceFlowNum to set
     */
    public void setCurrentServiceFlowNum(Integer currentServiceFlowNum) {
        this.currentServiceFlowNum = currentServiceFlowNum;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcQosServiceFlowStats [cmcPortId=");
        builder.append(cmcPortId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", docsQosIfDirection=");
        builder.append(docsQosIfDirection);
        builder.append(", currentServiceFlowNum=");
        builder.append(currentServiceFlowNum);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }
}
