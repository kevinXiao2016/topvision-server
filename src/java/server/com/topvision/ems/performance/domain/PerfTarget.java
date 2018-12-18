/***********************************************************************
 * $Id: PerfTarget.java,v1.0 2013-6-17 上午09:46:38 $
 * 
 * @author: lzt
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.domain;

import java.math.BigDecimal;

import com.topvision.ems.performance.utils.PerfTargetUtil;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author lzt
 * @created @2013-6-17 上午09:46:38
 * @modify @2013-9-4 by fanzidong
 * 
 */
public class PerfTarget implements AliasesSuperType {
    private static final long serialVersionUID = -4783695200024326294L;
    private String targetId;
    private Integer targetType;
    private String targetDisplayName;
    private String targetGroup;
    private String unit;
    private BigDecimal maxNum;
    private BigDecimal minNum;
    private Boolean enableStatus;
    private Integer regexpValue;

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public Integer getTargetType() {
        return targetType;
    }

    public void setTargetType(Integer targetType) {
        this.targetType = targetType;
    }

    public String getTargetDisplayName() {
        return targetDisplayName;
    }

    public void setTargetDisplayName(String targetDisplayName) {
        this.targetDisplayName = PerfTargetUtil.getString(targetDisplayName, "performance");
    }

    public String getTargetGroup() {
        return targetGroup;
    }

    public void setTargetGroup(String targetGroup) {
        this.targetGroup = PerfTargetUtil.getString(targetGroup, "performance");;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(BigDecimal maxNum) {
        this.maxNum = maxNum;
    }

    public BigDecimal getMinNum() {
        return minNum;
    }

    public void setMinNum(BigDecimal minNum) {
        this.minNum = minNum;
    }

    public Boolean getEnableStatus() {
        return enableStatus;
    }

    public void setEnableStatus(Boolean enableStatus) {
        this.enableStatus = enableStatus;
    }

    public Integer getRegexpValue() {
        return regexpValue;
    }

    public void setRegexpValue(Integer regexpValue) {
        this.regexpValue = regexpValue;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PerfTarget [targetId=");
        builder.append(targetId);
        builder.append(", targetType=");
        builder.append(targetType);
        builder.append(", targetDisplayName=");
        builder.append(targetDisplayName);
        builder.append(", targetGroup=");
        builder.append(targetGroup);
        builder.append(", unit=");
        builder.append(unit);
        builder.append(", maxNum=");
        builder.append(maxNum);
        builder.append(", minNum=");
        builder.append(minNum);
        builder.append(", enableStatus=");
        builder.append(enableStatus);
        builder.append(", regexpValue=");
        builder.append(regexpValue);
        builder.append("]");
        return builder.toString();
    }

}
