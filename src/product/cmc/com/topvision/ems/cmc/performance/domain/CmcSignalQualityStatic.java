/***********************************************************************
 * $Id: CmcSignalQualityStatic.java,v1.0 2013-12-3 上午10:29:35 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author Rod John
 * @created @2013-12-3-上午10:29:35
 * 
 */
public class CmcSignalQualityStatic implements AliasesSuperType {
    private static final long serialVersionUID = 4471018352320188495L;
    public static final String SNR = "snr";
    public static final String CCER = "ccer";
    public static final String UCER = "ucer";
    private Long cmcId;
    private Long cmcIndex;
    private Long channelIndex;
    private Float snrValue;
    private Float ccerValue;
    private Float ucerValue;
    private String snrCollectTime;
    private String ccerCollectTime;
    private String ucerCollectTime;

    public void setSnr(CmcPerfCommon perfCommon) {
        if (SNR.equals(perfCommon.getTargetName())) {
            this.snrValue = perfCommon.getTargetValue();
            this.snrCollectTime = perfCommon.getCollectTime();
        }
    }

    public void setMem(CmcPerfCommon perfCommon) {
        if (CCER.equals(perfCommon.getTargetName())) {
            this.ccerValue = perfCommon.getTargetValue();
            this.ccerCollectTime = perfCommon.getCollectTime();
        }
    }

    public void setFlash(CmcPerfCommon perfCommon) {
        if (UCER.equals(perfCommon.getTargetName())) {
            this.ucerValue = perfCommon.getTargetValue();
            this.ucerCollectTime = perfCommon.getCollectTime();
        }
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
     * @return the snrValue
     */
    public Float getSnrValue() {
        return snrValue;
    }

    /**
     * @param snrValue
     *            the snrValue to set
     */
    public void setSnrValue(Float snrValue) {
        this.snrValue = snrValue;
    }

    /**
     * @return the ccerValue
     */
    public Float getCcerValue() {
        return ccerValue;
    }

    /**
     * @param ccerValue
     *            the ccerValue to set
     */
    public void setCcerValue(Float ccerValue) {
        this.ccerValue = ccerValue;
    }

    /**
     * @return the ucerValue
     */
    public Float getUcerValue() {
        return ucerValue;
    }

    /**
     * @param ucerValue
     *            the ucerValue to set
     */
    public void setUcerValue(Float ucerValue) {
        this.ucerValue = ucerValue;
    }

    /**
     * @return the snrCollectTime
     */
    public String getSnrCollectTime() {
        return snrCollectTime;
    }

    /**
     * @param snrCollectTime
     *            the snrCollectTime to set
     */
    public void setSnrCollectTime(String snrCollectTime) {
        this.snrCollectTime = snrCollectTime;
    }

    /**
     * @return the ccerCollectTime
     */
    public String getCcerCollectTime() {
        return ccerCollectTime;
    }

    /**
     * @param ccerCollectTime
     *            the ccerCollectTime to set
     */
    public void setCcerCollectTime(String ccerCollectTime) {
        this.ccerCollectTime = ccerCollectTime;
    }

    /**
     * @return the ucerCollectTime
     */
    public String getUcerCollectTime() {
        return ucerCollectTime;
    }

    /**
     * @param ucerCollectTime
     *            the ucerCollectTime to set
     */
    public void setUcerCollectTime(String ucerCollectTime) {
        this.ucerCollectTime = ucerCollectTime;
    }

    /**
     * @return the channelIndex
     */
    public Long getChannelIndex() {
        return channelIndex;
    }

    /**
     * @param channelIndex
     *            the channelIndex to set
     */
    public void setChannelIndex(Long channelIndex) {
        this.channelIndex = channelIndex;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcSignalQualityStatic [cmcId=");
        builder.append(cmcId);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", channelIndex=");
        builder.append(channelIndex);
        builder.append(", snrValue=");
        builder.append(snrValue);
        builder.append(", ccerValue=");
        builder.append(ccerValue);
        builder.append(", ucerValue=");
        builder.append(ucerValue);
        builder.append(", snrCollectTime=");
        builder.append(snrCollectTime);
        builder.append(", ccerCollectTime=");
        builder.append(ccerCollectTime);
        builder.append(", ucerCollectTime=");
        builder.append(ucerCollectTime);
        builder.append("]");
        return builder.toString();
    }

}
