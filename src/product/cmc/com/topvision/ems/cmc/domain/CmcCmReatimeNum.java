/***********************************************************************
 * $Id: CmcCmReatimeNum.java,v1.0 2013-7-3 下午2:39:03 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

import java.text.DecimalFormat;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2013-7-3-下午2:39:03
 *
 */
@Alias("cmcCmReatimeNum")
public class CmcCmReatimeNum implements AliasesSuperType {
    private static final long serialVersionUID = -3786039744224958184L;
    private Long entityId;
    private Long ccIfIndex;
    private Long portIfIndex;
    private Long channelId;
    private Long channelFrequency;
    private Long interactiveNum;
    private Long broadbandNum;
    private Long mtaNum;
    private Long integratedNum;
    private Long onlineNum;
    private Long offlineNum;
    private Long otherNum;
    private Long cpeInteractiveNum;
    private Long cpeBroadbandNum;
    private Long cpeMtaNum;
    private Long cpeNum;
    private Long snr;
    private Integer channelModulationProfile;
    private String ccIfIndexString;
    private String snrString;
    private String frequencyString;
    private String channelModulationProfileString;
    private String channelString;
    private String ifDescr;
    public static final String[] MODULATIONPROFILETYPES = { "", "QPSK-Fair-Scdma", "QAM16-Fair-Scdma",
            "QAM64-Fair-Scdma", "QAM256-Fair-Scdma", "QPSK-Good-Scdma", "QAM16-Good-Scdma", "QAM64-Good-Scdma",
            "QAM256-Good-Scdma", "QAM64-Best-Scdma", "QAM256-Best-Scdma", "QPSK-Atdma", "QAM16-Atdma", "QAM64-Atdma",
            "QAM256-Atdma", "QAM64-Lowlatency-Scdma", "QAM256-Lowlatency-Scdma", "QAM32-Good-Scdma", "QAM32-Atdma" };

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Long getChannelFrequency() {
        return channelFrequency;
    }

    public void setChannelFrequency(Long channelFrequency) {
        this.channelFrequency = channelFrequency;
        double info = 0;
        if (this.channelFrequency != null) {
            DecimalFormat df = new DecimalFormat("0.000000");
            info = Double.parseDouble(df.format(this.channelFrequency / 1000000d));
        }
        this.frequencyString = info + " MHz";
    }

    public Long getMtaNum() {
        return mtaNum;
    }

    public void setMtaNum(Long mtaNum) {
        this.mtaNum = mtaNum;
    }

    public Long getIntegratedNum() {
        return integratedNum;
    }

    public void setIntegratedNum(Long integratedNum) {
        this.integratedNum = integratedNum;
    }

    public Long getOnlineNum() {
        return onlineNum;
    }

    public void setOnlineNum(Long onlineNum) {
        this.onlineNum = onlineNum;
    }

    public Long getOfflineNum() {
        return offlineNum;
    }

    public void setOfflineNum(Long offlineNum) {
        this.offlineNum = offlineNum;
    }

    public Long getSnr() {
        return snr;
    }

    public void setSnr(Long snr) {
        this.snr = snr;
        Float l = 0f;
        if (this.getSnr() != null) {
            l = (float) this.getSnr() / 10;
        }
        this.snrString = l.toString() + " dB";
    }

    public Long getOtherNum() {
        return otherNum;
    }

    public void setOtherNum(Long otherNum) {
        this.otherNum = otherNum;
    }

    public Long getCcIfIndex() {
        return ccIfIndex;
    }

    public void setCcIfIndex(Long ccIfIndex) {
        this.ccIfIndex = ccIfIndex;
        //modify by loyal cmts没有索引
        if (ccIfIndex != null) {
            this.ccIfIndexString = CmcIndexUtils.getSlotNo(ccIfIndex) + "/" + CmcIndexUtils.getPonNo(ccIfIndex) + "/"
                    + CmcIndexUtils.getCmcId(ccIfIndex);
        }
    }

    public Long getPortIfIndex() {
        return portIfIndex;
    }

    public void setPortIfIndex(Long portIfIndex) {
        this.portIfIndex = portIfIndex;
        this.channelId = CmcIndexUtils.getChannelId(portIfIndex);
    }

