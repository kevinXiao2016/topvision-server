/***********************************************************************
 * $Id: CmcUpChannelRanging.java,v1.0 2014-11-28-下午04:17:42 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.upchannel.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author YangYi
 * @created @2014-11-28-下午04:17:42
 * 
 */
@Alias("cmcUpChannelRanging")
public class CmcUpChannelRanging implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -1193861734123701564L;
    private Long cmcId;
    private Long cmcPortId;
    @SnmpProperty(oid = "1.3.6.1.2.1.2.2.1.1", index = true)
    private Long channelIndex;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.7", writable = true, type = "Integer32")
    private Integer channelRangingBackoffStart; // 上行测距BackoffStart
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.8", writable = true, type = "Integer32")
    private Integer channelRangingBackoffEnd; // 上行测距BackoffEnd
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.9", writable = true, type = "Integer32")
    private Integer channelTxBackoffStart;
    @SnmpProperty(oid = "1.3.6.1.2.1.10.127.1.1.2.1.10", writable = true, type = "Integer32")
    private Integer channelTxBackoffEnd;

    public Long getCmcId() {
        return cmcId;
    }

    public Long getCmcPortId() {
        return cmcPortId;
    }

    public Long getChannelIndex() {
        return channelIndex;
    }

    public Integer getChannelRangingBackoffStart() {
        return channelRangingBackoffStart;
    }

    public Integer getChannelRangingBackoffEnd() {
        return channelRangingBackoffEnd;
    }

    public Integer getChannelTxBackoffStart() {
        return channelTxBackoffStart;
    }

    public Integer getChannelTxBackoffEnd() {
        return channelTxBackoffEnd;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public void setCmcPortId(Long cmcPortId) {
        this.cmcPortId = cmcPortId;
    }

    public void setChannelIndex(Long channelIndex) {
        this.channelIndex = channelIndex;
    }

    public void setChannelRangingBackoffStart(Integer channelRangingBackoffStart) {
        this.channelRangingBackoffStart = channelRangingBackoffStart;
    }

    public void setChannelRangingBackoffEnd(Integer channelRangingBackoffEnd) {
        this.channelRangingBackoffEnd = channelRangingBackoffEnd;
    }

    public void setChannelTxBackoffStart(Integer channelTxBackoffStart) {
        this.channelTxBackoffStart = channelTxBackoffStart;
    }

    public void setChannelTxBackoffEnd(Integer channelTxBackoffEnd) {
        this.channelTxBackoffEnd = channelTxBackoffEnd;
    }

}
