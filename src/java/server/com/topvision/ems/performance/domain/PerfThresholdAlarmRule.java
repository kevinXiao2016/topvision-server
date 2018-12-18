/***********************************************************************
 * $Id: PerfThresholdAlarmRule.java,v1.0 2013-9-4 上午09:38:35 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.domain;

/**
 * @author lizongtian
 * @created @2013-9-4-上午09:38:35
 *
 */
public class PerfThresholdAlarmRule implements Comparable<PerfThresholdAlarmRule> {
    private Byte level;
    private Integer action;
    private Float value;
    private Integer priority;
    private Float clearValue;
    private Integer clearAction;

    public Byte getLevel() {
        return level;
    }

    public void setLevel(Byte level) {
        this.level = level;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public Float getValue() {
        return value;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PerfThresholdAlarmRule [level=");
        builder.append(level);
        builder.append(", action=");
        builder.append(action);
        builder.append(", value=");
        builder.append(value);
        builder.append("]");
        return builder.toString();
    }

    @Override
    public int compareTo(PerfThresholdAlarmRule o) {
        if (this.priority < o.priority) {
            return 1;
        } else {
            return 0;
        }
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Float getClearValue() {
        return clearValue;
    }

    public void setClearValue(Float clearValue) {
        this.clearValue = clearValue;
    }

    public Integer getClearAction() {
        return clearAction;
    }

    public void setClearAction(Integer clearAction) {
        this.clearAction = clearAction;
    }

}
