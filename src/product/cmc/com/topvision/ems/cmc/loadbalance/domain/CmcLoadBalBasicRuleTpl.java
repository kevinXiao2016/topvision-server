/***********************************************************************
 * $Id: CmcLoadBalBasicRuleTpl.java,v1.0 2013-4-24 下午3:12:42 $
 * 
 * @author: dengl
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.loadbalance.domain;

import java.util.Calendar;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.type.Alias;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * CMC负载均衡规则模板
 * @author dengl
 * @created @2013-4-24-下午3:12:42
 *
 */
@Alias("cmcLoadBalBasicRuleTpl")
public class CmcLoadBalBasicRuleTpl implements AliasesSuperType {
    private static final long serialVersionUID = 6614995171380931386L;
    public static final Integer RULE_ENABLED = 1;
    public static final Integer RULE_DISABLED = 2;
    public static final Integer RULE_DISABLED_PERIOD = 3;

    private Long ruleTplId;
    /**
     * 是否进行负载均衡
     * enabled(1) disabled(2) disabledPeriod(3) 
     */
    private Integer docsLoadBalBasicRuleEnable;
    /**
     * 不进行负载均衡的开始时间，设备实现为距离0点的秒数
     * 范围：0..86400
     */
    private Integer docsLoadBalBasicRuleDisStart;
    /**
     * If object docsLoadBalBasicRuleEnable is disablePeriod(3)
     * Load Balancing is disabled for the period of time defined between docsLoadBalBasicRuleDisStart and
     * docsLoadBalBasicRuleDisStart plus the period of time of
     * docsLoadBalBasicRuleDisPeriod. Otherwise, this object value has no meaning.
     * 范围：0..86400
     */
    private Integer docsLoadBalBasicRuleDisPeriod;

    public Long getRuleTplId() {
        return ruleTplId;
    }

    public void setRuleTplId(Long ruleTplId) {
        this.ruleTplId = ruleTplId;
    }

    public Integer getDocsLoadBalBasicRuleEnable() {
        return docsLoadBalBasicRuleEnable;
    }

    public void setDocsLoadBalBasicRuleEnable(Integer docsLoadBalBasicRuleEnable) {
        this.docsLoadBalBasicRuleEnable = docsLoadBalBasicRuleEnable;
    }

    public Integer getDocsLoadBalBasicRuleDisStart() {
        return docsLoadBalBasicRuleDisStart;
    }

    public void setDocsLoadBalBasicRuleDisStart(Integer docsLoadBalBasicRuleDisStart) {
        this.docsLoadBalBasicRuleDisStart = docsLoadBalBasicRuleDisStart;
    }

    public Integer getDocsLoadBalBasicRuleDisPeriod() {
        return docsLoadBalBasicRuleDisPeriod;
    }

    public void setDocsLoadBalBasicRuleDisPeriod(Integer docsLoadBalBasicRuleDisPeriod) {
        this.docsLoadBalBasicRuleDisPeriod = docsLoadBalBasicRuleDisPeriod;
    }

    /**
     * 08:30格式数据转成距离00:00的秒数
     * @param disStart
     * @return
     */
    public static Integer toDisStart(String disStart) {
        if (disStart != null && !disStart.equals("")) {
            String[] s = disStart.split(":");
            return Integer.valueOf(s[0]) * 60 * 60 + Integer.valueOf(s[1]) * 60;
        }
        return null;
    }

    /**
     * 开始和结束时间相隔的秒数
     * @param disStart 格式 08:00
     * @param disEnd 格式 08:00
     * @return
     */
    public static Integer toDisPeriod(String disStart, String disEnd) {
        if (StringUtils.isNotBlank(disStart) && StringUtils.isNotBlank(disEnd)) {
            Calendar cStart = Calendar.getInstance();
            String[] s1 = disStart.split(":");
            cStart.set(Calendar.HOUR_OF_DAY, Integer.valueOf(s1[0]));
            cStart.set(Calendar.MINUTE, Integer.valueOf(s1[1]));
            cStart.set(Calendar.SECOND, 0);

            Calendar cEnd = Calendar.getInstance();
            String[] s2 = disEnd.split(":");
            cEnd.set(Calendar.HOUR_OF_DAY, Integer.valueOf(s2[0]));
            cEnd.set(Calendar.MINUTE, Integer.valueOf(s2[1]));
            cEnd.set(Calendar.SECOND, 0);

            if (cEnd.compareTo(cStart) <= 0) {
                cEnd.add(Calendar.DAY_OF_MONTH, 1);
            }

            return (int) ((cEnd.getTimeInMillis() - cStart.getTimeInMillis()) / 1000);
        }

        return null;
    }

    public String getDisStartTime() {
        return CmcLoadBalBasicRuleTpl.toDisStartTime(this.docsLoadBalBasicRuleDisStart);
    }

    public String getDisEndTime() {
        return CmcLoadBalBasicRuleTpl.toDisEndTime(this.docsLoadBalBasicRuleDisStart,
                this.docsLoadBalBasicRuleDisPeriod);
    }

    /**
     * 负载均衡无效的开始时间,格式(08:30)
     * @return
     */
    public static String toDisStartTime(Integer disStart) {
        return toFmtTime(disStart);
    }

    /**
     * 负载均衡无效的结束时间,格式(08:30)
     * @return
     */
    public static String toDisEndTime(Integer disStart, Integer disPeriod) {
        if (disStart != null && disPeriod != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            Long bef = calendar.getTimeInMillis();
            calendar.add(Calendar.SECOND, disStart + disPeriod);
            Long aft = calendar.getTimeInMillis();

            return toFmtTime((int) ((aft - bef) / 1000));
        }
        return "";
    }

    public static String toFmtTime(Integer seconds) {
        if (seconds != null) {
            int hours = seconds / 60 / 60; // 小时
            int minutes = (seconds / 60) % 60; // 分钟

            StringBuffer ret = new StringBuffer();
            
            /*只有在超过24时才用，本地设定最长24时
            if (hours >= 24) {
                hours = hours - 24;
            }*/
            if (hours < 10) {
                ret.append("0");
            }

            ret.append(hours);
            ret.append(":");

            if (minutes < 10) {
                ret.append("0");
            }

            ret.append(minutes);
            return ret.toString();
        }
        return "";
    }

}
