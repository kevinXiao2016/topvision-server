/***********************************************************************
 * $ QualityRange.java,v1.0 14-5-11 下午12:45 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jay
 * @created @14-5-11-下午12:45
 */
public class QualityRange {
    public static Integer START = Integer.MIN_VALUE;
    public static Integer END = Integer.MAX_VALUE;
    public static Integer R = 1;
    public static Integer Y = 2;
    public static Integer G = 3;

    private Integer start;
    private Integer end;
    private Integer count = 0;
    private Integer rangeLevel;
    private Boolean isNa = false;
    private List<Long> cmIndexList = new ArrayList<>();

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Boolean getIsNa() {
        return isNa;
    }

    public void setIsNa(Boolean isNa) {
        this.isNa = isNa;
    }


    public Integer getRangeLevel() {
        return rangeLevel;
    }

    public void setRangeLevel(Integer rangeLevel) {
        this.rangeLevel = rangeLevel;
    }

    public QualityRange nextRange(Integer nextEnd,Integer rangeLevel) {
        QualityRange qualityRange = new QualityRange();
        qualityRange.setStart(end);
        qualityRange.setEnd(nextEnd);
        qualityRange.setRangeLevel(rangeLevel);
        return qualityRange;
    }

    public boolean isInRange(Float value) {
        if (isNa) {
            return value == null;
        } else {
            return value != null && value > start && value <= end;
        }
    }

    public void addCount(Long cmIndex) {
        count++;
        cmIndexList.add(cmIndex);
    }

    public boolean isException() {
        return isNa || rangeLevel.equals(R) || rangeLevel.equals(Y);
    }

    public List<Long> getCmIndexList() {
        return cmIndexList;
    }

    public void setCmIndexList(List<Long> cmIndexList) {
        this.cmIndexList = cmIndexList;
    }
}
