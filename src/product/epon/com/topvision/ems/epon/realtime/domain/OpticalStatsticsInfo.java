/***********************************************************************
 * $Id: OpticalStatsticsInfo.java,v1.0 2014-8-23 上午10:21:47 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.realtime.domain;

import java.util.ArrayList;
import java.util.List;

import com.topvision.ems.performance.domain.PerfThresholdRule;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponConstants;
import com.topvision.platform.zetaframework.util.Validator;

/**
 * @author flack
 * @created @2014-8-23-上午10:21:47
 * 记录下级设备光功率分区间数据
 */
public class OpticalStatsticsInfo implements AliasesSuperType, Comparable<OpticalStatsticsInfo> {
    private static final long serialVersionUID = -9188858887092712593L;

    public static final int OPTICAL_NORMAL_LEVEL = 0;

    private int operate;
    private int value;
    private int level;

    private Integer start;
    private Integer end;
    private Integer num = 0;
    private String color;
    private Integer startCompare;
    private Integer endCompare;
    private boolean includeStart;
    private boolean includeEnd;

    public OpticalStatsticsInfo() {

    }

    public OpticalStatsticsInfo(int operate, int value, int level) {
        super();
        this.operate = operate;
        this.value = value;
        this.level = level;
    }

    public int getOperate() {
        return operate;
    }

    public void setOperate(int operate) {
        this.operate = operate;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
        if (start == null) {
            this.startCompare = null;
        } else {
            this.startCompare = start * 100;
        }
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
        if (end == null) {
            this.endCompare = null;
        } else {
            this.endCompare = end * 100;
        }
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getStartCompare() {
        return startCompare;
    }

    public void setStartCompare(Integer startCompare) {
        this.startCompare = startCompare;
    }

    public Integer getEndCompare() {
        return endCompare;
    }

    public void setEndCompare(Integer endCompare) {
        this.endCompare = endCompare;
    }

    public boolean isIncludeStart() {
        return includeStart;
    }

    public void setIncludeStart(boolean includeStart) {
        this.includeStart = includeStart;
    }

    public boolean isIncludeEnd() {
        return includeEnd;
    }

    public void setIncludeEnd(boolean includeEnd) {
        this.includeEnd = includeEnd;
    }

    public void addMatchNum() {
        num++;
    }

    /**
     * 修改compareTo方法实现，保证按告警级别从高到低排序
     */
    @Override
    public int compareTo(OpticalStatsticsInfo o) {
        if (level < o.level) {
            return 1;
        } else if (level > o.level) {
            return -1;
        }
        return 0;
    }

    /**
     * 将定义的阈值转化为对应的对象列表
     * @param rule
     * @return
     */
    public static List<OpticalStatsticsInfo> parse(PerfThresholdRule rule) {
        List<OpticalStatsticsInfo> list = new ArrayList<OpticalStatsticsInfo>();
        String thresholds = rule.getThresholds();
        String[] ts = thresholds.split("#");
        for (String threshold : ts) {
            if (Validator.isTrue(threshold)) {
                String[] $thd = threshold.split("_");
                int operate = Integer.parseInt($thd[0]);
                int value = Integer.parseInt($thd[1]);
                int level = Integer.parseInt($thd[2]);
                OpticalStatsticsInfo $threshold = new OpticalStatsticsInfo(operate, value, level);
                list.add($threshold);
            }
        }
        return list;
    }

    /**
     * 计算分段阀值所属的范围
     * @param $t
     * @param rxPower
     * @return
     */
    public static boolean satisfy(OpticalStatsticsInfo $t, int thresholdValue) {
        int comValue = $t.getValue();
        switch ($t.getOperate()) {
        case EponConstants.OPERATOR_GREATER: // > 
            return thresholdValue > comValue;
        case EponConstants.OPERATOR_GREATERANDEQUAL: // >=
            return thresholdValue >= comValue;
        case EponConstants.OPERATOR_EQUAL: // ==
            return thresholdValue == comValue;
        case EponConstants.OPERATOR_LESSANDEQUAL: // <=
            return thresholdValue <= comValue;
        case EponConstants.OPERATOR_LESS: // <
            if (thresholdValue == comValue) {
                return true;
            }
            return thresholdValue < comValue;
        default:
            return false;
        }
    }

    public static boolean setIfInclude(int operate, OpticalStatsticsInfo stasticsInfo) {
        switch (operate) {
        case EponConstants.OPERATOR_GREATER: // > 
            stasticsInfo.setIncludeEnd(true);
            return false;
        case EponConstants.OPERATOR_GREATERANDEQUAL: // >=
            stasticsInfo.setIncludeEnd(false);
            return true;
        case EponConstants.OPERATOR_EQUAL: // ==
            stasticsInfo.setIncludeEnd(false);
            return true;
        case EponConstants.OPERATOR_LESSANDEQUAL: // <=
            stasticsInfo.setIncludeEnd(true);
            return false;
        case EponConstants.OPERATOR_LESS: // <
            stasticsInfo.setIncludeEnd(false);
            return true;
        default:
            return false;
        }
    }

    /**
    * 得到告警的颜色
    * @param $t
    * @return
    */
    public static String getColor(int level) {
        switch (level) {
        case EponConstants.ALERT_LEVEL_GENERAL:
            // 一般告警颜色
            return "#FFD700";
        case EponConstants.ALERT_LEVEL_MINOR:
            // 次要告警颜色
            return "#1C97FF";
        case EponConstants.ALERT_LEVEL_MAIN:
            // 主要告警颜色
            return "#FFD700";
        case EponConstants.ALERT_LEVEL_SERIOUS:
            // 严重告警颜色
            return "#FFA500";
        case EponConstants.ALERT_LEVEL_EMERGENCY:
            // 紧急告警颜色
            return "#E41A14";
        default:
            return "#00CE0A";
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OpticalStatsticsInfo [operate=");
        builder.append(operate);
        builder.append(", value=");
        builder.append(value);
        builder.append(", level=");
        builder.append(level);
        builder.append(", start=");
        builder.append(start);
        builder.append(", end=");
        builder.append(end);
        builder.append(", num=");
        builder.append(num);
        builder.append(", color=");
        builder.append(color);
        builder.append(", startCompare=");
        builder.append(startCompare);
        builder.append(", endCompare=");
        builder.append(endCompare);
        builder.append(", includeStart=");
        builder.append(includeStart);
        builder.append(", includeEnd=");
        builder.append(includeEnd);
        builder.append("]");
        return builder.toString();
    }

}
