/***********************************************************************
 * $Id: ExcutorPolicy.java,v1.0 2013-6-17 上午11:06:54 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.domain;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author haojie
 * @created @2013-6-17-上午11:06:54
 * 
 */
public class ExcutorTimePolicy implements Serializable {
    private static final long serialVersionUID = -4623892558064720951L;

    public static final int DAILYREPORT = 1;
    public static final int WEEKLYREPORT = 2;
    public static final int MONTHLYREPORT = 3;

    public static final int MON = 1;
    public static final int TUES = 2;
    public static final int WED = 3;
    public static final int THUR = 4;
    public static final int FRI = 5;
    public static final int SAT = 6;
    public static final int SUN = 7;

    public static final int THISPERIOD = 0;// 统计本周
    public static final int LASTPERIOD = 1;// 统计上周

    // 任务报表类型（日/周/月）
    private int cycleType;
    // 本（日/周/月）还是上（日/周/月）统计，0表示本周，1表示上周，对于日报表和月报表，此字段固定为上周统计
    private int excutorType;
    // 周报表选择了哪些天
    private int[] weekDaySelected = new int[7];
    // 对于周报表，此字段表示周几，对于月报表，此字段表示多少号
    private int excutorDay;
    private int excutorHour;
    private int excutorMin;
    private int weekStartDay;

    public int getCycleType() {
        return cycleType;
    }

    public void setCycleType(int cycleType) {
        this.cycleType = cycleType;
    }

    public int getExcutorType() {
        return excutorType;
    }

    public void setExcutorType(int excutorType) {
        this.excutorType = excutorType;
    }

    public int[] getWeekDaySelected() {
        return weekDaySelected;
    }

    public void setWeekDaySelected(int[] weekDaySelected) {
        this.weekDaySelected = weekDaySelected;
    }

    public int getExcutorDay() {
        return excutorDay;
    }

    public void setExcutorDay(int excutorDay) {
        this.excutorDay = excutorDay;
    }

    public int getExcutorHour() {
        return excutorHour;
    }

    public void setExcutorHour(int excutorHour) {
        this.excutorHour = excutorHour;
    }

    public int getExcutorMin() {
        return excutorMin;
    }

    public void setExcutorMin(int excutorMin) {
        this.excutorMin = excutorMin;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ExcutorPolicy [cycleType=");
        builder.append(cycleType);
        builder.append(", excutorType=");
        builder.append(excutorType);
        builder.append(", weekDaySelected=");
        builder.append(Arrays.toString(weekDaySelected));
        builder.append(", excutorDay=");
        builder.append(excutorDay);
        builder.append(", excutorHour=");
        builder.append(excutorHour);
        builder.append(", excutorMin=");
        builder.append(excutorMin);
        builder.append("]");
        return builder.toString();
    }

    public int getWeekStartDay() {
        return weekStartDay;
    }

    public void setWeekStartDay(int weekStartDay) {
        this.weekStartDay = weekStartDay;
    }

}
