/***********************************************************************
 * $Id: DocsIf3CmtsCmUsStatus.java,v1.0 2013-4-27 上午9:21:10 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.facade.domain;

import java.io.Serializable;
import java.text.DecimalFormat;

import org.apache.ibatis.type.Alias;

import com.topvision.ems.cmc.remotequerycm.facade.domain.Cm3DsRemoteQuery;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.ems.cmc.util.CmcUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author haojie
 * @created @2013-4-27-上午9:21:10
 *
 */
@Alias("docsIf3CmtsCmUsStatus")
public class DocsIf3CmtsCmUsStatus implements Serializable, AliasesSuperType ,Comparable<DocsIf3CmtsCmUsStatus>{
    private static final long serialVersionUID = -2916803091880766034L;
    private Long cmId;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.20.1.3.1.1", index = true)
    private Long cmRegStatusId;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.20.1.4.1.1", index = true)
    private Long cmUsStatusChIfIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.20.1.4.1.2")
    private Integer cmUsStatusModulationType;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.20.1.4.1.3")
    private Long cmUsStatusRxPower;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.20.1.4.1.4")
    private Long cmUsStatusSignalNoise;
    private String cmUsStatusSignalNoiseString;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.20.1.4.1.5")
    private Long cmUsStatusMicroreflections;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.20.1.4.1.6")
    private String cmUsStatusEqData;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.20.1.4.1.7", type = "Count32")
    private Long cmUsStatusUnerroreds;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.20.1.4.1.8", type = "Count32")
    private Long cmUsStatusCorrecteds;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.20.1.4.1.9", type = "Count32")
    private Long cmUsStatusUncorrectables;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.20.1.4.1.10")
    private Long CmUsStatusHighResolutionTimingOffset;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.20.1.4.1.11")
    private Long cmUsStatusIsMuted;
    // @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.20.1.4.1.12")
    private Long cmUsStatusRangingStatus;
    private Long upChannelId;

    private String cmUsStatusSignalNoiseForUnit = "--";
    private String cmUsStatusUnerroredsForUnit = "--";
    private String cmUsStatusCorrectedsForUnit = "--";
    private String cmUsStatusUncorrectablesForUnit = "--";
    private String upChannelIdString;

    private String docsIfUpChannelFrequencyForUnit = "--";//上行射频映射过来的上行频率
    private final static DecimalFormat df = new DecimalFormat("0.0");
    
    @Override
    public int compareTo(DocsIf3CmtsCmUsStatus o) {
        if (o != null) {
            if (this.getUpChannelId().longValue() > o.getUpChannelId().longValue()) {
                return 1;
            } else if (this.getUpChannelId().longValue() == o.getUpChannelId().longValue()) {
                return 0;
            }
        }
        return -1;
    }

    public Long getCmId() {
        return cmId;
    }

    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    public Long getCmRegStatusId() {
        return cmRegStatusId;
    }

    public void setCmRegStatusId(Long cmRegStatusId) {
        this.cmRegStatusId = cmRegStatusId;
    }

    public Long getCmUsStatusChIfIndex() {
        return cmUsStatusChIfIndex;
    }

    public void setCmUsStatusChIfIndex(Long cmUsStatusChIfIndex) {
        this.cmUsStatusChIfIndex = cmUsStatusChIfIndex;
        if (cmUsStatusChIfIndex != null) {
            this.upChannelId = CmcIndexUtils.getChannelId(cmUsStatusChIfIndex);
            this.upChannelIdString = this.upChannelId.toString();
        }
    }

    public Integer getCmUsStatusModulationType() {
        return cmUsStatusModulationType;
    }

    public void setCmUsStatusModulationType(Integer cmUsStatusModulationType) {
        this.cmUsStatusModulationType = cmUsStatusModulationType;
    }

    public Long getCmUsStatusRxPower() {
        return cmUsStatusRxPower;
    }

    public void setCmUsStatusRxPower(Long cmUsStatusRxPower) {
        this.cmUsStatusRxPower = cmUsStatusRxPower;
    }

    public Long getCmUsStatusSignalNoise() {
        return cmUsStatusSignalNoise;
    }

    public void setCmUsStatusSignalNoise(Long cmUsStatusSignalNoise) {
        this.cmUsStatusSignalNoise = cmUsStatusSignalNoise;
        if (cmUsStatusSignalNoise != null) {
            this.cmUsStatusSignalNoiseString = (float) cmUsStatusSignalNoise / 10 + " dB";
        }
    }

    public Long getCmUsStatusMicroreflections() {
        return cmUsStatusMicroreflections;
    }

    public void setCmUsStatusMicroreflections(Long cmUsStatusMicroreflections) {
        this.cmUsStatusMicroreflections = cmUsStatusMicroreflections;
    }

