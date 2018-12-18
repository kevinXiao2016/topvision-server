/***********************************************************************
 * $ DownChannelTxPower.java,v1.0 14-5-11 下午2:35 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.facade.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import org.apache.ibatis.type.Alias;

/**
 * @author jay
 * @created @14-5-11-下午2:35
 */
@Alias("downChannelTxPower")
public class DownChannelTxPower implements Serializable{
    private static final long serialVersionUID = 7342180206202447869L;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.1.1.1", index = true)
    private Long channelId;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.1.1.6")
    private Long txPower;

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Long getTxPower() {
        return txPower;
    }

    public void setTxPower(Long txPower) {
        this.txPower = txPower;
    }
}
