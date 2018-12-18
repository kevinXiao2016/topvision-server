/***********************************************************************
 * $Id: TopCcmtsSysDorType.java,v1.0 2016年9月13日 上午11:43:55 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.opticalreceiver.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2016年9月13日-上午11:43:55
 *
 */
public class TopCcmtsSysDorType implements AliasesSuperType {
    private static final long serialVersionUID = -916685251913776192L;
    public static final String OPTICALRECEIVER_TYPE_CFA = "CFA";
    public static final String OPTICALRECEIVER_TYPE_CFB = "CFB";
    public static final String OPTICALRECEIVER_TYPE_CFC = "CFC";
    public static final String OPTICALRECEIVER_TYPE_CFD = "CFD";
    public static final String OPTICALRECEIVER_TYPE_EF = "EF";
    public static final String OPTICALRECEIVER_TYPE_EP06 = "EP06";
    public static final String OPTICALRECEIVER_TYPE_EP09 = "EP09";
    public static final String OPTICALRECEIVER_TYPE_FFA = "FFA";
    public static final String OPTICALRECEIVER_TYPE_FFB = "FFB";
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long cmcIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.20")
    private String topCcmtsSysDorType;// 返回光机类型字符串

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
     * @return the topCcmtsSysDorType
     */
    public String getTopCcmtsSysDorType() {
        return topCcmtsSysDorType;
    }

    /**
     * @param topCcmtsSysDorType
     *            the topCcmtsSysDorType to set
     */
    public void setTopCcmtsSysDorType(String topCcmtsSysDorType) {
        this.topCcmtsSysDorType = topCcmtsSysDorType;
    }

}