    public String getCmUsStatusEqData() {
        return cmUsStatusEqData;
    }

    public void setCmUsStatusEqData(String cmUsStatusEqData) {
        this.cmUsStatusEqData = cmUsStatusEqData;
    }

    public Long getCmUsStatusUnerroreds() {
        return cmUsStatusUnerroreds;
    }

    public void setCmUsStatusUnerroreds(Long cmUsStatusUnerroreds) {
        this.cmUsStatusUnerroreds = cmUsStatusUnerroreds;
    }

    public Long getCmUsStatusCorrecteds() {
        return cmUsStatusCorrecteds;
    }

    public void setCmUsStatusCorrecteds(Long cmUsStatusCorrecteds) {
        this.cmUsStatusCorrecteds = cmUsStatusCorrecteds;
    }

    public Long getCmUsStatusUncorrectables() {
        return cmUsStatusUncorrectables;
    }

    public void setCmUsStatusUncorrectables(Long cmUsStatusUncorrectables) {
        this.cmUsStatusUncorrectables = cmUsStatusUncorrectables;
    }

    public Long getCmUsStatusHighResolutionTimingOffset() {
        return CmUsStatusHighResolutionTimingOffset;
    }

    public void setCmUsStatusHighResolutionTimingOffset(Long cmUsStatusHighResolutionTimingOffset) {
        CmUsStatusHighResolutionTimingOffset = cmUsStatusHighResolutionTimingOffset;
    }

    public Long getCmUsStatusIsMuted() {
        return cmUsStatusIsMuted;
    }

    public void setCmUsStatusIsMuted(Long cmUsStatusIsMuted) {
        this.cmUsStatusIsMuted = cmUsStatusIsMuted;
    }

    public Long getCmUsStatusRangingStatus() {
        return cmUsStatusRangingStatus;
    }

    public void setCmUsStatusRangingStatus(Long cmUsStatusRangingStatus) {
        this.cmUsStatusRangingStatus = cmUsStatusRangingStatus;
    }

    public Long getUpChannelId() {
        return upChannelId;
    }

    public void setUpChannelId(Long upChannelId) {
        this.upChannelId = upChannelId;
    }

    public String getCmUsStatusSignalNoiseString() {
        if (cmUsStatusSignalNoiseString == null || cmUsStatusSignalNoiseString.equalsIgnoreCase("")) {
            return "--";
        } else {
            return cmUsStatusSignalNoiseString;
        }
    }

    public void setCmUsStatusSignalNoiseString(String cmUsStatusSignalNoiseString) {
        this.cmUsStatusSignalNoiseString = cmUsStatusSignalNoiseString;
    }

    public String getCmUsStatusSignalNoiseForUnit() {
        if (this.getCmUsStatusSignalNoise() != null) {
            double NoiseForUnit = cmUsStatusSignalNoise;
            cmUsStatusSignalNoiseForUnit = df.format(NoiseForUnit / 10) /* + " dB" */;
        }
        return cmUsStatusSignalNoiseForUnit;
    }

    public void setCmUsStatusSignalNoiseForUnit(String cmUsStatusSignalNoiseForUnit) {
        this.cmUsStatusSignalNoiseForUnit = cmUsStatusSignalNoiseForUnit;
    }

    public String getCmUsStatusUnerroredsForUnit() {
        Long l = 0l;
        if (this.getCmUsStatusCorrecteds() != null && this.getCmUsStatusUncorrectables() != null
                && this.getCmUsStatusUnerroreds() != null) {
            l = this.getCmUsStatusCorrecteds() + this.getCmUsStatusUncorrectables() + this.getCmUsStatusUnerroreds();
            cmUsStatusUnerroredsForUnit = CmcUtil.turnToPercent(this.getCmUsStatusUnerroreds(), l);
            if (this.getCmUsStatusUnerroreds() > 1000000) {
                Long sigQuerroreds = this.getCmUsStatusUnerroreds() / 1000000;
                return sigQuerroreds + "M (" + cmUsStatusUnerroredsForUnit + ")";
            }
            cmUsStatusUnerroredsForUnit = this.getCmUsStatusUnerroreds() + "(" + cmUsStatusUnerroredsForUnit + ")";
        }
        return cmUsStatusUnerroredsForUnit;
    }

    public void setCmUsStatusUnerroredsForUnit(String cmUsStatusUnerroredsForUnit) {
        this.cmUsStatusUnerroredsForUnit = cmUsStatusUnerroredsForUnit;
    }

