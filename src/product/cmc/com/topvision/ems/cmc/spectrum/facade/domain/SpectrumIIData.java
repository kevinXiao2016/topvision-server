package com.topvision.ems.cmc.spectrum.facade.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.domain.PhysAddress;

import java.io.Serializable;

/**
 * @author bryan
 */
public class SpectrumIIData implements Serializable {
    private static final long serialVersionUID = 9143821457510086278L;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.16.4.1.1", index = true)
    private PhysAddress macIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.16.4.1.2", type = "OctetString")
    private String dataBuffer;

    public PhysAddress getMacIndex() {
        return macIndex;
    }

    public void setMacIndex(PhysAddress macIndex) {
        this.macIndex = macIndex;
    }

    public String getDataBuffer() {
        return dataBuffer;
    }

    public void setDataBuffer(String dataBuffer) {
        this.dataBuffer = dataBuffer;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    @Override
    public String toString() {
        return "SpectrumIIData{" +
                "cmcId=" + cmcId +
                ", macIndex=" + macIndex +
                ", dataBuffer='" + dataBuffer + '\'' +
                '}';
    }
}
