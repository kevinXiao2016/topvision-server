package com.topvision.ems.cmc.upchannel.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.ems.cmc.util.CmcUtil;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author bryan
 * @created @2013-10-15-下午04:33:30
 * 
 */
@Alias("usSignalInfo")
public class CmcUsSignalQualityInfo implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 8285813848843154734L;
    private Long cmcId;
    private Long cmcPortId;
    private Long cmcChannelId;
    private Long channelIndex;
    private Integer ifAdminStatus;
    private String ifDescr;
    private String ifName;

    private Long sigQCorrecteds; // 可纠错Codewords数
    private String sigQCorrectedsForunit; // 可纠错Codewords数
    private Long sigQUncorrectables; // 不可纠错Codewords数
    private String sigQUncorrectablesForunit; // 不可纠错Codewords数
    private Long sigQUnerroreds;// 无错码数
    private String sigQUnerroredsForunit;// 无错码数
    private String noerRate;// 无错码率

    private Long sigQSignalNoise; // 信噪比
    private String sigQSignalNoiseForunit; // 信噪比
    private String ccerRate;
    private String ucerRate;

    private boolean isAllCenterNull() {
        return (this.getSigQCorrecteds() == null || this.getSigQCorrecteds() == 0)
                && (this.getSigQUnerroreds() == null || this.getSigQUnerroreds() == 0)
                && (this.getSigQUncorrectables() == null || this.getSigQUncorrectables() == 0);
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
        if (channelIndex != null) {
            cmcChannelId = CmcIndexUtils.getChannelId(channelIndex);
        }
        return cmcChannelId;
    }

    /**
     * @return the ifAdminStatus
     */
    public Integer getIfAdminStatus() {
        return ifAdminStatus;
    }

    /**
     * @param ifAdminStatus
     *            the ifAdminStatus to set
     */
    public void setIfAdminStatus(Integer ifAdminStatus) {
        this.ifAdminStatus = ifAdminStatus;
    }

    /**
     * @param cmcChannelId
     *            the cmcChannelId to set
     */
    public void setCmcChannelId(Long cmcChannelId) {
        this.cmcChannelId = cmcChannelId;
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
        if (sigQCorrecteds < 0) {
            this.sigQCorrecteds = 0L;
        } else {
            this.sigQCorrecteds = sigQCorrecteds;
        }
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
        if (sigQUncorrectables < 0) {
            this.sigQUncorrectables = 0L;
        } else {
            this.sigQUncorrectables = sigQUncorrectables;
        }
    }

    /**
     * @return the sigQSignalNoise
     */
    public Long getSigQSignalNoise() {
        return sigQSignalNoise;
    }

    /**
     * @param sigQSignalNoise
     *            the sigQSignalNoise to set
     */
    public void setSigQSignalNoise(Long sigQSignalNoise) {
        this.sigQSignalNoise = sigQSignalNoise;
    }

    /**
     * @return the sigQCorrectedsForunit
     */
    public String getSigQCorrectedsForunit() {
        if (this.isAllCenterNull()) {
            return "--";
        }
        Long l = 0l;
        if (this.getSigQUncorrectables() != null && this.getSigQCorrecteds() != null
                && this.getSigQUnerroreds() != null) {
            l = this.getSigQUncorrectables() + this.getSigQCorrecteds() + this.getSigQUnerroreds();
        }
        sigQCorrectedsForunit = CmcUtil.turnToPercent(this.getSigQCorrecteds(), l);
        if (this.sigQCorrecteds > 1000000) {
            Long correcteds = this.sigQCorrecteds / 1000000;
            return correcteds + "M/" + sigQCorrectedsForunit;
        }
        return this.getSigQCorrecteds() + Symbol.SLASH + sigQCorrectedsForunit;
    }

    public String getSigQUnerroredsForunit() {
        if (this.isAllCenterNull()) {
            return "--";
        }
        Long l = 0l;
        if (this.getSigQUncorrectables() != null && this.getSigQCorrecteds() != null
                && this.getSigQUnerroreds() != null) {
            l = this.getSigQUncorrectables() + this.getSigQCorrecteds() + this.getSigQUnerroreds();
        }
        sigQUnerroredsForunit = CmcUtil.turnToPercent(this.getSigQUnerroreds(), l);
        if (this.sigQUnerroreds > 1000000) {
            Long unerrors = this.sigQUnerroreds / 100000;
            return unerrors + "M/" + sigQUnerroredsForunit;
        }
        return this.getSigQUnerroreds() + Symbol.SLASH + sigQUnerroredsForunit;
    }

    /**
     * @return the sigQUncorrectablesForunit
     */
    public String getSigQUncorrectablesForunit() {
        if (this.isAllCenterNull()) {
            return "--";
        }
        Long l = 0l;
        if (this.getSigQUncorrectables() != null && this.getSigQCorrecteds() != null
                && this.getSigQUnerroreds() != null) {
            l = this.getSigQUncorrectables() + this.getSigQCorrecteds() + this.getSigQUnerroreds();
        }
        sigQUncorrectablesForunit = CmcUtil.turnToPercent(this.getSigQUncorrectables(), l);
        if (this.sigQCorrecteds > 1000000) {
            Long correcteds = this.sigQUncorrectables / 1000000;
            return correcteds + "M/" + sigQUncorrectablesForunit;
        }
        return this.getSigQUncorrectables() + Symbol.SLASH + sigQUncorrectablesForunit;
    }

    /**
     * @param sigQCorrectedsForunit
     *            the sigQCorrectedsForunit to set
     */
    public void setSigQCorrectedsForunit(String sigQCorrectedsForunit) {
        this.sigQCorrectedsForunit = sigQCorrectedsForunit;
    }

    /**
     * @param sigQUncorrectablesForunit
     *            the sigQUncorrectablesForunit to set
     */
    public void setSigQUncorrectablesForunit(String sigQUncorrectablesForunit) {
        this.sigQUncorrectablesForunit = sigQUncorrectablesForunit;
    }

    /**
     * @return the sigQSignalNoiseForunit
     */
    public String getSigQSignalNoiseForunit() {
        String str = " dB";
        Float l = 0f;
        if (this.getSigQSignalNoise() != null) {
            l = (float) this.getSigQSignalNoise() / 10;
        }
        sigQSignalNoiseForunit = l.toString();
        return sigQSignalNoiseForunit + str;
    }

    /**
     * @param sigQSignalNoiseForunit
     *            the sigQSignalNoiseForunit to set
     */
    public void setSigQSignalNoiseForunit(String sigQSignalNoiseForunit) {
        this.sigQSignalNoiseForunit = sigQSignalNoiseForunit;
    }

    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
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

    public String getCcerRate() {
        return ccerRate;
    }

    public void setCcerRate(String ccerRate) {
        this.ccerRate = ccerRate;
    }

    public String getUcerRate() {
        return ucerRate;
    }

    public void setUcerRate(String ucerRate) {
        this.ucerRate = ucerRate;
    }

    public String getIfDescr() {
        return ifDescr;
    }

    public void setIfDescr(String ifDescr) {
        this.ifDescr = ifDescr;
    }

    public Long getSigQUnerroreds() {
        return sigQUnerroreds;
    }

    public String getNoerRate() {
        return noerRate;
    }

    public void setSigQUnerroreds(Long sigQUnerroreds) {
        if (sigQUnerroreds < 0) {
            this.sigQUnerroreds = 0L;
        } else {
            this.sigQUnerroreds = sigQUnerroreds;
        }
    }

    public void setSigQUnerroredsForunit(String sigQUnerroredsForunit) {
        this.sigQUnerroredsForunit = sigQUnerroredsForunit;
    }

    public void setNoerRate(String noerRate) {
        this.noerRate = noerRate;
    }

    public String getIfName() {
        return ifName;
    }

    public void setIfName(String ifName) {
        this.ifName = ifName;
    }

}
