/***********************************************************************
 * $ ViewerParam.java,v1.0 2012-7-15 15:56:12 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.topvision.exception.service.WrongPerfViewerParamException;

/**
 * @author jay
 * @created @2012-7-15-15:56:12
 */
public class ViewerParam implements Serializable {
    private static final long serialVersionUID = 7342180206202447869L;
    public static String TODAY = "Today";
    public static String YESTERDAY = "Yesterday";
    public static String THE_LAST_TWO_DAYS = "The last two days";
    public static String THE_LAST_SEVEN_DAYS = "The last seven days";
    public static String THIS_MONTH = "This month";
    public static String LAST_MONTH = "Last month";
    public static String THE_LAST_THIRTY_DAYS = "The last thirty days";
    public static String THE_LAST_THREE_MONTHS = "The last three months";
    public static String THE_LAST_TWELVE_MONTHS = "The last twelve months";
    public static String THIS_YEAR = "This year";
    public static String CUSTOM = "Custom";
    private String perfType;
    private Long entityId;
    private Long cmcId;
    private Long index;
    private String st;
    private String et;
    private Long stLong;
    private Long etLong;
    private String timeType;
    private Timestamp startTime;
    private Timestamp endTime;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
        this.etLong = endTime.getTime();
        this.et = sdf.format(endTime);
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEt() {
        return et;
    }

    public void setEt(String et) {
        this.et = et;
        try {
            this.etLong = sdf.parse(et).getTime();
            this.endTime = new Timestamp(etLong);
        } catch (ParseException e) {
        }
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
        try {
            this.stLong = sdf.parse(st).getTime();
            this.startTime = new Timestamp(stLong);
        } catch (ParseException e) {
        }
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
        this.stLong = startTime.getTime();
        this.st = sdf.format(startTime);
    }

    public String getPerfType() {
        return perfType;
    }

    public void setPerfType(String perfType) {
        this.perfType = perfType;
    }

    public String getTimeType() {
        return timeType;
    }

    public Long getEtLong() {
        return etLong;
    }

    public void setEtLong(Long etLong) {
        this.etLong = etLong;
        this.endTime = new Timestamp(etLong);
        this.et = sdf.format(endTime);
    }

    public Long getStLong() {
        return stLong;
    }

    public void setStLong(Long stLong) {
        this.stLong = stLong;
        this.startTime = new Timestamp(stLong);
        this.st = sdf.format(startTime);
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
        GregorianCalendar calendar = new GregorianCalendar();
        if (timeType.equalsIgnoreCase(TODAY)) {
            // 今天凌晨0点0分0秒0毫秒
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            setStartTime(new Timestamp(calendar.getTimeInMillis()));
            // 现在
            setEndTime(new Timestamp(System.currentTimeMillis()));
        } else if (timeType.equalsIgnoreCase(YESTERDAY)) {
            // 24小时之前的凌晨0点0分0秒0毫秒
            calendar.setTimeInMillis(System.currentTimeMillis() - 24 * 60 * 60 * 1000L);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            setStartTime(new Timestamp(calendar.getTimeInMillis()));
            // 24小时之前的晚上23点59分59秒999毫秒
            calendar.setTimeInMillis(System.currentTimeMillis() - 24 * 60 * 60 * 1000L);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            setEndTime(new Timestamp(calendar.getTimeInMillis()));
        } else if (timeType.equalsIgnoreCase(THE_LAST_TWO_DAYS)) {
            // 24小时之前的凌晨0点0分0秒0毫秒
            calendar.setTimeInMillis(System.currentTimeMillis() -  24 * 60 * 60 * 1000L);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            setStartTime(new Timestamp(calendar.getTimeInMillis()));
            // 现在
            setEndTime(new Timestamp(System.currentTimeMillis()));
        } else if (timeType.equalsIgnoreCase(THE_LAST_SEVEN_DAYS)) {
            // 6×24小时之前的凌晨0点0分0秒0毫秒
            calendar.setTimeInMillis(System.currentTimeMillis() - 6 * 24 * 60 * 60 * 1000L);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            setStartTime(new Timestamp(calendar.getTimeInMillis()));
            // 现在
            setEndTime(new Timestamp(System.currentTimeMillis()));
        } else if (timeType.equalsIgnoreCase(THIS_MONTH)) {
            // 本月1号的凌晨0点0分0秒0毫秒
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getMinimum(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            setStartTime(new Timestamp(calendar.getTimeInMillis()));
            // 现在
            setEndTime(new Timestamp(System.currentTimeMillis()));
        } else if (timeType.equalsIgnoreCase(LAST_MONTH)) {
            // 上个月1号的0点0分0秒0毫秒
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getMinimum(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            setStartTime(new Timestamp(calendar.getTimeInMillis()));
            // 上个月最后一天的23点59分59秒999毫秒
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getMinimum(Calendar.DAY_OF_MONTH));
            calendar.roll(Calendar.DAY_OF_MONTH, -1);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            setEndTime(new Timestamp(calendar.getTimeInMillis()));
        } else if (timeType.equalsIgnoreCase(THE_LAST_THIRTY_DAYS)) {
            // 29×24小时之前的0点0分0秒0毫秒
            calendar.setTimeInMillis(System.currentTimeMillis() - 29 * 24 * 60 * 60 * 1000L);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            setStartTime(new Timestamp(calendar.getTimeInMillis()));
            // 现在
            setEndTime(new Timestamp(System.currentTimeMillis()));
        } else if (timeType.equalsIgnoreCase(THE_LAST_THREE_MONTHS)) {
            // 3个月前当天所在月份的1号的0点0分0秒0毫秒
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 2);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getMinimum(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            setStartTime(new Timestamp(calendar.getTimeInMillis()));
            // 现在
            setEndTime(new Timestamp(System.currentTimeMillis()));
        } else if (timeType.equalsIgnoreCase(THE_LAST_TWELVE_MONTHS)) {
            // 11个月前当天所在月份的1号的0点0分0秒0毫秒
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 11);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getMinimum(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            setStartTime(new Timestamp(calendar.getTimeInMillis()));
         // 现在
            setEndTime(new Timestamp(System.currentTimeMillis()));
        } else if (timeType.equalsIgnoreCase(THIS_YEAR)) {
            // 今年1月份的1号的0点0分0秒0毫秒
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.MONTH, calendar.getMinimum(Calendar.MONTH));
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getMinimum(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            setStartTime(new Timestamp(calendar.getTimeInMillis()));
            // 现在
            setEndTime(new Timestamp(System.currentTimeMillis()));
        } else if (timeType.equalsIgnoreCase(CUSTOM)) {
            if (st == null || et == null) {
                throw new WrongPerfViewerParamException("timeType is custom, but st and et is null.");
            }
        } else {
            throw new WrongPerfViewerParamException("timeType[" + timeType + "] error");
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ViewerParam");
        sb.append("{cmcId=").append(cmcId);
        sb.append(", perfType='").append(perfType).append('\'');
        sb.append(", entityId=").append(entityId);
        sb.append(", index=").append(index);
        sb.append(", st=").append(st);
        sb.append(", et=").append(et);
        sb.append(", timeType='").append(timeType).append('\'');
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append('}');
        return sb.toString();
    }
}
