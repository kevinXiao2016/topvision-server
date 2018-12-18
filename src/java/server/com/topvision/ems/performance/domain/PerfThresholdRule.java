/***********************************************************************
 * $Id: PerfThresholdRule.java,v1.0 2013-6-8 下午02:21:28 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.domain;

import com.topvision.ems.performance.utils.PerfTargetUtil;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;
import com.topvision.platform.zetaframework.var.UnitConfigConstant;

/**
 * @author Rod John
 * @created @2013-6-8-下午02:21:28
 * 
 */
public class PerfThresholdRule extends BaseEntity implements AliasesSuperType {

    private static final long serialVersionUID = 7963828383158353099L;
    // 主键/外键
    private Integer ruleId;
    // 外键
    private Integer templateId;
    // 指标ID
    private String targetId;
    // 指标的显示名称
    private String displayName;
    // 阈值告警内容(格式化为：action_value_level_priority#action_value_level_priority#action_value_level_priority)
    private String thresholds;
    // 触发条件的时间长度
    private Integer minuteLength;
    // 触发条件的次数
    private Integer number;
    // 是否启动时间段限制(格式化为：startHour:startMin-endHour-endMin#1234567)
    private Integer isTimeLimit;
    // 时间段限制具体内容
    private String timeRange;
    // 模板单位
    private String targetUnit;
    // 清除告警规则(格式化为：action_value#action_value)
    private String clearRules;

    public static PerfThresholdRule formatStrToObject(String str) {
        PerfThresholdRule perfThresholdRule = new PerfThresholdRule();
        String[] splitStrs = str.split("/");
        perfThresholdRule.setTargetId(splitStrs[0]);
        //转换温度阈值
        String tempUnit = (String) UnitConfigConstant.get(UnitConfigConstant.TEMP_UNIT);
        if (tempUnit != null && UnitConfigConstant.FAHR_TEMP_UNIT.equals(tempUnit)) {
            if (UnitConfigConstant.TEMPRELATED_THRESHOLDS.contains(perfThresholdRule.getTargetId())) {
                perfThresholdRule.setThresholds(UnitConfigConstant.praseRuleValueToCenti(splitStrs[1]));
            } else {
                perfThresholdRule.setThresholds(splitStrs[1]);
            }
        } else {
            perfThresholdRule.setThresholds(splitStrs[1]);
        }
        if (UnitConfigConstant.ONU_CATV_RF.equals(perfThresholdRule.getTargetId())) {
            //CATV ONU 的输出电平
            String powerUnit = (String) UnitConfigConstant.get(UnitConfigConstant.POWER_UNIT);
            if (powerUnit != null && UnitConfigConstant.MILLI_VOLT_UNIT.equals(powerUnit)) {
                perfThresholdRule.setThresholds(UnitConfigConstant.parseRuleDBmVToDBμV(splitStrs[1]));
            }
        }
        //perfThresholdRule.setThresholds(splitStrs[1]);
        perfThresholdRule.setMinuteLength(Integer.valueOf(splitStrs[2]));
        perfThresholdRule.setNumber(Integer.valueOf(splitStrs[3]));
        perfThresholdRule.setIsTimeLimit(Integer.valueOf(splitStrs[4]));
        if (Integer.valueOf(splitStrs[4]) == 1) {
            perfThresholdRule.setTimeRange(splitStrs[5]);
            perfThresholdRule.setClearRules(splitStrs[6]);
        } else {
            perfThresholdRule.setTimeRange("");
            perfThresholdRule.setClearRules(splitStrs[5]);
        }
        return perfThresholdRule;
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

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

    public Integer getMinuteLength() {
        return minuteLength;
    }

    public void setMinuteLength(Integer minuteLength) {
        this.minuteLength = minuteLength;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getIsTimeLimit() {
        return isTimeLimit;
    }

    public void setIsTimeLimit(Integer isTimeLimit) {
        this.isTimeLimit = isTimeLimit;
    }

    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = PerfTargetUtil.getString(displayName, "performance");
    }

    public String getTargetUnit() {
        return targetUnit;
    }

    public void setTargetUnit(String targetUnit) {
        this.targetUnit = targetUnit;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PerfThresholdRule [ruleId=");
        builder.append(ruleId);
        builder.append(", templateId=");
        builder.append(templateId);
        builder.append(", targetId=");
        builder.append(targetId);
        builder.append(", displayName=");
        builder.append(displayName);
        builder.append(", thresholds=");
        builder.append(thresholds);
        builder.append(", minuteLength=");
        builder.append(minuteLength);
        builder.append(", number=");
        builder.append(number);
        builder.append(", isTimeLimit=");
        builder.append(isTimeLimit);
        builder.append(", timeRange=");
        builder.append(timeRange);
        builder.append(", targetUnit=");
        builder.append(targetUnit);
        builder.append("]");
        return builder.toString();
    }

    public String getClearRules() {
        return clearRules;
    }

    public void setClearRules(String clearRules) {
        this.clearRules = clearRules;
    }

}
