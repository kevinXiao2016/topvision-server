/***********************************************************************
 * $Id: TiemSection.java,v1.0 2013-4-27 下午1:23:19 $
 * 
 * @author: dengl
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author dengl
 * @created @2013-4-27-下午1:23:19
 *
 */
public class TimeSection implements Comparable<TimeSection> {
    private String startTime; //格式 15:30
    private String endTime; //格式 15:30

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(TimeSection timeSection) {
        return TimeSection.compareTime(this.startTime, timeSection.getStartTime());
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * 比较两个时间
     * @param t1 格式  15:30
     * @param t2 格式  15:30
     * @return 1:t1>t2 0:t1==t2 -1:t1<t2
     */
    public static int compareTime(String t1, String t2) {
        int sHour = Integer.valueOf(t1.split(":")[0]);
        int sMinute = Integer.valueOf(t1.split(":")[1]);
        int tHour = Integer.valueOf(t2.split(":")[0]);
        int tMinute = Integer.valueOf(t2.split(":")[1]);

        if (sHour > tHour) {
            return 1;
        } else if (sHour == tHour) {
            if (sMinute > tMinute) {
                return 1;
            } else if (sMinute == tMinute) {
                return 0;
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    /**
     * 合并重叠的时间段
     * @param sections
     * @return
     */
    public static List<TimeSection> mergeTimeSections(List<TimeSection> sections) {
        List<TimeSection> tSections = new ArrayList<TimeSection>();
        // 处理结束时间在开始时间前的时间段，如23:00-4:00
        for (TimeSection s : sections) {
            if (TimeSection.compareTime(s.getStartTime(), s.getEndTime()) > 0) {
                TimeSection t = new TimeSection();
                t.setStartTime("00:00");
                t.setEndTime(s.getEndTime());
                tSections.add(t);

                t = new TimeSection();
                t.setStartTime(s.getStartTime());
                t.setEndTime("24:00");
                tSections.add(t);
            } else if (TimeSection.compareTime(s.getStartTime(), s.getEndTime()) == 0) {
                TimeSection t = new TimeSection();
                t.setStartTime("00:00");
                t.setEndTime("24:00");
                tSections.add(t);
            } else {
                tSections.add(s);
            }
        }

        Collections.sort(tSections);// 先排序
        for (int i = 0; i < tSections.size() - 1; i++) {
            int j = i + 1;
            if (TimeSection.compareTime(tSections.get(i).getEndTime(), tSections.get(j).getStartTime()) >= 0) {
                //处理后一条数据的结束时间比前一条数据结束时间小的情况
                if (TimeSection.compareTime(tSections.get(i).getEndTime(), tSections.get(j).getEndTime()) >= 0) {
                    tSections.set(j, tSections.get(i));
                } else {
                    tSections.get(j).setStartTime(tSections.get(i).getStartTime());
                }

                tSections.set(i, null);
            }
        }

        // 去掉为null的记录
        for (Iterator<TimeSection> iterator = tSections.iterator(); iterator.hasNext();) {
            TimeSection timeSection = (TimeSection) iterator.next();
            if (timeSection == null) {
                iterator.remove();
            }
        }
        return tSections;
    }

    /* 用于测试
    public static void main(String[] args) {
        List<TimeSection> sections = new ArrayList<TimeSection>();
        TimeSection t = new TimeSection();

        t.setStartTime("00:30");
        t.setEndTime("04:30");
        sections.add(t);

        t = new TimeSection();
        t.setStartTime("03:30");
        t.setEndTime("04:30");
        sections.add(t);

        t = new TimeSection();
        t.setStartTime("2:30");
        t.setEndTime("07:30");
        sections.add(t);

        t = new TimeSection();
        t.setStartTime("08:30");
        t.setEndTime("08:30");
        sections.add(t);

        mergeTimeSections(sections);
    }
    */
}
