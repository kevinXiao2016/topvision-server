package com.topvision.ems.epon.onu.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * 
 * @author w1992wishes
 * @created @2017年12月22日-上午8:46:44
 *
 */
public class OnuCommonInfo implements AliasesSuperType {

    private static final long serialVersionUID = 6028810499675248949L;

    protected Long onuId;
    protected Long onuIndex;
    protected Long entityId;
    protected String entityIp;// 局端OLT
    protected String name;// 别名
    protected String onuUniqueIdentification;// Mac | gpon SN
    protected Integer onuOperationStatus;// 状态
    protected Boolean attention;// 是否关注
    protected String onuMac;// onuMac
    protected String onuEorG;
    protected Integer onuPreType; // typeId
    protected Long typeId;
    protected String manageName;// 局端olt name
    protected Integer lastOfflineReason; // 最后一次离线原因
    protected Integer tagId;
    protected String tagName;
    protected Long ponId;
    protected Long portIndex;
    protected Integer perfStats;
    private Integer templateId;

    public Long getOnuId() {
        return onuId;
    }

    public Long getOnuIndex() {
        return onuIndex;
    }

    public Long getEntityId() {
        return entityId;
    }

    public String getEntityIp() {
        return entityIp;
    }

    public String getName() {
        return name;
    }

    public String getOnuUniqueIdentification() {
        return onuUniqueIdentification;
    }

    public Integer getOnuOperationStatus() {
        return onuOperationStatus;
    }

    public Boolean getAttention() {
        return attention;
    }

    public String getOnuMac() {
        return onuMac;
    }

    public String getOnuEorG() {
        return onuEorG;
    }

    public Integer getOnuPreType() {
        return onuPreType;
    }

    public Long getTypeId() {
        return typeId;
    }

    public String getManageName() {
        return manageName;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public void setOnuIndex(Long onuIndex) {
        this.onuIndex = onuIndex;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public void setEntityIp(String entityIp) {
        this.entityIp = entityIp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOnuUniqueIdentification(String onuUniqueIdentification) {
        this.onuUniqueIdentification = onuUniqueIdentification;
    }

    public void setOnuOperationStatus(Integer onuOperationStatus) {
        this.onuOperationStatus = onuOperationStatus;
    }

    public void setAttention(Boolean attention) {
        this.attention = attention;
    }

    public void setOnuMac(String onuMac) {
        this.onuMac = onuMac;
    }

    public void setOnuEorG(String onuEorG) {
        this.onuEorG = onuEorG;
    }

    public void setOnuPreType(Integer onuPreType) {
        this.onuPreType = onuPreType;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public void setManageName(String manageName) {
        this.manageName = manageName;
    }

    public Integer getLastOfflineReason() {
        return lastOfflineReason;
    }

    public void setLastOfflineReason(Integer lastOfflineReason) {
        this.lastOfflineReason = lastOfflineReason;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public Long getPortIndex() {
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
    }

    public Integer getPerfStats() {
        return perfStats;
    }

    public void setPerfStats(Integer perfStats) {
        this.perfStats = perfStats;
    }

    public Integer getTagId() {
        return tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OnuDeviceInfo [onuId=");
        builder.append(onuId);
        builder.append(", onuIndex=");
        builder.append(onuIndex);
        builder.append(", entityId=");
        builder.append(entityId);
        builder.append(", entityIp=");
        builder.append(entityIp);
        builder.append(", name=");
        builder.append(name);
        builder.append(", onuOperationStatus=");
        builder.append(onuOperationStatus);
        builder.append(", onuUniqueIdentification=");
        builder.append(onuUniqueIdentification);
        builder.append(", lastOfflineReason=");
        builder.append(lastOfflineReason);
        builder.append(", ponId=");
        builder.append(ponId);
        builder.append(", tagId=");
        builder.append(tagId);
        builder.append(", tagName=");
        builder.append(tagName);
        builder.append(", portIndex=");
        builder.append(portIndex);
        builder.append(", perfStats=");
        builder.append(perfStats);
        builder.append(", templateId=");
        builder.append(templateId);
        builder.append("]");
        return builder.toString();
    }

}
