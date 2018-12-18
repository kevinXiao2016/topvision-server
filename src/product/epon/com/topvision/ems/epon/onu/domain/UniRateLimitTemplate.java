/***********************************************************************
 * $Id: UniRateLimitTemplate.java,v1.0 2014-5-15 下午2:37:10 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.domain;

import java.util.Date;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2014-5-15-下午2:37:10
 *
 */
public class UniRateLimitTemplate implements AliasesSuperType {
    private static final long serialVersionUID = -4603810312307928881L;

    private Integer templateId;
    private String templateName;
    private Long entityId;
    private Long uniIndex;
    private Long uniId;
    private Integer portInLimitEnable;
    private Integer portInCIR;
    private Integer portInCBS;
    private Integer portInEBS;
    private Integer portOutLimtEnable;
    private Integer portOutCIR;
    private Integer portOutPIR;
    private Date createTime;
    private Date updateTime;

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getPortInLimitEnable() {
        return portInLimitEnable;
    }

    public void setPortInLimitEnable(Integer portInLimitEnable) {
        this.portInLimitEnable = portInLimitEnable;
    }

    public Integer getPortInCIR() {
        return portInCIR;
    }

    public void setPortInCIR(Integer portInCIR) {
        this.portInCIR = portInCIR;
    }

    public Integer getPortInCBS() {
        return portInCBS;
    }

    public void setPortInCBS(Integer portInCBS) {
        this.portInCBS = portInCBS;
    }

    public Integer getPortInEBS() {
        return portInEBS;
    }

    public void setPortInEBS(Integer portInEBS) {
        this.portInEBS = portInEBS;
    }

    public Integer getPortOutLimtEnable() {
        return portOutLimtEnable;
    }

    public void setPortOutLimtEnable(Integer portOutLimtEnable) {
        this.portOutLimtEnable = portOutLimtEnable;
    }

    public Integer getPortOutCIR() {
        return portOutCIR;
    }

    public void setPortOutCIR(Integer portOutCIR) {
        this.portOutCIR = portOutCIR;
    }

    public Integer getPortOutPIR() {
        return portOutPIR;
    }

    public void setPortOutPIR(Integer portOutPIR) {
        this.portOutPIR = portOutPIR;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUniIndex() {
        return uniIndex;
    }

    public void setUniIndex(Long uniIndex) {
        this.uniIndex = uniIndex;
    }

    public Long getUniId() {
        return uniId;
    }

    public void setUniId(Long uniId) {
        this.uniId = uniId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UniRateLimitTemplate [templateId=");
        builder.append(templateId);
        builder.append(", templateName=");
        builder.append(templateName);
        builder.append(", entityId=");
        builder.append(entityId);
        builder.append(", portInLimitEnable=");
        builder.append(portInLimitEnable);
        builder.append(", portInCIR=");
        builder.append(portInCIR);
        builder.append(", portInCBS=");
        builder.append(portInCBS);
        builder.append(", portInEBS=");
        builder.append(portInEBS);
        builder.append(", portOutLimtEnable=");
        builder.append(portOutLimtEnable);
        builder.append(", portOutCIR=");
        builder.append(portOutCIR);
        builder.append(", portOutPIR=");
        builder.append(portOutPIR);
        builder.append(", createTime=");
        builder.append(createTime);
        builder.append(", updateTime=");
        builder.append(updateTime);
        builder.append("]");
        return builder.toString();
    }

}
