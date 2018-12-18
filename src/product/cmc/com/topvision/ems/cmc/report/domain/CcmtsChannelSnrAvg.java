/***********************************************************************
 * $Id: CcmtsChannelSnrAvg.java,v1.0 2013-6-8 上午10:51:47 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.domain;

import java.text.NumberFormat;

import com.topvision.framework.utils.CmcIndexUtils;

/**
 * @author Bravin
 * @created @2013-6-8-上午10:51:47
 * 
 */
public class CcmtsChannelSnrAvg {
    private Long cmcId;
    private Long ifIndex;
    private Integer ifNo;
    private Double snrAvg;
    private String snrDisplay;
    private static final NumberFormat formater = NumberFormat.getInstance();
    static {
        // 设置小数点后面最多2位
        formater.setMaximumFractionDigits(2);
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
     * @return the ifIndex
     */
    public Long getIfIndex() {
        return ifIndex;
    }

    /**
     * @param ifIndex
     *            the ifIndex to set
     */
    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    /**
     * @return the ifNo
     */
    public Integer getIfNo() {
        return CmcIndexUtils.getChannelId(this.ifIndex).intValue();
    }

    /**
     * @param ifNo
     *            the ifNo to set
     */
    public void setIfNo(Integer ifNo) {
        this.ifNo = ifNo;
    }

    /**
     * @return the snrAvg
     */
    public Double getSnrAvg() {
        return snrAvg;
    }

    /**
     * @param snrAvg
     *            the snrAvg to set
     */
    public void setSnrAvg(Double snrAvg) {
        if (snrAvg == null) {
            this.snrAvg = 0.0;
            this.snrDisplay = "-";
        } else {
            this.snrAvg = snrAvg / 10;
            this.snrDisplay = formater.format(this.snrAvg);
        }
    }

    /**
     * @return the snrDisplay
     */
    public String getSnrDisplay() {
        return snrDisplay;
    }

    /**
     * @param snrDisplay
     *            the snrDisplay to set
     */
    public void setSnrDisplay(String snrDisplay) {
        this.snrDisplay = snrDisplay;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CcmtsChannelSnrAvg [cmcId=");
        builder.append(cmcId);
        builder.append(", ifIndex=");
        builder.append(ifIndex);
        builder.append(", ifNo=");
        builder.append(ifNo);
        builder.append(", snrAvg=");
        builder.append(snrAvg);
        builder.append(", snrDisplay=");
        builder.append(snrDisplay);
        builder.append("]");
        return builder.toString();
    }

}
