/***********************************************************************
 * $Id: CmcUpChannelSignalQualityInfo.java,v1.0 2011-10-26 下午02:31:16 $
 * 
 * @author: xionghao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author xionghao
 * @created @2011-10-26-下午02:31:16
 *
 */
@Alias("usBitErrorRatePerfDomain")
public class UsBitErrorRatePerfDomain implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 8285813848843154734L;
    private Long cmcId;
    private Long cmcPortId;
    private Long cmcChannelId;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long channelIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.4.1.2")
    private Long docsIfSigQUnerroreds; // 无纠错Codewords数
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.4.1.3")
    private Long docsIfSigQCorrecteds; // 可纠错Codewords数
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.4.1.4")
    private Long docsIfSigQUncorrectables; // 不可纠错Codewords数

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
     * @return the cmcChannelId
     */
    public Long getCmcChannelId() {
        return cmcChannelId;
    }

    /**
     * @param cmcChannelId
     *            the cmcChannelId to set
     */
    public void setCmcChannelId(Long cmcChannelId) {
        this.cmcChannelId = cmcChannelId;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("UsBitErrorRatePerfDomain");
        sb.append("{channelIndex=").append(channelIndex);
        sb.append(", cmcId=").append(cmcId);
        sb.append(", cmcPortId=").append(cmcPortId);
        sb.append(", cmcChannelId=").append(cmcChannelId);
        sb.append(", docsIfSigQUnerroreds=").append(docsIfSigQUnerroreds);
        sb.append(", docsIfSigQCorrecteds=").append(docsIfSigQCorrecteds);
        sb.append(", docsIfSigQUncorrectables=").append(docsIfSigQUncorrectables);
        sb.append('}');
        return sb.toString();
    }

}