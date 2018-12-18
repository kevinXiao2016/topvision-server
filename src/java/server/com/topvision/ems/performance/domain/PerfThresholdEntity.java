/***********************************************************************
 * $Id: PerfThresholdEntity.java,v1.0 2013-6-8 下午02:16:17 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;

/**
 * @author Rod John
 * @created @2013-6-8-下午02:16:17
 * 
 */
public class PerfThresholdEntity extends BaseEntity implements AliasesSuperType {

    private static final long serialVersionUID = -2323826143788877146L;
    private Long entityId;
    private String entityName;
    private String mac;
    private Integer entityType;
    private String entityIp;
    private Integer typeId;
    private Long parentId;;
    private String parentName;
    private Integer parentTypeId;
    private Integer templateId;
    private String templateName;
    private Boolean isPerfThreshold;
    private String folderName;
    private String entityTypeDisplayName;
    private Integer start;
    private Integer limit;
    private Integer tempRela;
    private String uplinkDevice;

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the entityIp
     */
    public String getEntityIp() {
        return entityIp;
    }

    /**
     * @param entityIp
     *            the entityIp to set
     */
    public void setEntityIp(String entityIp) {
        this.entityIp = entityIp;
    }

    /**
     * @return the typeId
     */
    public Integer getTypeId() {
        return typeId;
    }

    /**
     * @param typeId
     *            the typeId to set
     */
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    /**
     * @return the templateId
     */
    public Integer getTemplateId() {
        return templateId;
    }

    /**
     * @param templateId
     *            the templateId to set
     */
    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    /**
     * @return the templateName
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * @param templateName
     *            the templateName to set
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     * @return the entityName
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * @param entityName
     *            the entityName to set
     */
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    /**
     * @return the isPerfThreshold
     */
    public Boolean getIsPerfThreshold() {
        return isPerfThreshold;
    }

    /**
     * @param isPerfThreshold
     *            the isPerfThreshold to set
     */
    public void setIsPerfThreshold(Boolean isPerfThreshold) {
        this.isPerfThreshold = isPerfThreshold;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Integer getEntityType() {
        return entityType;
    }

    public void setEntityType(Integer entityType) {
        this.entityType = entityType;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getEntityTypeDisplayName() {
        return entityTypeDisplayName;
    }

    public void setEntityTypeDisplayName(String entityTypeDisplayName) {
        this.entityTypeDisplayName = entityTypeDisplayName;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getParentTypeId() {
        return parentTypeId;
    }

    public void setParentTypeId(Integer parentTypeId) {
        this.parentTypeId = parentTypeId;
    }

    public Integer getTempRela() {
        return tempRela;
    }

    public void setTempRela(Integer tempRela) {
        this.tempRela = tempRela;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getUplinkDevice() {
        return uplinkDevice;
    }

    public void setUplinkDevice(String uplinkDevice) {
        this.uplinkDevice = uplinkDevice;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PerfThresholdEntity [entityId=");
        builder.append(entityId);
        builder.append(", entityName=");
        builder.append(entityName);
        builder.append(", mac=");
        builder.append(mac);
        builder.append(", entityType=");
        builder.append(entityType);
        builder.append(", entityIp=");
        builder.append(entityIp);
        builder.append(", typeId=");
        builder.append(typeId);
        builder.append(", parentId=");
        builder.append(parentId);
        builder.append(", parentName=");
        builder.append(parentName);
        builder.append(", parentTypeId=");
        builder.append(parentTypeId);
        builder.append(", templateId=");
        builder.append(templateId);
        builder.append(", templateName=");
        builder.append(templateName);
        builder.append(", isPerfThreshold=");
        builder.append(isPerfThreshold);
        builder.append(", folderName=");
        builder.append(folderName);
        builder.append(", entityTypeDisplayName=");
        builder.append(entityTypeDisplayName);
        builder.append(", start=");
        builder.append(start);
        builder.append(", limit=");
        builder.append(limit);
        builder.append(", tempRela=");
        builder.append(tempRela);
        builder.append(", uplinkDevice=");
        builder.append(uplinkDevice);
        builder.append("]");
        return builder.toString();
    }

}
