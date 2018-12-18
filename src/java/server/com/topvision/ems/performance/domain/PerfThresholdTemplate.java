/***********************************************************************
 * $Id: PerfThresholdTemplate.java,v1.0 2013-6-8 下午02:00:47 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.domain;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.topvision.framework.common.DateUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;

/**
 * @author Rod John
 * @created @2013-6-8-下午02:00:47
 * 
 */
public class PerfThresholdTemplate extends BaseEntity implements AliasesSuperType {

    private static final long serialVersionUID = 2823426111720415046L;
    private Integer templateId;
    private String templateName;
    private Integer templateType;
    private String typeDisplayName;
    private Integer parentType;
    private String parentDisplayName;
    private Integer type;
    private String createUser;
    private Boolean isDefaultTemplate;
    private Timestamp createTime;
    private String createTimeString;
    private Timestamp modifyTime;
    private String modifyTimeString;
    private Long relaDeviceNum;

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
     * @return the templateType
     */
    public Integer getTemplateType() {
        return templateType;
    }

    /**
     * @param templateType
     *            the templateType to set
     */
    public void setTemplateType(Integer templateType) {
        this.templateType = templateType;
    }

    /**
     * @return the isDefaultTemplate
     */
    public Boolean getIsDefaultTemplate() {
        return isDefaultTemplate;
    }

    /**
     * @param isDefaultTemplate
     *            the isDefaultTemplate to set
     */
    public void setIsDefaultTemplate(Boolean isDefaultTemplate) {
        this.isDefaultTemplate = isDefaultTemplate;
    }

    /**
     * @return the createTime
     */
    public Timestamp getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     *            the createTime to set
     */
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the modifyTime
     */
    public Timestamp getModifyTime() {
        return modifyTime;
    }

    /**
     * @param modifyTime
     *            the modifyTime to set
     */
    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getCreateTimeString() {
        if (createTimeString == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            createTimeString = sdf.format(createTime);
        }
        return createTimeString;
    }

    public void setCreateTimeString(String createTimeString) {
        this.createTimeString = DateUtils.format(this.getCreateTime());
    }

    public String getModifyTimeString() {
        if (modifyTimeString == null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            modifyTimeString = sdf.format(modifyTime);
        }
        return modifyTimeString;
    }

    public void setModifyTimeString(String modifyTimeString) {
        this.modifyTimeString = DateUtils.format(this.getModifyTime());
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getRelaDeviceNum() {
        return relaDeviceNum;
    }

    public void setRelaDeviceNum(Long relaDeviceNum) {
        this.relaDeviceNum = relaDeviceNum;
    }

    public Integer getParentType() {
        return parentType;
    }

    public void setParentType(Integer parentType) {
        this.parentType = parentType;
    }

    public String getTypeDisplayName() {
        return typeDisplayName;
    }

    public void setTypeDisplayName(String typeDisplayName) {
        this.typeDisplayName = typeDisplayName;
    }

    public String getParentDisplayName() {
        return parentDisplayName;
    }

    public void setParentDisplayName(String parentDisplayName) {
        this.parentDisplayName = parentDisplayName;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PerfThresholdTemplate [templateId=");
        builder.append(templateId);
        builder.append(", templateName=");
        builder.append(templateName);
        builder.append(", templateType=");
        builder.append(templateType);
        builder.append(", typeDisplayName=");
        builder.append(typeDisplayName);
        builder.append(", parentType=");
        builder.append(parentType);
        builder.append(", parentDisplayName=");
        builder.append(parentDisplayName);
        builder.append(", type=");
        builder.append(type);
        builder.append(", createUser=");
        builder.append(createUser);
        builder.append(", isDefaultTemplate=");
        builder.append(isDefaultTemplate);
        builder.append(", createTime=");
        builder.append(createTime);
        builder.append(", createTimeString=");
        builder.append(createTimeString);
        builder.append(", modifyTime=");
        builder.append(modifyTime);
        builder.append(", modifyTimeString=");
        builder.append(modifyTimeString);
        builder.append(", relaDeviceNum=");
        builder.append(relaDeviceNum);
        builder.append("]");
        return builder.toString();
    }

}
