/***********************************************************************
 * $Id: SnrCurrentPerf.java,v1.0 2012-5-7 下午07:12:26 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

import com.topvision.framework.constants.Symbol;

/**
 * @author dosion
 * @created @2012-5-7-下午07:12:26
 * 
 */
public class SnrCurrentPerf {
    private Long cmcId;
    private Long cmcPortId;
    private Integer maxCurrentPerf;
    private Integer minCurrentPerf;
    private Integer currentPerf;

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
     * @return the maxCurrentPerf
     */
    public Integer getMaxCurrentPerf() {
        return maxCurrentPerf;
    }

    /**
     * @param maxCurrentPerf
     *            the maxCurrentPerf to set
     */
    public void setMaxCurrentPerf(Integer maxCurrentPerf) {
        this.maxCurrentPerf = maxCurrentPerf;
    }

    /**
     * @return the minCurrentPerf
     */
    public Integer getMinCurrentPerf() {
        return minCurrentPerf;
    }

    /**
     * @param minCurrentPerf
     *            the minCurrentPerf to set
     */
    public void setMinCurrentPerf(Integer minCurrentPerf) {
        this.minCurrentPerf = minCurrentPerf;
    }

    /**
     * @return the currentPerf
     */
    public Integer getCurrentPerf() {
        return currentPerf;
    }

    /**
     * @param currentPerf
     *            the currentPerf to set
     */
    public void setCurrentPerf(Integer currentPerf) {
        this.currentPerf = currentPerf;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SnrCurrentPerf [cmcId=");
        builder.append(cmcId);
        builder.append(", cmcPortId=");
        builder.append(cmcPortId);
        builder.append(", maxCurrentPerf=");
        builder.append(maxCurrentPerf);
        builder.append(", minCurrentPerf=");
        builder.append(minCurrentPerf);
        builder.append(", currentPerf=");
        builder.append(currentPerf);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
