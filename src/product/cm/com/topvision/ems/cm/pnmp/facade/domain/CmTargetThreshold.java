package com.topvision.ems.cm.pnmp.facade.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

public class CmTargetThreshold implements AliasesSuperType {

    private static final long serialVersionUID = -6745487793555354963L;

    private String targetName;//指标名称
    private String thresholdName;//阈值名称
    private Integer thresholdLevel;//阈值级别
    private Double lowValue;//阈值下限
    private Double highValue;//阈值上限

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getThresholdName() {
        return thresholdName;
    }

    public void setThresholdName(String thresholdName) {
        this.thresholdName = thresholdName;
    }

    public Integer getThresholdLevel() {
        return thresholdLevel;
    }

    public void setThresholdLevel(Integer thresholdLevel) {
        this.thresholdLevel = thresholdLevel;
    }

    public Double getLowValue() {
		return lowValue;
	}

	public void setLowValue(Double lowValue) {
		this.lowValue = lowValue;
	}

	public Double getHighValue() {
		return highValue;
	}

	public void setHighValue(Double highValue) {
		this.highValue = highValue;
	}

	@Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmTargetThreshold [targetName=");
        builder.append(targetName);
        builder.append(", thresholdName=");
        builder.append(thresholdName);
        builder.append(", thresholdLevel=");
        builder.append(thresholdLevel);
        builder.append(", lowValue=");
        builder.append(lowValue);
        builder.append(", highValue=");
        builder.append(highValue);
        builder.append("]");
        return builder.toString();
    }

}
