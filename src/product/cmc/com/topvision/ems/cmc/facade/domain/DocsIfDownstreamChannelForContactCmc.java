/***********************************************************************
 * $Id: DocsIfDownstreamChannel.java,v1.0 2013-4-28 上午10:13:43 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.facade.domain;

import java.io.Serializable;
import java.text.DecimalFormat;

import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author haojie
 * @created @2013-4-28-上午10:13:43
 * 
 */
public class DocsIfDownstreamChannelForContactCmc implements Serializable, Comparable<DocsIfDownstreamChannelForContactCmc> {
    private static final long serialVersionUID = 7034829237649438771L;
    // 下行发射电频表
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long ifIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.1.1.1")
    private Long docsIfDownChannelId;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.1.1.6")
    private Long docsIfDownChannelPower;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.1.1.2")
    private Long docsIfDownChannelFrequency;

    private final static DecimalFormat df = new DecimalFormat("0.0");
    private String docsIfDownChannelPowerForUnit;
    private String docsIfDownChannelFrequencyForUnit;

    public Long getDocsIfDownChannelId() {
        return docsIfDownChannelId;
    }

    public void setDocsIfDownChannelId(Long docsIfDownChannelId) {
        this.docsIfDownChannelId = docsIfDownChannelId;
    }

    public Long getDocsIfDownChannelPower() {
        return docsIfDownChannelPower;
    }

    public void setDocsIfDownChannelPower(Long docsIfDownChannelPower) {
        this.docsIfDownChannelPower = docsIfDownChannelPower;
    }

    public String getDocsIfDownChannelFrequencyForUnit() {
        if (this.getDocsIfDownChannelFrequency() != null) {
            double dcf = docsIfDownChannelFrequency;
            docsIfDownChannelFrequencyForUnit = df.format(dcf / 1000 / 1000);
        }
        return docsIfDownChannelFrequencyForUnit;
    }

    public void setDocsIfDownChannelFrequencyForUnit(String docsIfDownChannelFrequencyForUnit) {
        this.docsIfDownChannelFrequencyForUnit = docsIfDownChannelFrequencyForUnit;
    }

    public String getDocsIfDownChannelPowerForUnit() {
        if (this.getDocsIfDownChannelPower() != null) {
            double downChannelPowerForUnit = docsIfDownChannelPower;
            docsIfDownChannelPowerForUnit = df.format(downChannelPowerForUnit / 10) /* + " dBmV" */;
        }
        return docsIfDownChannelPowerForUnit;
    }

    public void setDocsIfDownChannelPowerForUnit(String docsIfDownChannelPowerForUnit) {
        this.docsIfDownChannelPowerForUnit = docsIfDownChannelPowerForUnit;
    }

    public Long getIfIndex() {
        return ifIndex;
    }

    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    public Long getDocsIfDownChannelFrequency() {
        return docsIfDownChannelFrequency;
    }

    public void setDocsIfDownChannelFrequency(Long docsIfDownChannelFrequency) {
        this.docsIfDownChannelFrequency = docsIfDownChannelFrequency;
    }

    @Override
    public int compareTo(DocsIfDownstreamChannelForContactCmc another) {
        return (int) (this.docsIfDownChannelId - another.docsIfDownChannelId);
    }

}
