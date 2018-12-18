/***********************************************************************
 * $Id: DocsIfSignalQuality.java,v1.0 2013-4-28 上午10:31:53 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.facade.domain;

import java.io.Serializable;
import java.text.DecimalFormat;

import com.topvision.ems.cmc.util.CmcUtil;
import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author haojie
 * @created @2013-4-28-上午10:31:53
 *
 */
public class DocsIfSignalQuality implements Serializable {
    private static final long serialVersionUID = -6073906532878063277L;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long ifIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.4.1.1")
    private Integer docsIfSigQIncludesContention;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.4.1.2")
    private Long docsIfSigQUnerroreds;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.4.1.3")
    private Long docsIfSigQCorrecteds;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.4.1.4")
    private Long docsIfSigQUncorrectables;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.4.1.5")
    private Long docsIfSigQSignalNoise;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.4.1.6")
    private Long docsIfSigQMicroreflections;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.4.1.7")
    private String docsIfSigQEqualizationData;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.4.1.8")
    private Long docsIfSigQExtUnerroreds;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.4.1.9")
    private Long docsIfSigQExtCorrecteds;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.4.1.10")
    private Long docsIfSigQExtUncorrectables;
    private Long downChanelId;

    private final static DecimalFormat df = new DecimalFormat("0.00");
    private String docsIfSigQMicroreflectionsForUnit;
    private String docsIfSigQSignalNoiseForUnit;
    private String docsIfSigQUnerroredsForUnit;
    private String docsIfSigQCorrectedsForUnit;
    private String docsIfSigQUncorrectablesForUnit;
    private String downChanelIdString;//add by loyal 兼容cmts下cm

    private String docsIfDownChannelFrequencyForUnit;//下行射频映射过来的信道频率

    public Long getIfIndex() {
        return ifIndex;
    }

    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    public Integer getDocsIfSigQIncludesContention() {
        return docsIfSigQIncludesContention;
    }

    public void setDocsIfSigQIncludesContention(Integer docsIfSigQIncludesContention) {
        this.docsIfSigQIncludesContention = docsIfSigQIncludesContention;
    }

    public Long getDocsIfSigQUnerroreds() {
        return docsIfSigQUnerroreds;
    }

    public void setDocsIfSigQUnerroreds(Long docsIfSigQUnerroreds) {
        this.docsIfSigQUnerroreds = docsIfSigQUnerroreds;
    }

    public Long getDocsIfSigQCorrecteds() {
        return docsIfSigQCorrecteds;
    }

    public void setDocsIfSigQCorrecteds(Long docsIfSigQCorrecteds) {
        this.docsIfSigQCorrecteds = docsIfSigQCorrecteds;
    }

    public Long getDocsIfSigQUncorrectables() {
        return docsIfSigQUncorrectables;
    }

    public void setDocsIfSigQUncorrectables(Long docsIfSigQUncorrectables) {
        this.docsIfSigQUncorrectables = docsIfSigQUncorrectables;
    }

    public Long getDocsIfSigQSignalNoise() {
        return docsIfSigQSignalNoise;
    }

    public void setDocsIfSigQSignalNoise(Long docsIfSigQSignalNoise) {
        this.docsIfSigQSignalNoise = docsIfSigQSignalNoise;
    }

    public Long getDocsIfSigQMicroreflections() {
        return docsIfSigQMicroreflections;
    }

    public void setDocsIfSigQMicroreflections(Long docsIfSigQMicroreflections) {
        this.docsIfSigQMicroreflections = docsIfSigQMicroreflections;
    }

    public String getDocsIfSigQEqualizationData() {
        return docsIfSigQEqualizationData;
    }

    public void setDocsIfSigQEqualizationData(String docsIfSigQEqualizationData) {
        this.docsIfSigQEqualizationData = docsIfSigQEqualizationData;
    }

    public Long getDocsIfSigQExtUnerroreds() {
        return docsIfSigQExtUnerroreds;
    }

    public void setDocsIfSigQExtUnerroreds(Long docsIfSigQExtUnerroreds) {
        this.docsIfSigQExtUnerroreds = docsIfSigQExtUnerroreds;
    }

    public Long getDocsIfSigQExtCorrecteds() {
        return docsIfSigQExtCorrecteds;
    }

    public void setDocsIfSigQExtCorrecteds(Long docsIfSigQExtCorrecteds) {
        this.docsIfSigQExtCorrecteds = docsIfSigQExtCorrecteds;
    }

    public Long getDocsIfSigQExtUncorrectables() {
        return docsIfSigQExtUncorrectables;
    }

    public void setDocsIfSigQExtUncorrectables(Long docsIfSigQExtUncorrectables) {
        this.docsIfSigQExtUncorrectables = docsIfSigQExtUncorrectables;
    }

    public Long getDownChanelId() {
        return downChanelId;
    }

    public void setDownChanelId(Long downChanelId) {
        this.downChanelId = downChanelId;
        this.downChanelIdString = downChanelId.toString();
    }

    public String getDocsIfSigQMicroreflectionsForUnit() {
        if (this.getDocsIfSigQMicroreflections() != null) {
            this.docsIfSigQMicroreflectionsForUnit = this.getDocsIfSigQMicroreflections().toString() /*+ " -dBc"*/;
        }
        return docsIfSigQMicroreflectionsForUnit;
    }

    public void setDocsIfSigQMicroreflectionsForUnit(String docsIfSigQMicroreflectionsForUnit) {
        this.docsIfSigQMicroreflectionsForUnit = docsIfSigQMicroreflectionsForUnit;
    }

