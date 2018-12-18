/***********************************************************************
 * $Id: SpectrumCompressedData.java,v1.0 2014年4月17日 上午11:49:39 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.spectrum.facade.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.domain.PhysAddress;

/**
 * @author YangYi
 * @created @2014年4月17日-上午11:49:39
 * 
 */
public class SpectrumCompressedData implements Serializable {
    private static final long serialVersionUID = -9095618629244661448L;
    private Long cmcId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.16.4.1.1", index = true)
    private PhysAddress topCcmtsCompressedFftDataCmcMacIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.1.1.2.16.4.1.2", type = "OctetString")
    private String topCcmtsCompressedFftDataBuffer;

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public PhysAddress getTopCcmtsCompressedFftDataCmcMacIndex() {
        return topCcmtsCompressedFftDataCmcMacIndex;
    }

    public void setTopCcmtsCompressedFftDataCmcMacIndex(PhysAddress topCcmtsCompressedFftDataCmcMacIndex) {
        this.topCcmtsCompressedFftDataCmcMacIndex = topCcmtsCompressedFftDataCmcMacIndex;
    }

    public String getTopCcmtsCompressedFftDataBuffer() {
        return topCcmtsCompressedFftDataBuffer;
    }

    public void setTopCcmtsCompressedFftDataBuffer(String topCcmtsCompressedFftDataBuffer) {
        this.topCcmtsCompressedFftDataBuffer = topCcmtsCompressedFftDataBuffer;
    }

}
