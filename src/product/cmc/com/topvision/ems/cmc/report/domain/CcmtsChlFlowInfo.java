/***********************************************************************
 * $Id: CcmtsChlFlowInfo.java,v1.0 2014-3-28 下午2:45:38 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.domain;

import java.text.DecimalFormat;

import com.topvision.framework.utils.CmcIndexUtils;

/**
 * @author haojie
 * @created @2014-3-28-下午2:45:38
 *
 */
public class CcmtsChlFlowInfo {
    private Long cmcId;
    private Long channelIndex;//信道index
    private String channelName;//信道名称，需要自己组装
    private Integer modulationProfile;//调制方式
    private String modulationProfileString;
    private Double maxFlow;//信道流量最大值
    private String maxFlowString;
    private Double maxFlowUsage;//信道流量利用率最大值
    private String maxFlowUsageString;
    private static final DecimalFormat df = new DecimalFormat("##0.00");

    public static final String[] UP_MODULE_NAMES = { "", "Scdma-QPSK-Fair", "Scdma-QAM16-Fair", "Scdma-QAM64-Fair",
            "Scdma-QAM256-Fair", "Scdma-QPSK-Good", "Scdma-QAM16-Good", "Scdma-QAM64-Good", "Scdma-QAM256-Good",
            "Scdma-QAM64-Best", "Scdma-QAM256-Best", "Atdma-QPSK", "Atdma-QAM16", "Atdma-QAM64", "Atdma-QAM256",
            "Scdma-QAM64-Lowlatency", "Scdma-QAM256-Lowlatency", "Scdma-QAM32-Good", "Atdma-QAM32" };
    public static final String[] DOWN_MODULE_NAMES = { "", "", "QAM1024", "QAM64", "QAM256" };

    public Long getChannelIndex() {
        return channelIndex;
    }

    public void setChannelIndex(Long channelIndex) {
        this.channelIndex = channelIndex;
        if (channelIndex != null) {
            channelName = CmcIndexUtils.getChannelNameByIndex(channelIndex);
        }
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Integer getModulationProfile() {
        return modulationProfile;
    }

    public void setModulationProfile(Integer modulationProfile) {
        this.modulationProfile = modulationProfile;
        if (channelIndex != null) {
            Long channelType = CmcIndexUtils.getChannelType(channelIndex);
            if (modulationProfile != null) {
                if (channelType.equals(1l)) {
                    modulationProfileString = DOWN_MODULE_NAMES[modulationProfile];
                } else {
                    modulationProfileString = UP_MODULE_NAMES[modulationProfile];
                }
            }
        }
    }

    public String getModulationProfileString() {
        return modulationProfileString;
    }

    public void setModulationProfileString(String modulationProfileString) {
        this.modulationProfileString = modulationProfileString;
    }

    public Double getMaxFlow() {
        return maxFlow;
    }

    public void setMaxFlow(Double maxFlow) {
        this.maxFlow = maxFlow;
        if (maxFlow != null) {
            maxFlowString = df.format(maxFlow);
        }
    }

    public String getMaxFlowString() {
        return maxFlowString;
    }

    public void setMaxFlowString(String maxFlowString) {
        this.maxFlowString = maxFlowString;
    }

    public Double getMaxFlowUsage() {
        return maxFlowUsage;
    }

    public void setMaxFlowUsage(Double maxFlowUsage) {
        this.maxFlowUsage = maxFlowUsage;
        if (maxFlowUsage != null) {
            maxFlowUsageString = df.format(maxFlowUsage);
        }
    }

    public String getMaxFlowUsageString() {
        return maxFlowUsageString;
    }

    public void setMaxFlowUsageString(String maxFlowUsageString) {
        this.maxFlowUsageString = maxFlowUsageString;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CcmtsChlFlowInfo [cmcId=");
        builder.append(cmcId);
        builder.append(", channelIndex=");
        builder.append(channelIndex);
        builder.append(", channelName=");
        builder.append(channelName);
        builder.append(", modulationProfile=");
        builder.append(modulationProfile);
        builder.append(", modulationProfileString=");
        builder.append(modulationProfileString);
        builder.append(", maxFlow=");
        builder.append(maxFlow);
        builder.append(", maxFlowString=");
        builder.append(maxFlowString);
        builder.append(", maxFlowUsage=");
        builder.append(maxFlowUsage);
        builder.append(", maxFlowUsageString=");
        builder.append(maxFlowUsageString);
        builder.append("]");
        return builder.toString();
    }

}
