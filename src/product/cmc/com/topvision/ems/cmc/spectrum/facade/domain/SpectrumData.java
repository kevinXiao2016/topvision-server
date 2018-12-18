package com.topvision.ems.cmc.spectrum.facade.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.domain.PhysAddress;

/**
 * @author bryan
 */
public class SpectrumData implements Serializable {
    private static final long serialVersionUID = 9143821457510086278L;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.16.3.1.1", index = true)
    private PhysAddress topCcmtsFftDataCmcMacIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.16.3.1.2", index = true)
    private Integer topCcmtsFftDataFreqIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.16.3.1.3", type = "OctetString")
    private String topCcmtsFftDataBuffer;

    public PhysAddress getTopCcmtsFftDataCmcMacIndex() {
        return topCcmtsFftDataCmcMacIndex;
    }

    public void setTopCcmtsFftDataCmcMacIndex(PhysAddress topCcmtsFftDataCmcMacIndex) {
        this.topCcmtsFftDataCmcMacIndex = topCcmtsFftDataCmcMacIndex;
    }

    public Integer getTopCcmtsFftDataFreqIndex() {
        return topCcmtsFftDataFreqIndex;
    }

    public void setTopCcmtsFftDataFreqIndex(Integer topCcmtsFftDataFreqIndex) {
        this.topCcmtsFftDataFreqIndex = topCcmtsFftDataFreqIndex;
    }

    public String getTopCcmtsFftDataBuffer() {
        return topCcmtsFftDataBuffer;
    }

    public void setTopCcmtsFftDataBuffer(String topCcmtsFftDataBuffer) {
        this.topCcmtsFftDataBuffer = topCcmtsFftDataBuffer;
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
        builder.append("CmcFftMonitorData [cmcId=");
        builder.append(cmcId);
        builder.append(", topCcmtsFftDataCmcMacIndex=");
        builder.append(topCcmtsFftDataCmcMacIndex);
        builder.append(", topCcmtsFftDataFreqIndex=");
        builder.append(topCcmtsFftDataFreqIndex);
        builder.append(", topCcmtsFftDataBuffer=");
        builder.append(topCcmtsFftDataBuffer);
        builder.append("]");
        return builder.toString();
    }

}
