/***********************************************************************
 * $Id: DownstreamChannelInfo.java,v1.0 2013-4-28 上午10:13:43 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmpoll.facade.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;

import java.io.Serializable;

/**
 * @author jay
 * @created @2015-3-20-上午10:13:43
 *
 */
@TableProperty(tables = { "default", "signal" })
public class UpstreamChannelInfo implements Serializable {
    private static final long serialVersionUID = 7034829237649438771L;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long ifIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.1")
    private Integer docsIfUpChannelId;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.2")
    private Long docsIfUpChannelFrequency;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.2.2.1.3")
    private Long docsIfCmStatusTxPower;//2.0上行发射电频
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.20.1.2.1.1")
    private Long docsIf3CmStatusUsTxPower;//3.0上行发射电频
    private Long txPower;//根据cm类型获取的电平值

    public Long getIfIndex() {
        return ifIndex;
    }

    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    public Integer getDocsIfUpChannelId() {
        return docsIfUpChannelId;
    }

    public void setDocsIfUpChannelId(Integer docsIfUpChannelId) {
        this.docsIfUpChannelId = docsIfUpChannelId;
    }

    public Long getDocsIfCmStatusTxPower() {
        return docsIfCmStatusTxPower;
    }

    public void setDocsIfCmStatusTxPower(Long docsIfCmStatusTxPower) {
        this.docsIfCmStatusTxPower = docsIfCmStatusTxPower;
    }

    public Long getDocsIf3CmStatusUsTxPower() {
        return docsIf3CmStatusUsTxPower;
    }

    public void setDocsIf3CmStatusUsTxPower(Long docsIf3CmStatusUsTxPower) {
        this.docsIf3CmStatusUsTxPower = docsIf3CmStatusUsTxPower;
    }

    public Long getTxPower() {
        return txPower;
    }

    public void setTxPower(Long txPower) {
        this.txPower = txPower;
    }

    public Long getDocsIfUpChannelFrequency() {
        return docsIfUpChannelFrequency;
    }

    public void setDocsIfUpChannelFrequency(Long docsIfUpChannelFrequency) {
        this.docsIfUpChannelFrequency = docsIfUpChannelFrequency;
    }

    @Override
    public String toString() {
        return "UpstreamChannelInfo{" +
                "ifIndex=" + ifIndex +
                ", docsIfUpChannelId=" + docsIfUpChannelId +
                ", docsIfUpChannelFrequency=" + docsIfUpChannelFrequency +
                ", docsIfCmStatusTxPower=" + docsIfCmStatusTxPower +
                ", docsIf3CmStatusUsTxPower=" + docsIf3CmStatusUsTxPower +
                ", txPower=" + txPower +
                '}';
    }
}
