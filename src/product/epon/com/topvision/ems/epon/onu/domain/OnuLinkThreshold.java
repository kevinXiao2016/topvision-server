package com.topvision.ems.epon.onu.domain;

import java.io.Serializable;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * 
 * @author CWQ
 * @created @2017年12月28日-下午5:18:06
 *
 */
public class OnuLinkThreshold implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = -5360860668904724258L;
    private Integer templateId;
    private String targetId;
    private String thresholds;

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getThresholds() {
        return thresholds;
    }

    public void setThresholds(String thresholds) {
        this.thresholds = thresholds;
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
        builder.append("OnuLinkThreshold [templateId=");
        builder.append(templateId);
        builder.append(", targetId=");
        builder.append(targetId);
        builder.append(", thresholds=");
        builder.append(thresholds);
        builder.append("]");
        return builder.toString();
    }
}