    public String getCmUsStatusCorrectedsForUnit() {
        Long l = 0l;
        if (this.getCmUsStatusCorrecteds() != null && this.getCmUsStatusUncorrectables() != null
                && this.getCmUsStatusUnerroreds() != null) {
            l = this.getCmUsStatusCorrecteds() + this.getCmUsStatusUncorrectables() + this.getCmUsStatusUnerroreds();
            cmUsStatusCorrectedsForUnit = this.getCmUsStatusCorrecteds() + "("
                    + CmcUtil.turnToPercent(this.getCmUsStatusCorrecteds(), l) + ")";
        }
        return cmUsStatusCorrectedsForUnit;
    }

    public void setCmUsStatusCorrectedsForUnit(String cmUsStatusCorrectedsForUnit) {
        this.cmUsStatusCorrectedsForUnit = cmUsStatusCorrectedsForUnit;
    }

    public String getCmUsStatusUncorrectablesForUnit() {
        Long l = 0l;
        if (this.getCmUsStatusCorrecteds() != null && this.getCmUsStatusUncorrectables() != null
                && this.getCmUsStatusUnerroreds() != null) {
            l = this.getCmUsStatusCorrecteds() + this.getCmUsStatusUncorrectables() + this.getCmUsStatusUnerroreds();
            cmUsStatusUncorrectablesForUnit = this.getCmUsStatusUncorrectables() + "("
                    + CmcUtil.turnToPercent(this.getCmUsStatusUncorrectables(), l) + ")";
        }
        return cmUsStatusUncorrectablesForUnit;
    }

    public void setCmUsStatusUncorrectablesForUnit(String cmUsStatusUncorrectablesForUnit) {
        this.cmUsStatusUncorrectablesForUnit = cmUsStatusUncorrectablesForUnit;
    }

    public String getDocsIfUpChannelFrequencyForUnit() {
        return docsIfUpChannelFrequencyForUnit;
    }

    public void setDocsIfUpChannelFrequencyForUnit(String docsIfUpChannelFrequencyForUnit) {
        this.docsIfUpChannelFrequencyForUnit = docsIfUpChannelFrequencyForUnit;
    }

    public String getUpChannelIdString() {
        return upChannelIdString;
    }

    public void setUpChannelIdString(String upChannelIdString) {
        this.upChannelIdString = upChannelIdString;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DocsIf3CmtsCmUsStatus [cmId=");
        builder.append(cmId);
        builder.append(", cmRegStatusId=");
        builder.append(cmRegStatusId);
        builder.append(", cmUsStatusChIfIndex=");
        builder.append(cmUsStatusChIfIndex);
        builder.append(", cmUsStatusModulationType=");
        builder.append(cmUsStatusModulationType);
        builder.append(", cmUsStatusRxPower=");
        builder.append(cmUsStatusRxPower);
        builder.append(", cmUsStatusSignalNoise=");
        builder.append(cmUsStatusSignalNoise);
        builder.append(", cmUsStatusSignalNoiseString=");
        builder.append(cmUsStatusSignalNoiseString);
        builder.append(", cmUsStatusMicroreflections=");
        builder.append(cmUsStatusMicroreflections);
        builder.append(", cmUsStatusEqData=");
        builder.append(cmUsStatusEqData);
        builder.append(", cmUsStatusUnerroreds=");
        builder.append(cmUsStatusUnerroreds);
        builder.append(", cmUsStatusCorrecteds=");
        builder.append(cmUsStatusCorrecteds);
        builder.append(", cmUsStatusUncorrectables=");
        builder.append(cmUsStatusUncorrectables);
        builder.append(", CmUsStatusHighResolutionTimingOffset=");
        builder.append(CmUsStatusHighResolutionTimingOffset);
        builder.append(", cmUsStatusIsMuted=");
        builder.append(cmUsStatusIsMuted);
        builder.append(", cmUsStatusRangingStatus=");
        builder.append(cmUsStatusRangingStatus);
        builder.append(", upChannelId=");
        builder.append(upChannelId);
        builder.append(", cmUsStatusSignalNoiseForUnit=");
        builder.append(cmUsStatusSignalNoiseForUnit);
        builder.append(", cmUsStatusUnerroredsForUnit=");
        builder.append(cmUsStatusUnerroredsForUnit);
        builder.append(", cmUsStatusCorrectedsForUnit=");
        builder.append(cmUsStatusCorrectedsForUnit);
        builder.append(", cmUsStatusUncorrectablesForUnit=");
        builder.append(cmUsStatusUncorrectablesForUnit);
        builder.append(", upChannelIdString=");
        builder.append(upChannelIdString);
        builder.append(", docsIfUpChannelFrequencyForUnit=");
        builder.append(docsIfUpChannelFrequencyForUnit);
        builder.append("]");
        return builder.toString();
    }

}
