/***********************************************************************
 * $Id: DocsIfUpstreamChannel.java,v1.0 2013-4-28 上午10:20:21 $
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
 * @created @2013-4-28-上午10:20:21
 * 
 */
public class DocsIfUpstreamChannelForContactCmc implements Serializable, Comparable<DocsIfUpstreamChannelForContactCmc> {
    private static final long serialVersionUID = -1371578805492462866L;
    // CM上行发射电频表
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long ifIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.1")
    private Integer docsIfUpChannelId;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.20.1.2.1.1")
    private Integer docsIf3CmStatusUsTxPower;// 3.0上行发射电频
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.2.2.1.3")
    private Integer docsIfCmStatusTxPower;// 2.0上行发射电频
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.2")
    private Long docsIfUpChannelFrequency;

    private String txPower;// 根据cm类型获取的电平值

    private String docsIf3CmStatusUsTxPowerForUnit;
    private String docsIfCmStatusTxPowerForUnit;
    private String docsIfUpChannelFrequencyForUnit; // 上行信道频率

    private final static DecimalFormat df = new DecimalFormat("0.0");

    public Integer getDocsIfUpChannelId() {
        return docsIfUpChannelId;
    }

    public void setDocsIfUpChannelId(Integer docsIfUpChannelId) {
        this.docsIfUpChannelId = docsIfUpChannelId;
    }

    public Integer getDocsIf3CmStatusUsTxPower() {
        return docsIf3CmStatusUsTxPower;
    }

    public String getDocsIfUpChannelFrequencyForUnit() {
        if (this.getDocsIfUpChannelFrequency() != null) {
            double ucf = docsIfUpChannelFrequency;
            docsIfUpChannelFrequencyForUnit = df.format(ucf / 1000 / 1000);
        }
        return docsIfUpChannelFrequencyForUnit;
    }

    public void setDocsIfUpChannelFrequencyForUnit(String docsIfUpChannelFrequencyForUnit) {
        this.docsIfUpChannelFrequencyForUnit = docsIfUpChannelFrequencyForUnit;
    }

    public void setDocsIf3CmStatusUsTxPower(Integer docsIf3CmStatusUsTxPower) {
        this.docsIf3CmStatusUsTxPower = docsIf3CmStatusUsTxPower;
    }

    public String getDocsIf3CmStatusUsTxPowerForUnit() {
        if (this.getDocsIf3CmStatusUsTxPower() != null) {
            double TxPowerForUnit = docsIf3CmStatusUsTxPower;
            docsIf3CmStatusUsTxPowerForUnit = df.format(TxPowerForUnit / 10);
        }
        return docsIf3CmStatusUsTxPowerForUnit;
    }

    public void setDocsIf3CmStatusUsTxPowerForUnit(String docsIf3CmStatusUsTxPowerForUnit) {
        this.docsIf3CmStatusUsTxPowerForUnit = docsIf3CmStatusUsTxPowerForUnit;
    }

    public Integer getDocsIfCmStatusTxPower() {
        return docsIfCmStatusTxPower;
    }

    public void setDocsIfCmStatusTxPower(Integer docsIfCmStatusTxPower) {
        this.docsIfCmStatusTxPower = docsIfCmStatusTxPower;
    }

    public String getDocsIfCmStatusTxPowerForUnit() {
        if (this.getDocsIfCmStatusTxPower() != null) {
            double TxPowerForUnit = docsIfCmStatusTxPower;
            docsIfCmStatusTxPowerForUnit = df.format(TxPowerForUnit / 10);
        } else {
            docsIfCmStatusTxPowerForUnit = this.getDocsIf3CmStatusUsTxPowerForUnit();
        }
        return docsIfCmStatusTxPowerForUnit;
    }

    public void setDocsIfCmStatusTxPowerForUnit(String docsIfCmStatusTxPowerForUnit) {
        this.docsIfCmStatusTxPowerForUnit = docsIfCmStatusTxPowerForUnit;
    }

    public String getTxPower() {
        return txPower;
    }

    public void setTxPower(String txPower) {
        this.txPower = txPower;
    }

    public Long getIfIndex() {
        return ifIndex;
    }

    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    public Long getDocsIfUpChannelFrequency() {
        return docsIfUpChannelFrequency;
    }

    public void setDocsIfUpChannelFrequency(Long docsIfUpChannelFrequency) {
        this.docsIfUpChannelFrequency = docsIfUpChannelFrequency;
    }

    @Override
    public int compareTo(DocsIfUpstreamChannelForContactCmc another) {
        return this.docsIfUpChannelId - another.docsIfUpChannelId;
    }

}