    public String getDocsIfSigQSignalNoiseForUnit() {
        if (this.getDocsIfSigQSignalNoise() != null) {
            double NoiseForUnit = docsIfSigQSignalNoise;
            docsIfSigQSignalNoiseForUnit = df.format(NoiseForUnit / 10) /*+ " dB"*/;
        }
        return docsIfSigQSignalNoiseForUnit;
    }

    public void setDocsIfSigQSignalNoiseForUnit(String docsIfSigQSignalNoiseForUnit) {
        this.docsIfSigQSignalNoiseForUnit = docsIfSigQSignalNoiseForUnit;
    }

    public String getDocsIfSigQUnerroredsForUnit() {
        Long l = 0l;
        if (this.getDocsIfSigQCorrecteds() != null && this.getDocsIfSigQUncorrectables() != null
                && this.getDocsIfSigQUnerroreds() != null) {
            l = this.getDocsIfSigQCorrecteds() + this.getDocsIfSigQUncorrectables() + this.getDocsIfSigQUnerroreds();
        }
        if (this.getDocsIfSigQUnerroreds() != null) {
            docsIfSigQUnerroredsForUnit = CmcUtil.turnToPercent(this.getDocsIfSigQUnerroreds(), l);
            if (this.getDocsIfSigQUnerroreds() > 1000000) {
                Long sigQuerroreds = this.getDocsIfSigQUnerroreds() / 1000000;
                return sigQuerroreds + "M (" + docsIfSigQUnerroredsForUnit + ")";
            }
        }
        return this.getDocsIfSigQUnerroreds() + "(" + docsIfSigQUnerroredsForUnit + ")";
    }

    public void setDocsIfSigQUnerroredsForUnit(String docsIfSigQUnerroredsForUnit) {
        this.docsIfSigQUnerroredsForUnit = docsIfSigQUnerroredsForUnit;
    }

    public String getDocsIfSigQCorrectedsForUnit() {
        Long l = 0l;
        if (this.getDocsIfSigQCorrecteds() != null && this.getDocsIfSigQUncorrectables() != null
                && this.getDocsIfSigQUnerroreds() != null) {
            l = this.getDocsIfSigQCorrecteds() + this.getDocsIfSigQUncorrectables() + this.getDocsIfSigQUnerroreds();
        }
        if (this.getDocsIfSigQCorrecteds() != null) {
            docsIfSigQCorrectedsForUnit = CmcUtil.turnToPercent(this.getDocsIfSigQCorrecteds(), l);
        }
        return this.getDocsIfSigQCorrecteds() + "(" + docsIfSigQCorrectedsForUnit + ")";
    }

    public void setDocsIfSigQCorrectedsForUnit(String docsIfSigQCorrectedsForUnit) {
        this.docsIfSigQCorrectedsForUnit = docsIfSigQCorrectedsForUnit;
    }

    public String getDocsIfSigQUncorrectablesForUnit() {
        Long l = 0l;
        if (this.getDocsIfSigQCorrecteds() != null && this.getDocsIfSigQUncorrectables() != null
                && this.getDocsIfSigQUnerroreds() != null) {
            l = this.getDocsIfSigQCorrecteds() + this.getDocsIfSigQUncorrectables() + this.getDocsIfSigQUnerroreds();
        }
        if (this.getDocsIfSigQUncorrectables() != null) {
            docsIfSigQUncorrectablesForUnit = CmcUtil.turnToPercent(this.getDocsIfSigQUncorrectables(), l);
        }
        return this.getDocsIfSigQUncorrectables() + "(" + docsIfSigQUncorrectablesForUnit + ")";
    }

    public void setDocsIfSigQUncorrectablesForUnit(String docsIfSigQUncorrectablesForUnit) {
        this.docsIfSigQUncorrectablesForUnit = docsIfSigQUncorrectablesForUnit;
    }

    public String getDocsIfDownChannelFrequencyForUnit() {
        return docsIfDownChannelFrequencyForUnit;
    }

    public void setDocsIfDownChannelFrequencyForUnit(String docsIfDownChannelFrequencyForUnit) {
        this.docsIfDownChannelFrequencyForUnit = docsIfDownChannelFrequencyForUnit;
    }
    
    public String getDownChanelIdString() {
        return downChanelIdString;
    }

    public void setDownChanelIdString(String downChanelIdString) {
        this.downChanelIdString = downChanelIdString;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DocsIfSignalQuality [ifIndex=");
        builder.append(ifIndex);
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
        builder.append(", downChanelId=");
        builder.append(downChanelId);
        builder.append(", docsIfSigQMicroreflectionsForUnit=");
        builder.append(docsIfSigQMicroreflectionsForUnit);
        builder.append(", docsIfSigQSignalNoiseForUnit=");
        builder.append(docsIfSigQSignalNoiseForUnit);
        builder.append(", docsIfSigQUnerroredsForUnit=");
        builder.append(docsIfSigQUnerroredsForUnit);
        builder.append(", docsIfSigQCorrectedsForUnit=");
        builder.append(docsIfSigQCorrectedsForUnit);
        builder.append(", docsIfSigQUncorrectablesForUnit=");
        builder.append(docsIfSigQUncorrectablesForUnit);
        builder.append(", downChanelIdString=");
        builder.append(downChanelIdString);
        builder.append(", docsIfDownChannelFrequencyForUnit=");
        builder.append(docsIfDownChannelFrequencyForUnit);
        builder.append("]");
        return builder.toString();
    }

}
