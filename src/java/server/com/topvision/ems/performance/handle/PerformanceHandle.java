/***********************************************************************
 * $Id: PerformanceHandle.java,v1.0 2013-6-13 上午11:48:28 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.handle;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.DelayQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.PerformanceData;
import com.topvision.ems.fault.domain.Event;
import com.topvision.ems.fault.service.EventSender;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.performance.dao.PerfThresholdDao;
import com.topvision.ems.performance.domain.PerfDelayItem;
import com.topvision.ems.performance.domain.PerfThresholdAlarmRule;
import com.topvision.ems.performance.domain.PerfThresholdEntity;
import com.topvision.ems.performance.domain.PerfThresholdEventParams;
import com.topvision.ems.performance.domain.PerfThresholdRule;
import com.topvision.ems.performance.service.CachedThresholdAlertService;
import com.topvision.ems.performance.service.PerfThresholdService;
import com.topvision.ems.performance.service.PerformanceStatistics;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.utils.EponConstants;
import com.topvision.platform.ResourceManager;

/**
 * @author Rod John
 * @created @2013-6-13-上午11:48:28
 * 
 */
public abstract class PerformanceHandle extends BaseService implements PerformanceHandleIf {
    @Autowired
    protected PerfThresholdDao thresholdDao;
    @Autowired
    protected EntityDao entityDao;
    @Autowired
    protected EntityTypeService entityTypeService;
    @Autowired
    protected PerformanceStatistics performanceStatisticsCenter;
    @Autowired
    protected PerfThresholdService perfThresholdService;
    @Autowired
    protected EntityService entityService;
    @Autowired
    protected CachedThresholdAlertService cachedThreshholdAlertService;

    /*
     * // 处理不连续次数的内存变量 protected ConcurrentMap<String, Integer> perfSnap = new
     * ConcurrentHashMap<String, Integer>(); // 处理连续次数的内存变量 protected ConcurrentMap<String, Integer>
     * continueSnap = new ConcurrentHashMap<String, Integer>();
     */
    // 处理正常状态的内存变量
    protected ConcurrentMap<String, Byte> clearSnap = new ConcurrentHashMap<String, Byte>();
    protected DelayQueue<PerfDelayItem> perfEventQueue = new DelayQueue<PerfDelayItem>();
    // 定义性能数据的格式化
    protected DecimalFormat df = new DecimalFormat("0.00");

    // private static final Logger logger = LoggerFactory.getLogger(PerformanceHandle.class);

