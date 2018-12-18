/***********************************************************************
 * $Id: CmcSingleQuality.java,v1.0 2013-8-10 下午01:28:10 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.facade;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topvision.ems.facade.nbi.NbiSnmpProperty;
import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author Rod John
 * @created @2013-8-10-下午01:28:10
 * 
 */
public class CmcSignalQuality implements Serializable {
    private static final long serialVersionUID = -5064475306266077796L;
    public static Integer CMC_SCHEDULER = 1;
    public static Integer CMTS_SCHEDULER = 2;
    private Long cmcId;
    private Long cmcIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long cmcChannelIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.4.1.2")
    private Long docsIfSigQUnerroreds; // 无纠错Codewords数
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.4.1.3")
    private Long docsIfSigQCorrecteds; // 可纠错Codewords数
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.4.1.4")
    private Long docsIfSigQUncorrectables; // 不可纠错Codewords数
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.4.1.5")
    private Long docsIfSigQSignalNoise; // 信噪比
    @NbiSnmpProperty(oid = "1.3.6.1.4.1.32285.12.2.3.9.7.1.3")
    private Float noise;
    private Long sigQUnerroreds; // 无错码数
    private Float sigQUnerroredRate; // 无错码率
    private Long sigQCorrecteds; // 可纠错码数
    @NbiSnmpProperty(oid = "1.3.6.1.4.1.32285.12.2.3.9.7.1.1")
    private Float sigQCorrectedRate; // 可纠错码率
    private Long sigQUncorrectables; // 不可纠错码数
    @NbiSnmpProperty(oid = "1.3.6.1.4.1.32285.12.2.3.9.7.1.2")
    private Float sigQUncorrectedRate; // 不可纠错吗率
    private Integer schedulerType = CMC_SCHEDULER;
    private Timestamp collectTime;
    private Long totalCodeNum;

    public Float getSigQUncorrectedRate() {
        return sigQUncorrectedRate;
    }

