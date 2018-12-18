package com.topvision.ems.cmc.domain;

import java.io.Serializable;

import com.topvision.framework.constants.Symbol;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.ems.cmc.util.CmcUtil;

/**
 * @author bryan
 * @created @2013-10-15-下午04:33:30
 *
 */
public class CmcUsSignalQualityInfo implements Serializable {
    private static final long serialVersionUID = 8285813848843154734L;
    private Long cmcId;
    private Long cmcPortId;
    private Long cmcChannelId;
    private Long channelIndex;
    private Integer ifAdminStatus;
    
    private Long sigQCorrecteds; // 可纠错Codewords数
    private Long sigQUncorrectables; // 不可纠错Codewords数
    private Long sigQSignalNoise; // 信噪比

    private String sigQCorrectedsForunit; // 可纠错Codewords数
    private String sigQUncorrectablesForunit; // 不可纠错Codewords数
    private String sigQSignalNoiseForunit; // 信噪比
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
        if (channelIndex!=null) {
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
     * @param ifAdminStatus the ifAdminStatus to set
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
     * @param sigQCorrecteds the sigQCorrecteds to set
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
     * @param sigQUncorrectables the sigQUncorrectables to set
     */
    public void setSigQUncorrectables(Long sigQUncorrectables) {
        this.sigQUncorrectables = sigQUncorrectables;
    }

    /**
     * @return the sigQSignalNoise
     */
    public Long getSigQSignalNoise() {
        return sigQSignalNoise;
    }

    /**
     * @param sigQSignalNoise the sigQSignalNoise to set
     */
    public void setSigQSignalNoise(Long sigQSignalNoise) {
        this.sigQSignalNoise = sigQSignalNoise;
    }

    /**
     * @return the sigQCorrectedsForunit
     */
    public String getSigQCorrectedsForunit() {
        Long l = 0l;
        if (this.getSigQUncorrectables()!= null && this.getSigQCorrecteds() != null) {
            l = this.getSigQUncorrectables()+ this.getSigQCorrecteds();
        }
        sigQCorrectedsForunit = CmcUtil.turnToPercent(this.getSigQCorrecteds(), l);
        if(this.sigQCorrecteds > 1000000){
            Long correcteds = this.sigQCorrecteds / 1000000;
            return correcteds + "M/" + sigQCorrectedsForunit;
        }
        return this.getSigQCorrecteds() + Symbol.SLASH + sigQCorrectedsForunit;
    }

    /**
     * @param sigQCorrectedsForunit the sigQCorrectedsForunit to set
     */
    public void setSigQCorrectedsForunit(String sigQCorrectedsForunit) {
        this.sigQCorrectedsForunit = sigQCorrectedsForunit;
    }

    /**
     * @return the sigQUncorrectablesForunit
     */
    public String getSigQUncorrectablesForunit() {
        Long l = 0l;
        if (this.getSigQUncorrectables()!= null && this.getSigQCorrecteds() != null) {
            l = this.getSigQUncorrectables()+ this.getSigQCorrecteds();
        }
        sigQUncorrectablesForunit = CmcUtil.turnToPercent(this.getSigQUncorrectables(), l);
        if(this.sigQCorrecteds > 1000000){
            Long correcteds = this.sigQUncorrectables / 1000000;
            return correcteds + "M/" + sigQUncorrectablesForunit;
        }
        return this.getSigQUncorrectables() + Symbol.SLASH + sigQUncorrectablesForunit;
    }

    /**
     * @param sigQUncorrectablesForunit the sigQUncorrectablesForunit to set
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
            sigQSignalNoiseForunit = l.toString() + str;
        }else{
            sigQSignalNoiseForunit = "-";
        }
        return sigQSignalNoiseForunit;
    }

    /**
     * @param sigQSignalNoiseForunit the sigQSignalNoiseForunit to set
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

   


}