    /**
     * 标准性能数据处理流程
     * 
     * @param data
     * @return
     */
    @Override
    public void handle(PerformanceData data) {
        Long entityId = data.getEntityId();
        PerfThresholdEntity relation = perfThresholdService.selectPerfTemplateByEntityId(entityId);
        if (relation == null) {
            // 设备不存在阈值模板
            return;
        } else {
            if (relation.getTemplateId() == null || relation.getTemplateId() == EponConstants.TEMPLATE_ENTITY_UNLINK) {
                // 设备没有关联阈值模板
                return;
            } else {
                if (!relation.getIsPerfThreshold()) {
                    // 设备没有启动阈值告警开关
                    return;
                } else {
                    String typeCode = getTypeCode(data);
                    //String targetId = getTargetIdByTypeCode(typeCode);
                    // 获得设备关联模板的规则
                    List<PerfThresholdRule> rules = perfThresholdService.selectTargetRulesByTargetId(
                            relation.getTemplateId(), typeCode);
                    if (rules == null || rules.size() == 0) {
                        return;
                    }
                    PerfThresholdEventParams params = getEventParams(data);
                    if (params != null && params.getPerfValue() != null) {
                        compareRules(data, params, rules);
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /**
     * 获得指标事件参数
     * 
     * @param data
     * @return
     */
    public abstract PerfThresholdEventParams getEventParams(PerformanceData data);

    /**
     * 指标性能规则比较策略
     * 
     * 
     * @param rules
     * @return
     */
    public void compareRules(PerformanceData data, PerfThresholdEventParams param, List<PerfThresholdRule> rules) {
        for (PerfThresholdRule rule : rules) {
            // 对于关联模板的特定指标只会存在单一的规则
            /*if (rule.getTargetId().equals(param.getTargetId())) {*/
            // 比较时间
            if (rule.getIsTimeLimit() == 1 && !compareTime(rule.getTimeRange())) {
                continue;
            }
            // 取出告警规则
            String[] thresholds = rule.getThresholds().split("#");
            // 取出清除规则
            String[] clearRules = rule.getClearRules().split("#");
            // 对阈值告警进行排序
            List<PerfThresholdAlarmRule> alarmRules = new ArrayList<PerfThresholdAlarmRule>();
            for (int i = 0; i < thresholds.length; i++) {
                try {
                    PerfThresholdAlarmRule perfThresholdAlarmRule = new PerfThresholdAlarmRule();
                    perfThresholdAlarmRule.setAction(Integer.parseInt(thresholds[i].split("_")[0]));
                    perfThresholdAlarmRule.setValue(Float.parseFloat(thresholds[i].split("_")[1]));
                    perfThresholdAlarmRule.setLevel(Byte.parseByte(thresholds[i].split("_")[2]));
                    perfThresholdAlarmRule.setPriority(Integer.parseInt(thresholds[i].split("_")[3]));
                    perfThresholdAlarmRule.setClearAction(Integer.parseInt(clearRules[i].split("_")[0]));
                    perfThresholdAlarmRule.setClearValue(Float.parseFloat(clearRules[i].split("_")[1]));
                    alarmRules.add(perfThresholdAlarmRule);
                } catch (Exception e) {
                    logger.error("PerformanceStatisticsCenter TargetId:{} Thresholds:{} ClearRules:{}", rule.getTargetId(),rule.getThresholds(),rule.getClearRules());
                    logger.trace("PerformanceStatisticsCenter",e);
                }
            }
            Collections.sort(alarmRules);

            Float perfValue = param.getPerfValue();
            Integer alertEventId = param.getAlertEventId();
            String source = param.getSource();
            Long entityId = data.getEntityId();

            Float prevValue = cachedThreshholdAlertService.takePrevAlertValue(entityId, alertEventId, source);

            Integer prevLevelId = cachedThreshholdAlertService.takePrevAlertLevelId(entityId, alertEventId, source);
            byte newLevel = 0;
            byte clearLevel = 0;
            boolean findPreAlertComparedNow = false;
            PerfThresholdAlarmRule matchCreateRule = null;
            /* 此处的正确做法是求出前值所属产生规则，再得出对应规则,而不是直接用对应规则进行匹配 **/
            for (PerfThresholdAlarmRule p : alarmRules) {
                if (clearLevel == 0 && prevValue != null && compareAction(p.getAction(), p.getValue(), prevValue)) {
                    findPreAlertComparedNow = true;
                    /**分别计算出当前值是否属于前值的清除条件,和当前值是否属于产生条件*/
                    if (compareAction(p.getClearAction(), p.getClearValue(), perfValue)) {
                        clearLevel = p.getLevel();
                    }
                }

                if (newLevel == 0 && compareAction(p.getAction(), p.getValue(), perfValue)) {
                    /**分别计算出当前值是否属于前值的清除条件,和当前值是否属于产生条件*/
                    newLevel = p.getLevel();
                    matchCreateRule = p;
                }
            }

            /** 此判断为解决平滑升级问题,只有平滑升级后value才可能为空并且CACHE中存在KEY,value为空并且CACHE无key的情况就是告警不存在的情况 */
            if (prevValue == null && cachedThreshholdAlertService.alertKeyExist(entityId, alertEventId, source)) {
                /** 平滑升级后只要存在历史告警并且满足新告警,则不区分前后级别，统一按清除+产生处理 */
                forceClearAndCreate(param, data, matchCreateRule, rule, newLevel, prevLevelId);
            } else if (prevValue != null && !findPreAlertComparedNow) {
                /** 修改阈值后。如果前值存在,并且当前无任何区间匹配,则强制清除 */
                forceClearAndCreate(param, data, matchCreateRule, rule, newLevel, prevLevelId);
            } else if (clearLevel == 0) {/*如果没有满足清除条件*/
                /** 只有当当前告警级别比前告警级别高的时候才能产生告警,由此则可以保证告警的唯一性,即同一告警不会产生多个等级的告警  */
                if (newLevel != 0 && newLevel >= prevLevelId) {
                    createEvent(param, data, matchCreateRule, rule);
                }
            } else {
                if (newLevel == 0) {//只清除不产生
                    /* clear的时候采用prevlevel而不是clearlevel的原因是防止阈值区间被修改后,匹配的clearlevel和原告警level不一致。所有告警的唯一性,所以此处应该强制清除 **/
                    createClearEvent(param, data, (byte) prevLevelId.intValue());
                } else {
                    if (newLevel < clearLevel) {
                        createClearEvent(param, data, (byte) prevLevelId.intValue());
                    }
                    createEvent(param, data, matchCreateRule, rule);
                }
            }

        }
    }

    /**
     * 该方法只适用于两种特例场景
     * 1. 平滑升级后导致的前值数据不存在: 前值为null并且cache中存在key
     * 2. 修改阈值区间后导致前值告警不在当前区间存在:前值不为null但是前值和当前区间不匹配
     * @param param
     * @param data
     * @param matchCreateRule
     * @param rule
     * @param createConditionAndLevel
     * @param prevLevelId 
     */
    private void forceClearAndCreate(PerfThresholdEventParams param, PerformanceData data,
            PerfThresholdAlarmRule matchCreateRule, PerfThresholdRule rule, byte createConditionAndLevel,
            int prevLevelId) {
        /** 如果当前告警级别比前告警级别低,则清除.也适用于当前不告警级别 */
        if (createConditionAndLevel < prevLevelId) {
            createClearEvent(param, data, (byte) prevLevelId);
        }
        if (createConditionAndLevel != 0) {
            createEvent(param, data, matchCreateRule, rule);
        }
    }

    /**
     * @param clearConditionAndLevel 
     * @param data 
     * @param param 
     * 
     */
    private void createClearEvent(PerfThresholdEventParams param, PerformanceData data, byte clearConditionAndLevel) {
        Event event = createEvent(param.getSource(), param.getClearEventId(), data.getEntityId(), (byte) 0,
                param.getMessage() + getString("PerformanceAlert.normal", "performance"));
        event.setClearLevelId(clearConditionAndLevel);
        performanceStatisticsCenter.sendClearEvent(event);

        cachedThreshholdAlertService.removeThresholdAlertValue(data.getEntityId(), param.getAlertEventId(),
                param.getSource());
    }

    /**
     * @param param
     * @param data
     * @param matchCreateRule
     * @param rule
     */
    private void createEvent(PerfThresholdEventParams param, PerformanceData data,
            PerfThresholdAlarmRule matchCreateRule, PerfThresholdRule rule) {
        Event event = createEvent(param.getSource(), param.getAlertEventId(), data.getEntityId(),
                matchCreateRule.getLevel(), param.getMessage() + getString("PerformanceAlert.unNormal", "performance"));
        performanceStatisticsCenter.checkPoint(event, rule.getNumber(), rule.getMinuteLength());

        cachedThreshholdAlertService.cacheThresholdAlertValue(data.getEntityId(), param.getAlertEventId(),
                param.getSource(), param.getPerfValue(), (int) matchCreateRule.getLevel());
    }

    /**
     * 初始化一个PerformanceHandle
     */
    @Override
    @PostConstruct
    public abstract void initialize();

    /**
     * 销毁一个PerformanceHandle
     */
    @Override
    @PreDestroy
    public abstract void destroy();

    /**
     * 返回指标处理器的Flag
     * @param data 
     * 
     * @return
     */
    public abstract String getTypeCode(PerformanceData data);

    /**
     * 
     * 
     * @param compareAction
     * @param compareValue
     * @return
     */
    protected Boolean compareAction(Integer compareAction, Float compare, Float perf) {
        switch (compareAction) {
        case 1: // >
            return perf > compare;
        case 2: // >=
            return perf >= compare;
        case 3: // =
            return perf == compare;
        case 4: // <=
            return perf <= compare;
        case 5: // <
            return perf < compare;
        default:
            return false;
        }
    }

    protected Boolean compareTime(String timeRange) {
        boolean inThisRange = false;
        try {
            String timePart = timeRange.split("#")[0];
            String dayPart = timeRange.split("#")[1];

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK);
            int currentHour = cal.get(Calendar.HOUR_OF_DAY);
            int currentMin = cal.get(Calendar.MINUTE);
            if (dayPart.indexOf(String.valueOf(currentWeekDay)) != -1) {
                int startHour = Integer.parseInt(timePart.split("-")[0].split(":")[0]);
                int startMin = Integer.parseInt(timePart.split("-")[0].split(":")[1]);
                int endHour = Integer.parseInt(timePart.split("-")[1].split(":")[0]);
                int endMin = Integer.parseInt(timePart.split("-")[1].split(":")[1]);
                if (startHour == currentHour) {
                    if (startMin <= currentMin) {
                        inThisRange = true;
                    }
                } else if (startHour < currentHour) {
                    if (currentHour < endHour) {
                        inThisRange = true;
                    } else if (currentHour == endHour) {
                        if (currentMin <= endMin) {
                            inThisRange = true;
                        }
                    }
                }
                // if((startHour<=currentHour && currentHour<= endHour) && (startMin<=currentMin &&
                // currentMin<=end))

            }
            return inThisRange;

            //String dayPart = timeRange.split("#")[0];
            //String timePart = timeRange.split("#")[1];
            //Calendar cal = Calendar.getInstance();
            //cal.setTimeInMillis(System.currentTimeMillis());
            //int day = cal.get(Calendar.DAY_OF_WEEK);
            //int hour = cal.get(Calendar.HOUR_OF_DAY); // 判断日期 
            //if (dayPart.indexOf(String.valueOf(day)) != -1) {
            //    int startHour = Integer.parseInt(timePart.split("-")[0]);
            //    int endHour = Integer.parseInt(timePart.split("-")[1]); // 判断小时 
            //    if (hour >= startHour && hour <= endHour) {
            //        return true;
            //    }
            //}
            //return false;
        } catch (Exception e) {
            return false;
        }
    }

    protected String getHost(Long entityId) {
        Entity entity = entityDao.selectByPrimaryKey(entityId);
        if (entityTypeService.isCcmtsWithoutAgent(entity.getTypeId()) || entityTypeService.isOnu(entity.getTypeId())) {
            Entity oltEntity = entityDao.selectByPrimaryKey(entity.getParentId());
            return oltEntity.getIp();
        }
        return entity.getIp();
    }

    protected String getMac(Long entityId) {
        Entity entity = entityDao.selectByPrimaryKey(entityId);
        if (entity != null) {
            //兼容CMTS设备Source的处理
            if (entity.getMac() != null) {
                return entity.getMac();
            } else {
                return entity.getIp();
            }
        } else {
            return null;
        }
    }

    protected Event createEvent(String source, Integer typeId, Long entityId, Byte levelId, String message) {
        Event event = EventSender.getInstance().createEvent(typeId, getHost(entityId), source.toString());
        event.setEntityId(entityId);
        event.setLevelId(levelId);
        event.setMessage(message);
        return event;
    }

    protected String getAdditionText(Entity entity) {
        return entity.getMac() != null ? entity.getMac() : entity.getIp();
    }

    // 国际化方法
    protected String getString(String key, String module) {
        module = String.format("com.topvision.ems.%s.resources", module);
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager(module);
            return resourceManager.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }

}