    public Long getInteractiveNum() {
        return interactiveNum;
    }

    public void setInteractiveNum(Long interactiveNum) {
        this.interactiveNum = interactiveNum;
    }

    public Long getBroadbandNum() {
        return broadbandNum;
    }

    public void setBroadbandNum(Long broadbandNum) {
        this.broadbandNum = broadbandNum;
    }

    public String getCcIfIndexString() {
        return ccIfIndexString;
    }

    public void setCcIfIndexString(String ccIfIndexString) {
        this.ccIfIndexString = ccIfIndexString;
    }

    public String getSnrString() {
        return snrString;
    }

    public void setSnrString(String snrString) {
        this.snrString = snrString;
    }

    public String getFrequencyString() {
        return frequencyString;
    }

    public void setFrequencyString(String frequencyString) {
        this.frequencyString = frequencyString;
    }

    public Integer getChannelModulationProfile() {
        return channelModulationProfile;
    }

    public void setChannelModulationProfile(Integer channelModulationProfile) {
        this.channelModulationProfile = channelModulationProfile;
        if (this.channelModulationProfile != null && channelModulationProfile < MODULATIONPROFILETYPES.length) {
            this.channelModulationProfileString = MODULATIONPROFILETYPES[channelModulationProfile.intValue()];
        } else {
            this.channelModulationProfileString = "Other";
        }
    }

    public String getChannelModulationProfileString() {
        return channelModulationProfileString;
    }

    public void setChannelModulationProfileString(String channelModulationProfileString) {
        this.channelModulationProfileString = channelModulationProfileString;
    }

    public Long getCpeNum() {
        return cpeNum;
    }

    public void setCpeNum(Long cpeNum) {
        this.cpeNum = cpeNum;
    }

    public String getChannelString() {
        return channelString;
    }

    public void setChannelString(String channelString) {
        this.channelString = channelString;
    }

    public String getIfDescr() {
        return ifDescr;
    }

    public void setIfDescr(String ifDescr) {
        this.ifDescr = ifDescr;
    }

    public Long getCpeInteractiveNum() {
        return cpeInteractiveNum;
    }

    public void setCpeInteractiveNum(Long cpeInteractiveNum) {
        this.cpeInteractiveNum = cpeInteractiveNum;
    }

    public Long getCpeBroadbandNum() {
        return cpeBroadbandNum;
    }

    public void setCpeBroadbandNum(Long cpeBroadbandNum) {
        this.cpeBroadbandNum = cpeBroadbandNum;
    }

    public Long getCpeMtaNum() {
        return cpeMtaNum;
    }

    public void setCpeMtaNum(Long cpeMtaNum) {
        this.cpeMtaNum = cpeMtaNum;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmcCmReatimeNum [entityId=");
        builder.append(entityId);
        builder.append(", ccIfIndex=");
        builder.append(ccIfIndex);
        builder.append(", portIfIndex=");
        builder.append(portIfIndex);
        builder.append(", channelId=");
        builder.append(channelId);
        builder.append(", channelFrequency=");
        builder.append(channelFrequency);
        builder.append(", interactiveNum=");
        builder.append(interactiveNum);
        builder.append(", broadbandNum=");
        builder.append(broadbandNum);
        builder.append(", mtaNum=");
        builder.append(mtaNum);
        builder.append(", integratedNum=");
        builder.append(integratedNum);
        builder.append(", onlineNum=");
        builder.append(onlineNum);
        builder.append(", offlineNum=");
        builder.append(offlineNum);
        builder.append(", otherNum=");
        builder.append(otherNum);
        builder.append(", cpeNum=");
        builder.append(cpeNum);
        builder.append(", snr=");
        builder.append(snr);
        builder.append(", channelModulationProfile=");
        builder.append(channelModulationProfile);
        builder.append(", ccIfIndexString=");
        builder.append(ccIfIndexString);
        builder.append(", snrString=");
        builder.append(snrString);
        builder.append(", frequencyString=");
        builder.append(frequencyString);
        builder.append(", channelModulationProfileString=");
        builder.append(channelModulationProfileString);
        builder.append(", channelString=");
        builder.append(channelString);
        builder.append(", ifDescr=");
        builder.append(ifDescr);
        builder.append("]");
        return builder.toString();
    }

}
