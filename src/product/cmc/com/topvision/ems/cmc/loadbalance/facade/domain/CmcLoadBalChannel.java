/***********************************************************************
 * $Id: CmcLoadBalChannel.java,v1.0 2013-4-23 下午5:26:45 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.loadbalance.facade.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2013-4-23-下午5:26:45
 *
 */
@Alias("cmcLoadBalChannel")
public class CmcLoadBalChannel implements Serializable, AliasesSuperType{
    private static final long serialVersionUID = -7100250426017638893L;
    private Long channelId;
    private Long cmcId;
    private Long grpId;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.2.1.3.1.1.1", index = true)
    private Long docsLoadBalGrpId; 
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.2.1.3.2.1.1", index = true)
    private Long docsLoadBalChannelIfIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.4491.2.1.2.1.3.2.1.2", writable = true, type = "Integer32")
    private Integer docsLoadBalChannelStatus;
    public Long getChannelId() {
        return channelId;
    }
    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }
    public Long getGrpId() {
        return grpId;
    }
    public void setGrpId(Long grpId) {
        this.grpId = grpId;
    }
    public Long getDocsLoadBalGrpId() {
        return docsLoadBalGrpId;
    }
    public void setDocsLoadBalGrpId(Long docsLoadBalGrpId) {
        this.docsLoadBalGrpId = docsLoadBalGrpId;
    }
    public Long getDocsLoadBalChannelIfIndex() {
        return docsLoadBalChannelIfIndex;
    }
    public void setDocsLoadBalChannelIfIndex(Long docsLoadBalChannelIfIndex) {
        this.docsLoadBalChannelIfIndex = docsLoadBalChannelIfIndex;
    }
    public Integer getDocsLoadBalChannelStatus() {
        return docsLoadBalChannelStatus;
    }
    public void setDocsLoadBalChannelStatus(Integer docsLoadBalChannelStatus) {
        this.docsLoadBalChannelStatus = docsLoadBalChannelStatus;
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
        builder.append("CmcLoadBalChannel [channelId=");
        builder.append(channelId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", grpId=");
        builder.append(grpId);
        builder.append(", docsLoadBalGrpId=");
        builder.append(docsLoadBalGrpId);
        builder.append(", docsLoadBalChannelIfIndex=");
        builder.append(docsLoadBalChannelIfIndex);
        builder.append(", docsLoadBalChannelStatus=");
        builder.append(docsLoadBalChannelStatus);
        builder.append("]");
        return builder.toString();
    }
}
