/***********************************************************************
 * $Id: CmcChannelStaticInfo.java,v1.0 2013-11-11 下午03:38:09 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Rod John
 * @created @2013-11-11-下午03:38:09
 * 
 */
public class CmcChannelStaticInfo implements AliasesSuperType {
    private static final long serialVersionUID = 3737398018375984160L;
    private Long cmcId;
    private Long cmcIndex;
    private Double max;
    private Double min;
    private Double avg;

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
     * @return the max
     */
    public Double getMax() {
        return max;
    }

    /**
     * @param max
     *            the max to set
     */
    public void setMax(Double max) {
        this.max = max;
    }

    /**
     * @return the min
     */
    public Double getMin() {
        return min;
    }

    /**
     * @param min
     *            the min to set
     */
    public void setMin(Double min) {
        this.min = min;
    }

    /**
     * @return the avg
     */
    public Double getAvg() {
        return avg;
    }

    /**
     * @param avg
     *            the avg to set
     */
    public void setAvg(Double avg) {
        this.avg = avg;
    }

}
