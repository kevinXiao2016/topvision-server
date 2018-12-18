/***********************************************************************
 * $Id: SpectrumTempConfigLogDetail.java,v1.0 2013-8-17 下午3:41:47 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.frequencyhopping.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author flack
 * @created @2013-8-17-下午3:41:47
 * 
 */
public class SpectrumTempConfigLogDetail implements Serializable {
    private static final long serialVersionUID = -7263704000133305970L;

    private Long configDetailId;
    private Long configLogId;
    private String templateName;
    private String configUnit;
    private String configOperation;
    private Date configTime;
    private Integer configResult;// 0 配置失败 1 配置成功

    public Long getConfigDetailId() {
        return configDetailId;
    }

    public void setConfigDetailId(Long configDetailId) {
        this.configDetailId = configDetailId;
    }

    public Long getConfigLogId() {
        return configLogId;
    }

    public void setConfigLogId(Long configLogId) {
        this.configLogId = configLogId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getConfigUnit() {
        return configUnit;
    }

    public void setConfigUnit(String configUnit) {
        this.configUnit = configUnit;
    }

    public String getConfigOperation() {
        return configOperation;
    }

    public void setConfigOperation(String configOperation) {
        this.configOperation = configOperation;
    }

    public String getConfigTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(configTime);
    }

    public void setConfigTime(Date configTime) {
        this.configTime = configTime;
    }

    public Integer getConfigResult() {
        return configResult;
    }

    public void setConfigResult(Integer configResult) {
        this.configResult = configResult;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SpectrumTempConfigLogDetail [configDetailId=");
        builder.append(configDetailId);
        builder.append(", configLogId=");
        builder.append(configLogId);
        builder.append(", templateName=");
        builder.append(templateName);
        builder.append(", configUnit=");
        builder.append(configUnit);
        builder.append(", configOperation=");
        builder.append(configOperation);
        builder.append(", configTime=");
        builder.append(configTime);
        builder.append(", configResult=");
        builder.append(configResult);
        builder.append("]");
        return builder.toString();
    }
}
