/***********************************************************************
 * $Id: CmPollAttribute.java,v1.0 2015年3月21日 上午9:24:06 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmpoll.taskbuild.domain;

import java.io.Serializable;
import java.util.List;

import com.topvision.ems.cm.cmpoll.facade.domain.*;
import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author loyal
 * @created @2015年3月21日-上午9:24:06
 * 
 */
@Alias("cmPollAttribute")
public class CmPollAttribute implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 327209911487031939L;
    private Long cmId;
    private Long cmcId;
    private Long statusIndex;
    private String statusMacAddress;
    private String statusIpAddress;
    private Integer statusValue;
    private Long cmcIndex;
    private Long entityId;
    private Integer cmRemoteQueryStatus;
    private List<CmPoll3UsRemoteQuery> cmPoll3UsRemoteQueryList;
    private List<CmPoll3DsRemoteQuery> cmPoll3DsRemoteQueryList;
    private List<DocsIf3CmPollUsStatus> docsIf3CmPollUsStatusList;
    private List<UpstreamChannelInfo> upstreamChannelInfo;
    private List<DownstreamChannelInfo> downstreamChannelInfo;

    public Long getCmId() {
        return cmId;
    }

    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getStatusIndex() {
        return statusIndex;
    }

    public void setStatusIndex(Long statusIndex) {
        this.statusIndex = statusIndex;
    }

    public String getStatusMacAddress() {
        return statusMacAddress;
    }

    public void setStatusMacAddress(String statusMacAddress) {
        this.statusMacAddress = statusMacAddress;
    }

    public String getStatusIpAddress() {
        return statusIpAddress;
    }

    public void setStatusIpAddress(String statusIpAddress) {
        this.statusIpAddress = statusIpAddress;
    }

    public Integer getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(Integer statusValue) {
        this.statusValue = statusValue;
    }

    public Long getCmcIndex() {
        return cmcIndex;
    }

    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public List<CmPoll3UsRemoteQuery> getCmPoll3UsRemoteQueryList() {
        return cmPoll3UsRemoteQueryList;
    }

    public void setCmPoll3UsRemoteQueryList(List<CmPoll3UsRemoteQuery> cmPoll3UsRemoteQueryList) {
        this.cmPoll3UsRemoteQueryList = cmPoll3UsRemoteQueryList;
    }

    public List<CmPoll3DsRemoteQuery> getCmPoll3DsRemoteQueryList() {
        return cmPoll3DsRemoteQueryList;
    }

    public void setCmPoll3DsRemoteQueryList(List<CmPoll3DsRemoteQuery> cmPoll3DsRemoteQueryList) {
        this.cmPoll3DsRemoteQueryList = cmPoll3DsRemoteQueryList;
    }

    public List<DocsIf3CmPollUsStatus> getDocsIf3CmPollUsStatusList() {
        return docsIf3CmPollUsStatusList;
    }

    public void setDocsIf3CmPollUsStatusList(List<DocsIf3CmPollUsStatus> docsIf3CmPollUsStatusList) {
        this.docsIf3CmPollUsStatusList = docsIf3CmPollUsStatusList;
    }
    
    public List<UpstreamChannelInfo> getUpstreamChannelInfo() {
        return upstreamChannelInfo;
    }

    public void setUpstreamChannelInfo(List<UpstreamChannelInfo> upstreamChannelInfo) {
        this.upstreamChannelInfo = upstreamChannelInfo;
    }

    public List<DownstreamChannelInfo> getDownstreamChannelInfo() {
        return downstreamChannelInfo;
    }

    public void setDownstreamChannelInfo(List<DownstreamChannelInfo> downstreamChannelInfo) {
        this.downstreamChannelInfo = downstreamChannelInfo;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmPollAttribute [cmId=");
        builder.append(cmId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", statusIndex=");
        builder.append(statusIndex);
        builder.append(", statusMacAddress=");
        builder.append(statusMacAddress);
        builder.append(", statusIpAddress=");
        builder.append(statusIpAddress);
        builder.append(", statusValue=");
        builder.append(statusValue);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", entityId=");
        builder.append(entityId);
        builder.append(", cmPoll3UsRemoteQueryList=");
        builder.append(cmPoll3UsRemoteQueryList);
        builder.append(", cmPoll3DsRemoteQueryList=");
        builder.append(cmPoll3DsRemoteQueryList);
        builder.append(", docsIf3CmPollUsStatusList=");
        builder.append(docsIf3CmPollUsStatusList);
        builder.append("]");
        return builder.toString();
    }

    public void setCmRemoteQueryStatus(Integer cmRemoteQueryStatus) {
        this.cmRemoteQueryStatus = cmRemoteQueryStatus;
    }

    public Integer getCmRemoteQueryStatus() {
        return cmRemoteQueryStatus;
    }
}
