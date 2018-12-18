/***********************************************************************
 * $Id: DownstreamChannelInfo.java,v1.0 2013-4-28 上午10:13:43 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.facade.domain;

import com.topvision.framework.annotation.SnmpProperty;

import java.io.Serializable;

/**
 * @author jay
 * @created @2015-3-20-上午10:13:43
 *
 */
public class CmDownChannel implements Serializable {
    private static final long serialVersionUID = 7034829237649438771L;
    //下行发射电频表
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long ifIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.1.1.6")
    private Integer docsIfDownChannelPower;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.4.1.5")
    private Integer docsIfSigQSignalNoise;

    public Long getIfIndex() {
        return ifIndex;
    }

    public void setIfIndex(Long ifIndex) {
        this.ifIndex = ifIndex;
    }

    public Integer getDocsIfDownChannelPower() {
        return docsIfDownChannelPower;
    }

    public void setDocsIfDownChannelPower(Integer docsIfDownChannelPower) {
        this.docsIfDownChannelPower = docsIfDownChannelPower;
    }

    public Integer getDocsIfSigQSignalNoise() {
        return docsIfSigQSignalNoise;
    }

    public void setDocsIfSigQSignalNoise(Integer docsIfSigQSignalNoise) {
        this.docsIfSigQSignalNoise = docsIfSigQSignalNoise;
    }

    @Override
    public String toString() {
        return "CmDownChannel{" +
                "ifIndex=" + ifIndex +
                ", docsIfDownChannelPower=" + docsIfDownChannelPower +
                ", docsIfSigQSignalNoise=" + docsIfSigQSignalNoise +
                '}';
    }
}