    public void setSigQUncorrectedRate(Float sigQUncorrectedRate) {
        this.sigQUncorrectedRate = sigQUncorrectedRate;
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
     * @return the cmcChannelIndex
     */
    public Long getCmcChannelIndex() {
        return cmcChannelIndex;
    }

    /**
     * @param cmcChannelIndex
     *            the cmcChannelIndex to set
     */
    public void setCmcChannelIndex(Long cmcChannelIndex) {
        this.cmcChannelIndex = cmcChannelIndex;
    }

    /**
     * @return the docsIfSigQUnerroreds
     */
    public Long getDocsIfSigQUnerroreds() {
        return docsIfSigQUnerroreds;
    }

    /**
     * @param docsIfSigQUnerroreds
     *            the docsIfSigQUnerroreds to set
     */
    public void setDocsIfSigQUnerroreds(Long docsIfSigQUnerroreds) {
        this.docsIfSigQUnerroreds = docsIfSigQUnerroreds;
    }

    /**
     * @return the docsIfSigQCorrecteds
     */
    public Long getDocsIfSigQCorrecteds() {
        return docsIfSigQCorrecteds;
    }

    /**
     * @param docsIfSigQCorrecteds
     *            the docsIfSigQCorrecteds to set
     */
    public void setDocsIfSigQCorrecteds(Long docsIfSigQCorrecteds) {
        this.docsIfSigQCorrecteds = docsIfSigQCorrecteds;
    }

    /**
     * @return the docsIfSigQUncorrectables
     */
    public Long getDocsIfSigQUncorrectables() {
        return docsIfSigQUncorrectables;
    }

    /**
     * @param docsIfSigQUncorrectables
     *            the docsIfSigQUncorrectables to set
     */
    public void setDocsIfSigQUncorrectables(Long docsIfSigQUncorrectables) {
        this.docsIfSigQUncorrectables = docsIfSigQUncorrectables;
    }

    /**
     * @return the docsIfSigQSignalNoise
     */
    public Long getDocsIfSigQSignalNoise() {
        return docsIfSigQSignalNoise;
    }

    /**
     * @param docsIfSigQSignalNoise
     *            the docsIfSigQSignalNoise to set
     */
    public void setDocsIfSigQSignalNoise(Long docsIfSigQSignalNoise) {
        this.docsIfSigQSignalNoise = docsIfSigQSignalNoise;
    }

    /**
     * @return the collectTime
     */
    public Timestamp getCollectTime() {
        return collectTime;
    }

    /**
     * @param collectTime
     *            the collectTime to set
     */
    public void setCollectTime(Timestamp collectTime) {
        this.collectTime = collectTime;
    }

    /**
     * @return the sigQUnerroreds
     */
    public Long getSigQUnerroreds() {
        return sigQUnerroreds;
    }

    /**
     * @param sigQUnerroreds
     *            the sigQUnerroreds to set
     */
    public void setSigQUnerroreds(Long sigQUnerroreds) {
        this.sigQUnerroreds = sigQUnerroreds;
    }

    /**
     * @return the sigQCorrecteds
     */
    public Long getSigQCorrecteds() {
        return sigQCorrecteds;
    }

    /**
     * @param sigQCorrecteds
     *            the sigQCorrecteds to set
     */
    public void setSigQCorrecteds(Long sigQCorrecteds) {
        this.sigQCorrecteds = sigQCorrecteds;
    }

    /**
     * @return the sigQUncorrectables
     */
    public Long getSigQUncorrectables() {
        return sigQUncorrectables;
    }

    /**
     * @param sigQUncorrectables
     *            the sigQUncorrectables to set
     */
    public void setSigQUncorrectables(Long sigQUncorrectables) {
        this.sigQUncorrectables = sigQUncorrectables;
    }

    public Float getNoise() {
        return noise;
    }

    public void setNoise(Float noise) {
        this.noise = noise;
    }

    /**
     * @return the sigQUnerroredRate
     */
    public Float getSigQUnerroredRate() {
        return sigQUnerroredRate;
    }

    /**
     * @param sigQUnerroredRate
     *            the sigQUnerroredRate to set
     */
    public void setSigQUnerroredRate(Float sigQUnerroredRate) {
        this.sigQUnerroredRate = sigQUnerroredRate;
    }

    /**
     * @return the sigQCorrectedRate
     */
    public Float getSigQCorrectedRate() {
        return sigQCorrectedRate;
    }

    /**
     * @param sigQCorrectedRate
     *            the sigQCorrectedRate to set
     */
    public void setSigQCorrectedRate(Float sigQCorrectedRate) {
        this.sigQCorrectedRate = sigQCorrectedRate;
    }

    /**
     * @return the schedulerType
     */
    public Integer getSchedulerType() {
        return schedulerType;
    }

    /**
     * @param schedulerType
     *            the schedulerType to set
     */
    public void setSchedulerType(Integer schedulerType) {
        this.schedulerType = schedulerType;
    }

    public Long getTotalCodeNum() {
        return totalCodeNum;
    }

    public void setTotalCodeNum(Long totalCodeNum) {
        this.totalCodeNum = totalCodeNum;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcSingleQuality [cmcId=");
        builder.append(cmcId);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", cmcChannelIndex=");
        builder.append(cmcChannelIndex);
        builder.append(", docsIfSigQUnerroreds=");
        builder.append(docsIfSigQUnerroreds);
        builder.append(", docsIfSigQCorrecteds=");
        builder.append(docsIfSigQCorrecteds);
        builder.append(", docsIfSigQUncorrectables=");
        builder.append(docsIfSigQUncorrectables);
        builder.append(", docsIfSigQSignalNoise=");
        builder.append(docsIfSigQSignalNoise);
        builder.append(", collectTime=");
        builder.append(collectTime);
        builder.append("]");
        return builder.toString();
    }

}
