/***********************************************************************
 * $Id: DownstreamChannelInfo.java,v1.0 2013-4-28 上午10:13:43 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmpoll.facade.domain;

import com.topvision.framework.annotation.SnmpProperty;

import java.io.Serializable;

/**
 * @author jay
 * @created @2015-3-20-上午10:13:43
 *
 */
public class DownstreamChannelInfo implements Serializable {
    private static final long serialVersionUID = 7034829237649438771L;
    //下行发射电频表
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long ifIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.1.1.1")
    private Integer docsIfDownChannelId;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.1.1.2")
    private Long docsIfDownChannelFrequency;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.1.1.6")
    private Long docsIfDownChannelPower;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.4.1.5")
    private Long docsIfSigQSignalNoise;

    public Long getIfIndex() {
        return ifIndex;
    }

    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    public Long getDocsIfDownChannelPower() {
        return docsIfDownChannelPower;
    }

    public void setDocsIfDownChannelPower(Long docsIfDownChannelPower) {
        this.docsIfDownChannelPower = docsIfDownChannelPower;
    }

    public Long getDocsIfSigQSignalNoise() {
        return docsIfSigQSignalNoise;
    }

    public void setDocsIfSigQSignalNoise(Long docsIfSigQSignalNoise) {
        this.docsIfSigQSignalNoise = docsIfSigQSignalNoise;
    }

    public Integer getDocsIfDownChannelId() {
        return docsIfDownChannelId;
    }

    public void setDocsIfDownChannelId(Integer docsIfDownChannelId) {
        this.docsIfDownChannelId = docsIfDownChannelId;
    }

    public Long getDocsIfDownChannelFrequency() {
        return docsIfDownChannelFrequency;
    }

    public void setDocsIfDownChannelFrequency(Long docsIfDownChannelFrequency) {
        this.docsIfDownChannelFrequency = docsIfDownChannelFrequency;
    }

    @Override
    public String toString() {
        return "DownstreamChannelInfo{" +
                "ifIndex=" + ifIndex +
                ", docsIfDownChannelId=" + docsIfDownChannelId +
                ", docsIfDownChannelFrequency=" + docsIfDownChannelFrequency +
                ", docsIfDownChannelPower=" + docsIfDownChannelPower +
                ", docsIfSigQSignalNoise=" + docsIfSigQSignalNoise +
                '}';
    }
}
