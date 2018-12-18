/***********************************************************************
 * $Id: CmcUpChannelSignalQualityInfo.java,v1.0 2011-10-26 下午02:31:16 $
 * 
 * @author: xionghao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.upchannel.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.ems.cmc.util.CmcUtil;
import com.topvision.ems.cmc.util.ResourcesUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author xionghao
 * @created @2011-10-26-下午02:31:16
 * 
 */
@Alias("cmcUpChannelSignalQualityInfo")
public class CmcUpChannelSignalQualityInfo implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 8285813848843154734L;
    private Long cmcId;
    private Long cmcPortId;
    private Long cmcIndex;
    private Long cmcChannelId;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long channelIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.4.1.1")
    private Integer docsIfSigQIncludesContention; // 是否存在竞争时隙
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.4.1.2")
    private Long docsIfSigQUnerroreds; // 无纠错Codewords数
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.4.1.3")
    private Long docsIfSigQCorrecteds; // 可纠错Codewords数
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.4.1.4")
    private Long docsIfSigQUncorrectables; // 不可纠错Codewords数
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.4.1.5")
    private Long docsIfSigQSignalNoise; // 信噪比
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.4.1.6")
    private Integer docsIfSigQMicroreflections; // 微反射
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.4.1.7")
    private String docsIfSigQEqualizationData;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.4.1.8")
    private Long docsIfSigQExtUnerroreds; // 64位无纠错Codewords数
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.4.1.9")
    private Long docsIfSigQExtCorrecteds; // 64位可纠错Codewords数
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.4.1.10")
    private Long docsIfSigQExtUncorrectables; // 64位不可纠错Codewords数
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.20.1.25.1.2", writable = true)
    private Integer docsIf3SignalPower;// 接收电平(负130 --正230)
    @SnmpProperty(oid = "1.3.6.1.4.1.4981.2.1.2.1.1", writable = true)
    private Integer rdnCmtsUSNominalRxPower;// BSR接收电平(负130 --正230)
    private String ifDescr;
    private String ifName;
    private String docsIfSigQIncludesContentionName; // 是否存在竞争时隙
    public static final String[] ENABLETYPES = { "", ResourcesUtil.getString("COMMON.yes"),
            ResourcesUtil.getString("COMMON.no") };

    private String docsIfSigQUnerroredsForunit; // 无纠错Codewords数
    private String docsIfSigQCorrectedsForunit; // 可纠错Codewords数
    private String docsIfSigQUncorrectablesForunit; // 不可纠错Codewords数
    private String docsIfSigQSignalNoiseForunit; // 信噪比
    private String docsIfSigQMicroreflectionsForunit; // 微反射
    private String docsIfSigQExtUnerroredsForunit; // 64位无纠错Codewords数
    private String docsIfSigQExtCorrectedsForunit; // 64位可纠错Codewords数
    private String docsIfSigQExtUncorrectablesForunit; // 64位不可纠错Codewords数

    
    public CmcUpChannelSignalQualityInfo(){
    }
    
    
    public CmcUpChannelSignalQualityInfo(Long upChannelIndex, Long cmcIndex){
        this.channelIndex = upChannelIndex;
        this.cmcIndex = cmcIndex;
    }
     
    
    /**
     * @return the cmcIndex
     */
    public Long getCmcIndex() {
        return cmcIndex;
    }


    /**
     * @param cmcIndex the cmcIndex to set
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
     * @return the docsIfSigQIncludesContention
     */
    public Integer getDocsIfSigQIncludesContention() {
        return docsIfSigQIncludesContention;
    }

    /**
     * @param docsIfSigQIncludesContention
     *            the docsIfSigQIncludesContention to set
     */
    public void setDocsIfSigQIncludesContention(Integer docsIfSigQIncludesContention) {
        this.docsIfSigQIncludesContention = docsIfSigQIncludesContention;

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
     * @return the docsIfSigQMicroreflections
     */
    public Integer getDocsIfSigQMicroreflections() {
        return docsIfSigQMicroreflections;
    }

    /**
     * @param docsIfSigQMicroreflections
     *            the docsIfSigQMicroreflections to set
     */
    public void setDocsIfSigQMicroreflections(Integer docsIfSigQMicroreflections) {
        this.docsIfSigQMicroreflections = docsIfSigQMicroreflections;
    }

    /**
     * @return the docsIfSigQEqualizationData
     */
    public String getDocsIfSigQEqualizationData() {
        return docsIfSigQEqualizationData;
    }

    /**
     * @param docsIfSigQEqualizationData
     *            the docsIfSigQEqualizationData to set
     */
    public void setDocsIfSigQEqualizationData(String docsIfSigQEqualizationData) {
        this.docsIfSigQEqualizationData = docsIfSigQEqualizationData;
    }

    /**
     * @return the docsIfSigQExtUnerroreds
     */
    public Long getDocsIfSigQExtUnerroreds() {
        return docsIfSigQExtUnerroreds;
    }

    /**
     * @param docsIfSigQExtUnerroreds
     *            the docsIfSigQExtUnerroreds to set
     */
    public void setDocsIfSigQExtUnerroreds(Long docsIfSigQExtUnerroreds) {
        this.docsIfSigQExtUnerroreds = docsIfSigQExtUnerroreds;
    }

    /**
     * @return the docsIfSigQExtCorrecteds
     */
    public Long getDocsIfSigQExtCorrecteds() {
        return docsIfSigQExtCorrecteds;
    }

    /**
     * @param docsIfSigQExtCorrecteds
     *            the docsIfSigQExtCorrecteds to set
     */
    public void setDocsIfSigQExtCorrecteds(Long docsIfSigQExtCorrecteds) {
        this.docsIfSigQExtCorrecteds = docsIfSigQExtCorrecteds;
    }

    /**
     * @return the docsIfSigQExtUncorrectables
     */
    public Long getDocsIfSigQExtUncorrectables() {
        return docsIfSigQExtUncorrectables;
    }

    /**
     * @param docsIfSigQExtUncorrectables
     *            the docsIfSigQExtUncorrectables to set
     */
    public void setDocsIfSigQExtUncorrectables(Long docsIfSigQExtUncorrectables) {
        this.docsIfSigQExtUncorrectables = docsIfSigQExtUncorrectables;
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
     * @return the docsIfSigQIncludesContentionName
     */
    public String getDocsIfSigQIncludesContentionName() {
        if (this.docsIfSigQIncludesContention != null) {
            this.docsIfSigQIncludesContentionName = ENABLETYPES[docsIfSigQIncludesContention];
        }
        return docsIfSigQIncludesContentionName;
    }

    /**
     * @param docsIfSigQIncludesContentionName
     *            the docsIfSigQIncludesContentionName to set
     */
    public void setDocsIfSigQIncludesContentionName(String docsIfSigQIncludesContentionName) {
        this.docsIfSigQIncludesContentionName = docsIfSigQIncludesContentionName;
    }

    /**
     * @return the docsIfSigQUnerroredsForunit
     */
    public String getDocsIfSigQUnerroredsForunit() {
        Long l = 0l;
        if (this.getDocsIfSigQCorrecteds() != null && this.getDocsIfSigQUncorrectables() != null
                && this.getDocsIfSigQUnerroreds() != null) {
            l = this.getDocsIfSigQCorrecteds() + this.getDocsIfSigQUncorrectables() + this.getDocsIfSigQUnerroreds();
        }
        docsIfSigQUnerroredsForunit = CmcUtil.turnToPercent(this.getDocsIfSigQUnerroreds(), l);
        if (this.getDocsIfSigQUnerroreds() > 1000000) {
            Long sigQuerroreds = this.getDocsIfSigQUnerroreds() / 1000000;
            return sigQuerroreds + "M/" + docsIfSigQUnerroredsForunit;
        }
        return this.getDocsIfSigQUnerroreds() + Symbol.SLASH + docsIfSigQUnerroredsForunit;
    }

    /**
     * @param docsIfSigQUnerroredsForunit
     *            the docsIfSigQUnerroredsForunit to set
     */
    public void setDocsIfSigQUnerroredsForunit(String docsIfSigQUnerroredsForunit) {
        this.docsIfSigQUnerroredsForunit = docsIfSigQUnerroredsForunit;
    }

    /**
     * @return the docsIfSigQCorrectedsForunit
     */
    public String getDocsIfSigQCorrectedsForunit() {
        Long l = 0l;
        if (this.getDocsIfSigQCorrecteds() != null && this.getDocsIfSigQUncorrectables() != null
                && this.getDocsIfSigQUnerroreds() != null) {
            l = this.getDocsIfSigQCorrecteds() + this.getDocsIfSigQUncorrectables() + this.getDocsIfSigQUnerroreds();
        }
        docsIfSigQCorrectedsForunit = CmcUtil.turnToPercent(this.getDocsIfSigQCorrecteds(), l);
        if (this.docsIfSigQCorrecteds > 1000000) {
            Long correcteds = this.docsIfSigQCorrecteds / 1000000;
            return correcteds + "M/" + docsIfSigQCorrectedsForunit;
        }
        return this.getDocsIfSigQCorrecteds() + Symbol.SLASH + docsIfSigQCorrectedsForunit;
    }

    /**
     * @param docsIfSigQCorrectedsForunit
     *            the docsIfSigQCorrectedsForunit to set
     */
    public void setDocsIfSigQCorrectedsForunit(String docsIfSigQCorrectedsForunit) {
        this.docsIfSigQCorrectedsForunit = docsIfSigQCorrectedsForunit;
    }

    /**
     * @return the docsIfSigQUncorrectablesForunit
     */
    public String getDocsIfSigQUncorrectablesForunit() {
        Long l = 0l;
        if (this.getDocsIfSigQCorrecteds() != null && this.getDocsIfSigQUncorrectables() != null
                && this.getDocsIfSigQUnerroreds() != null) {
            l = this.getDocsIfSigQCorrecteds() + this.getDocsIfSigQUncorrectables() + this.getDocsIfSigQUnerroreds();
        }
        docsIfSigQUncorrectablesForunit = CmcUtil.turnToPercent(this.getDocsIfSigQUncorrectables(), l);
        if (this.docsIfSigQUncorrectables > 1000000) {
            Long uncorrectables = docsIfSigQUncorrectables / 1000000;
            return uncorrectables + "M/" + docsIfSigQUncorrectablesForunit;
        }
        return this.getDocsIfSigQUncorrectables() + Symbol.SLASH + docsIfSigQUncorrectablesForunit;
    }

    /**
     * @param docsIfSigQUncorrectablesForunit
     *            the docsIfSigQUncorrectablesForunit to set
     */
    public void setDocsIfSigQUncorrectablesForunit(String docsIfSigQUncorrectablesForunit) {
        this.docsIfSigQUncorrectablesForunit = docsIfSigQUncorrectablesForunit;
    }

    /**
     * @return the docsIfSigQSignalNoiseForunit
     */
    public String getDocsIfSigQSignalNoiseForunit() {
        String str = " dB";
        Float l = 0f;
        if (this.getDocsIfSigQSignalNoise() != null) {
            l = (float) this.getDocsIfSigQSignalNoise() / 10;
        }
        docsIfSigQSignalNoiseForunit = l.toString();
        return docsIfSigQSignalNoiseForunit + str;
    }

    /**
     * @param docsIfSigQSignalNoiseForunit
     *            the docsIfSigQSignalNoiseForunit to set
     */
    public void setDocsIfSigQSignalNoiseForunit(String docsIfSigQSignalNoiseForunit) {
        this.docsIfSigQSignalNoiseForunit = docsIfSigQSignalNoiseForunit;
    }

    /**
     * @return the docsIfSigQExtUnerroredsForunit
     */
    public String getDocsIfSigQExtUnerroredsForunit() {
        Long l = 0l;
        if (this.getDocsIfSigQExtCorrecteds() != null && this.getDocsIfSigQExtUncorrectables() != null
                && this.getDocsIfSigQExtUnerroreds() != null) {
            l = this.getDocsIfSigQExtCorrecteds() + this.getDocsIfSigQExtUncorrectables()
                    + this.getDocsIfSigQExtUnerroreds();
        }
        docsIfSigQExtUnerroredsForunit = CmcUtil.turnToPercent(this.getDocsIfSigQExtUnerroreds(), l);
        if (this.getDocsIfSigQUnerroreds() > 1000000) {
            Long sigQuerroreds = this.getDocsIfSigQUnerroreds() / 1000000;
            return sigQuerroreds + "M (" + docsIfSigQUnerroredsForunit + Symbol.PARENTHESIS_RIGHT;
        }
        return this.getDocsIfSigQExtUnerroreds() + Symbol.PARENTHESIS_LEFT + docsIfSigQExtUnerroredsForunit
                + Symbol.PARENTHESIS_RIGHT;
    }

    /**
     * @param docsIfSigQExtUnerroredsForunit
     *            the docsIfSigQExtUnerroredsForunit to set
     */
    public void setDocsIfSigQExtUnerroredsForunit(String docsIfSigQExtUnerroredsForunit) {
        this.docsIfSigQExtUnerroredsForunit = docsIfSigQExtUnerroredsForunit;
    }

    /**
     * @return the docsIfSigQExtCorrectedsForunit
     */
    public String getDocsIfSigQExtCorrectedsForunit() {
        Long l = 0l;
        if (this.getDocsIfSigQExtCorrecteds() != null && this.getDocsIfSigQExtUncorrectables() != null
                && this.getDocsIfSigQExtUnerroreds() != null) {
            l = this.getDocsIfSigQExtCorrecteds() + this.getDocsIfSigQExtUncorrectables()
                    + this.getDocsIfSigQExtUnerroreds();
        }
        docsIfSigQExtCorrectedsForunit = CmcUtil.turnToPercent(this.getDocsIfSigQExtCorrecteds(), l);
        return this.getDocsIfSigQExtCorrecteds() + Symbol.PARENTHESIS_LEFT + docsIfSigQExtCorrectedsForunit
                + Symbol.PARENTHESIS_RIGHT;
    }

    /**
     * @param docsIfSigQExtCorrectedsForunit
     *            the docsIfSigQExtCorrectedsForunit to set
     */
    public void setDocsIfSigQExtCorrectedsForunit(String docsIfSigQExtCorrectedsForunit) {
        this.docsIfSigQExtCorrectedsForunit = docsIfSigQExtCorrectedsForunit;
    }

    /**
     * @return the docsIfSigQExtUncorrectablesForunit
     */
    public String getDocsIfSigQExtUncorrectablesForunit() {
        Long l = 0l;
        if (this.getDocsIfSigQExtCorrecteds() != null && this.getDocsIfSigQExtUncorrectables() != null
                && this.getDocsIfSigQExtUnerroreds() != null) {
            l = this.getDocsIfSigQExtCorrecteds() + this.getDocsIfSigQExtUncorrectables()
                    + this.getDocsIfSigQExtUnerroreds();
        }
        docsIfSigQExtUncorrectablesForunit = CmcUtil.turnToPercent(this.getDocsIfSigQExtUncorrectables(), l);
        return this.getDocsIfSigQExtUncorrectables() + Symbol.PARENTHESIS_LEFT + docsIfSigQExtUncorrectablesForunit
                + Symbol.PARENTHESIS_RIGHT;
    }

    /**
     * @param docsIfSigQExtUncorrectablesForunit
     *            the docsIfSigQExtUncorrectablesForunit to set
     */
    public void setDocsIfSigQExtUncorrectablesForunit(String docsIfSigQExtUncorrectablesForunit) {
        this.docsIfSigQExtUncorrectablesForunit = docsIfSigQExtUncorrectablesForunit;
    }

    /**
     * @return the docsIfSigQMicroreflectionsForunit
     */
    public String getDocsIfSigQMicroreflectionsForunit() {
        String str = " -dBc";
        if (this.getDocsIfSigQMicroreflections() != null) {
            docsIfSigQMicroreflectionsForunit = this.getDocsIfSigQMicroreflections().toString();
        }
        return docsIfSigQMicroreflectionsForunit + str;
    }

    /**
     * @param docsIfSigQMicroreflectionsForunit
     *            the docsIfSigQMicroreflectionsForunit to set
     */
    public void setDocsIfSigQMicroreflectionsForunit(String docsIfSigQMicroreflectionsForunit) {
        this.docsIfSigQMicroreflectionsForunit = docsIfSigQMicroreflectionsForunit;
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

    public void setDocsIf3SignalPower(Integer docsIf3SignalPower) {
        this.docsIf3SignalPower = docsIf3SignalPower;
    }

    public Integer getDocsIf3SignalPower() {
        if (docsIf3SignalPower == null) {
            if (rdnCmtsUSNominalRxPower == null) {
                return 0;
            } else {
                return rdnCmtsUSNominalRxPower;
            }
        } else {
            return docsIf3SignalPower;
        }
    }

    public String getIfDescr() {
        return ifDescr;
    }

    public void setIfDescr(String ifDescr) {
        this.ifDescr = ifDescr;
    }

    public Integer getRdnCmtsUSNominalRxPower() {
        return rdnCmtsUSNominalRxPower;
    }

    public void setRdnCmtsUSNominalRxPower(Integer rdnCmtsUSNominalRxPower) {
        this.rdnCmtsUSNominalRxPower = rdnCmtsUSNominalRxPower;
    }

    public String getIfName() {
        return ifName;
    }


    public void setIfName(String ifName) {
        this.ifName = ifName;
    }


    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcUpChannelSignalQualityInfo [cmcId=");
        builder.append(cmcId);
        builder.append(", cmcPortId=");
        builder.append(cmcPortId);
        builder.append(", channelIndex=");
        builder.append(channelIndex);
        builder.append(", docsIfSigQIncludesContention=");
        builder.append(docsIfSigQIncludesContention);
        builder.append(", docsIfSigQUnerroreds=");
        builder.append(docsIfSigQUnerroreds);
        builder.append(", docsIfSigQCorrecteds=");
        builder.append(docsIfSigQCorrecteds);
        builder.append(", docsIfSigQUncorrectables=");
        builder.append(docsIfSigQUncorrectables);
        builder.append(", docsIfSigQSignalNoise=");
        builder.append(docsIfSigQSignalNoise);
        builder.append(", docsIfSigQMicroreflections=");
        builder.append(docsIfSigQMicroreflections);
        builder.append(", docsIfSigQEqualizationData=");
        builder.append(docsIfSigQEqualizationData);
        builder.append(", docsIfSigQExtUnerroreds=");
        builder.append(docsIfSigQExtUnerroreds);
        builder.append(", docsIfSigQExtCorrecteds=");
        builder.append(docsIfSigQExtCorrecteds);
        builder.append(", docsIfSigQExtUncorrectables=");
        builder.append(docsIfSigQExtUncorrectables);
        builder.append(", docsIfSigQIncludesContentionName=");
        builder.append(docsIfSigQIncludesContentionName);
        builder.append(", docsIfSigQUnerroredsForunit=");
        builder.append(docsIfSigQUnerroredsForunit);
        builder.append(", docsIfSigQCorrectedsForunit=");
        builder.append(docsIfSigQCorrectedsForunit);
        builder.append(", docsIfSigQUncorrectablesForunit=");
        builder.append(docsIfSigQUncorrectablesForunit);
        builder.append(", docsIfSigQSignalNoiseForunit=");
        builder.append(docsIfSigQSignalNoiseForunit);
        builder.append(", docsIfSigQMicroreflectionsForunit=");
        builder.append(docsIfSigQMicroreflectionsForunit);
        builder.append(", docsIfSigQExtUnerroredsForunit=");
        builder.append(docsIfSigQExtUnerroredsForunit);
        builder.append(", docsIfSigQExtCorrectedsForunit=");
        builder.append(docsIfSigQExtCorrectedsForunit);
        builder.append(", docsIfSigQExtUncorrectablesForunit=");
        builder.append(docsIfSigQExtUncorrectablesForunit);
        builder.append(", ifDescr=");
        builder.append(ifDescr);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

}
