package com.topvision.ems.cm.cmpoll.facade.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author jay
 * @created 15-3-3.
 */
public class CmPollTask implements Serializable {
    private static final long serialVersionUID = 3286404264964208289L;
    private Integer engineId;
    private Long taskId;
    private Long entityId;
    private Long cmcId;
    private Long cmcIndex;
    private Long cmIndex;
    private String cmMac;
    private String cmIp;
    private Integer statusValue;
    private Long cmId;
    private boolean end;
    private Long collectTime;
    private List<DocsIf3CmPollUsStatus> docsIf3CmPollUsStatusList;
    private List<UpstreamChannelInfo> upstreamChannelInfo;
    private List<DownstreamChannelInfo> downstreamChannelList;

    public Integer getEngineId() {
        return engineId;
    }

    public void setEngineId(Integer engineId) {
        this.engineId = engineId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Long getCmcIndex() {
        return cmcIndex;
    }

    public void setCmcIndex(Long cmcIndex) {
        this.cmcIndex = cmcIndex;
    }

    public Long getCmIndex() {
        return cmIndex;
    }

    public void setCmIndex(Long cmIndex) {
        this.cmIndex = cmIndex;
    }

    public String getCmMac() {
        return cmMac;
    }

    public void setCmMac(String cmMac) {
        this.cmMac = cmMac;
    }

    public String getCmIp() {
        return cmIp;
    }

    public void setCmIp(String cmIp) {
        this.cmIp = cmIp;
    }

    public Integer getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(Integer statusValue) {
        this.statusValue = statusValue;
    }

    public Long getCmId() {
        return cmId;
    }

    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public Long getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Long collectTime) {
        this.collectTime = collectTime;
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

    public List<DownstreamChannelInfo> getDownstreamChannelList() {
        return downstreamChannelList;
    }

    public void setDownstreamChannelList(List<DownstreamChannelInfo> downstreamChannelList) {
        this.downstreamChannelList = downstreamChannelList;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmPollTask [entityId=");
        builder.append(entityId);
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", cmcIndex=");
        builder.append(cmcIndex);
        builder.append(", cmIndex=");
        builder.append(cmIndex);
        builder.append(", cmMac=");
        builder.append(cmMac);
        builder.append(", cmIp=");
        builder.append(cmIp);
        builder.append(", statusValue=");
        builder.append(statusValue);
        builder.append(", cmId=");
        builder.append(cmId);
        builder.append("]");
        return builder.toString();
    }

}
